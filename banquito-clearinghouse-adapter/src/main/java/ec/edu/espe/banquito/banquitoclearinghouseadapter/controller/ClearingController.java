package ec.edu.espe.banquito.banquitoclearinghouseadapter.controller;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.ClearingFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.service.ClearingQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v2/clearing")
public class ClearingController {
    private final ClearingQueryService clearingQueryService;

    public  ClearingController(ClearingQueryService clearingQueryService) {
        this.clearingQueryService = clearingQueryService;
    }

    @GetMapping("/batches/{batchId}/file")
    public ResponseEntity<ClearingFileResponse> getfile(@PathVariable UUID batchId) {
        return ResponseEntity.ok(clearingQueryService.findByBatchId(batchId));
    }
}
