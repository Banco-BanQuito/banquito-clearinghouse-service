package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.PaymentStatus;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.exception.AccountingIntegrationException;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.exception.FileGenerationException;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.CompensationFile;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.OffUsPayment;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.CompensationFileRepository;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.OffUsPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CompensationFileService {

    private static final Logger log = LoggerFactory.getLogger(CompensationFileService.class);

    private final OffUsPaymentRepository offUsPaymentRepository;
    private final CompensationFileRepository compensationFileRepository;
    private final AccountingService accountingService;
    private final String outputDir;

    public CompensationFileService(
            OffUsPaymentRepository offUsPaymentRepository,
            CompensationFileRepository compensationFileRepository,
            AccountingService accountingService,
            @Value("${compensation.output.dir:}") String outputDir) {

        this.offUsPaymentRepository = offUsPaymentRepository;
        this.compensationFileRepository = compensationFileRepository;
        this.accountingService = accountingService;
        this.outputDir = outputDir != null ? outputDir.trim() : "";
    }

    public CompensationFile generateCompensationFile(UUID batchId) {
        List<OffUsPayment> payments = offUsPaymentRepository.findByBatchId(batchId);

        if (payments == null || payments.isEmpty()) {
            throw new FileGenerationException(
                    "No hay pagos Off-Us para el lote: " + batchId
            );
        }

        int count = payments.size();

        BigDecimal total = payments.stream()
                .map(OffUsPayment::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String fileName = buildFileName(batchId);

        try {
            File dir = resolveOutputDirectory();
            File out = writeCompensationFile(dir, fileName, payments);

            CompensationFile file = buildCompensationFile(
                    batchId,
                    out,
                    count,
                    total
            );

            compensationFileRepository.save(file);

            callAccountingService(batchId, total);

            return file;

        } catch (IOException e) {
            throw new FileGenerationException(
                    "No se pudo generar el archivo de compensación: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Scheduled(fixedRate = 30000) // Runs every 30 seconds
    public void generateSpiFile() {
        List<OffUsPayment> pendingPayments = offUsPaymentRepository.findByStatus(PaymentStatus.RECEIVED);
        if (pendingPayments.isEmpty()) {
            return;
        }

        log.info("Generando archivo plano SPI para el Banco Central con {} transacciones...", pendingPayments.size());

        try {
            File dir = resolveOutputDirectory();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = "SPI_BCE_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            File file = new File(dir, filename);

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("TRX_ID,ROUTING_CODE,ORIGIN_ACCOUNT,DESTINATION_ACCOUNT,AMOUNT,DATE");
                for (OffUsPayment payment : pendingPayments) {
                    writer.printf("%s,%s,%s,%s,%.2f,%s%n",
                            payment.getTransactionId(),
                            payment.getRoutingCode(),
                            payment.getOriginAccount(),
                            payment.getDestinationAccount(),
                            payment.getAmount(),
                            payment.getCreatedAt()
                    );
                    payment.setStatus(PaymentStatus.ACCOUNTED);
                }
            }

            offUsPaymentRepository.saveAll(pendingPayments);
            log.info("Archivo SPI generado exitosamente: {}", file.getAbsolutePath());

        } catch (Exception e) {
            log.error("Error generando el archivo SPI: {}", e.getMessage());
        }
    }

    private String buildFileName(UUID batchId) {
        String datePart = LocalDate.now(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return String.format(
                "COMPENSACION_%s_%s.txt",
                datePart,
                batchId
        );
    }

    private File resolveOutputDirectory() throws IOException {
        if (!outputDir.isBlank()) {
            return createConfiguredDirectory();
        }
        return createTemporaryDirectory();
    }

    private File createConfiguredDirectory() {
        Path configured = Path.of(outputDir);

        if (configured.getParent() == null) {
            throw new FileGenerationException(
                    "Directorio de salida inválido: no puede ser la raíz del sistema"
            );
        }

        File dir = configured.toFile();

        if (!dir.exists() && !dir.mkdirs()) {
            throw new FileGenerationException(
                    "No se pudo crear el directorio de salida: "
                            + dir.getAbsolutePath()
            );
        }

        validateWritableDirectory(
                dir,
                "El directorio de salida no es escribible: "
        );

        return dir;
    }

    private File createTemporaryDirectory() throws IOException {
        Path userBase = Path.of(
                System.getProperty("user.home"),
                ".banquito",
                "compensation"
        );

        Files.createDirectories(userBase);

        File dir = Files.createTempDirectory(
                userBase,
                "compensation-"
            ).toFile();

        validateWritableDirectory(
                dir,
                "El directorio temporal no es escribible: "
        );

        return dir;
    }

    private void validateWritableDirectory(File dir, String message) {
        if (!dir.canWrite()) {
            throw new FileGenerationException(
                    message + dir.getAbsolutePath()
            );
        }
    }

    private File writeCompensationFile(
            File dir,
            String fileName,
            List<OffUsPayment> payments) throws IOException {

        File out = new File(dir, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {

            writer.write(
                    "transactionId|routingCode|originAccount|destinationAccount|amount|currency|valueDate|concept"
            );
            writer.newLine();

            for (OffUsPayment payment : payments) {
                writer.write(buildLine(payment));
                writer.newLine();
            }
        }

        return out;
    }

    private CompensationFile buildCompensationFile(
            UUID batchId,
            File out,
            int count,
            BigDecimal total) {

        CompensationFile file = new CompensationFile();

        file.setBatchId(batchId);
        file.setFileName(out.getName());
        file.setFilePath(out.getAbsolutePath());
        file.setOffUsRecords(count);
        file.setTotalAmount(total);
        file.setStatus(
                ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.FileStatus.GENERATED
        );
        file.setGeneratedAt(LocalDateTime.now(ZoneId.systemDefault()));

        return file;
    }

    private void callAccountingService(UUID batchId, BigDecimal total) {
        try {
            accountingService.registerOffUsAccountingEntry(batchId, total);
        } catch (Exception ex) {
            throw new AccountingIntegrationException(
                    "Error registrando asiento contable: " + ex.getMessage(),
                    ex
            );
        }
    }

    private String buildLine(OffUsPayment p) {
        String txId = p.getTransactionId() != null
                ? p.getTransactionId().toString()
                : "";

        String routing = p.getRoutingCode() != null
                ? p.getRoutingCode()
                : "";

        String origin = p.getOriginAccount() != null
                ? p.getOriginAccount()
                : "";

        String dest = p.getDestinationAccount() != null
                ? p.getDestinationAccount()
                : "";

        String amount = p.getAmount() != null
                ? p.getAmount().toPlainString()
                : "0";

        String currency = p.getCurrency() != null
                ? p.getCurrency()
                : "";

        String valueDate = p.getValueDate() != null
                ? p.getValueDate().toString()
                : "";

        String concept = p.getConcept() != null
                ? p.getConcept()
                : "";

        return String.join(
                "|",
                txId,
                routing,
                origin,
                dest,
                amount,
                currency,
                valueDate,
                concept
        );
    }
}
