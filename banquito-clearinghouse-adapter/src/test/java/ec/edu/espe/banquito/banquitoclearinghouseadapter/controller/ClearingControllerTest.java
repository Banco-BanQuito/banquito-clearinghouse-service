package ec.edu.espe.banquito.banquitoclearinghouseadapter.controller;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.ClearingFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.CompensationFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.FileStatus;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.CompensationFile;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.CompensationFileRepository;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.service.ClearingQueryService;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.service.CompensationFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClearingControllerTest {

    @Mock
    private ClearingQueryService clearingQueryService;

    @Mock
    private CompensationFileRepository compensationFileRepository;

    @Mock
    private CompensationFileService compensationFileService;

    @InjectMocks
    private ClearingController clearingController;

    @Test
    void consolidate_debeUsarFechaProvista_cuandoSeEnviaParametro() {
        LocalDate date = LocalDate.of(2026, 6, 20);
        CompensationFile file = buildFile();
        when(compensationFileService.generateConsolidatedFile(date)).thenReturn(file);

        ResponseEntity<CompensationFileResponse> response = clearingController.consolidate(date);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        verify(compensationFileService).generateConsolidatedFile(date);
    }

    @Test
    void consolidate_debeUsarFechaActual_cuandoNoSeEnviaParametro() {
        CompensationFile file = buildFile();
        when(compensationFileService.generateConsolidatedFile(any(LocalDate.class))).thenReturn(file);

        ResponseEntity<CompensationFileResponse> response = clearingController.consolidate(null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(compensationFileService).generateConsolidatedFile(any(LocalDate.class));
    }

    @Test
    void getfile_debeRetornarRespuestaDelQueryService() {
        UUID batchId = UUID.randomUUID();
        ClearingFileResponse expected = new ClearingFileResponse();
        when(clearingQueryService.findByBatchId(batchId)).thenReturn(expected);

        ResponseEntity<ClearingFileResponse> response = clearingController.getfile(batchId);

        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void listFiles_debeRetornarListaOrdenadaPorFechaDescendente() {
        CompensationFile older = buildFile();
        older.setGeneratedAt(LocalDateTime.of(2026, 6, 1, 0, 0));
        CompensationFile newer = buildFile();
        newer.setGeneratedAt(LocalDateTime.of(2026, 6, 20, 0, 0));
        when(compensationFileRepository.findAll()).thenReturn(List.of(older, newer));

        List<CompensationFileResponse> result = clearingController.listFiles();

        assertThat(result).hasSize(2);
    }

    @Test
    void downloadCsv_debeLanzar404_cuandoArchivoNoExisteEnRepositorio() {
        when(compensationFileRepository.findById("abc")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clearingController.downloadCsv("abc"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void downloadTxt_debeLanzar404_cuandoVarianteNoDisponible() {
        CompensationFile file = buildFile();
        file.setTxtFilePath(null);
        when(compensationFileRepository.findById("abc")).thenReturn(Optional.of(file));

        assertThatThrownBy(() -> clearingController.downloadTxt("abc"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void downloadPdf_debeLanzar404_cuandoArchivoNoExisteEnDisco() {
        CompensationFile file = buildFile();
        file.setPdfFilePath("/ruta/inexistente.pdf");
        when(compensationFileRepository.findById("abc")).thenReturn(Optional.of(file));

        assertThatThrownBy(() -> clearingController.downloadPdf("abc"))
                .isInstanceOf(ResponseStatusException.class);
    }

    private CompensationFile buildFile() {
        CompensationFile file = new CompensationFile();
        file.setBatchId(UUID.randomUUID());
        file.setFileName("clearing.txt");
        file.setFilePath("/files/clearing.txt");
        file.setTxtFilePath("/files/clearing.txt");
        file.setOffUsRecords(1);
        file.setTotalAmount(new BigDecimal("100.00"));
        file.setStatus(FileStatus.GENERATED);
        file.setGeneratedAt(LocalDateTime.now());
        return file;
    }
}
