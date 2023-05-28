package com.springboot.demo.rest;

import com.springboot.demo.constants.DemoCommon;
import com.springboot.demo.dto.NewDocumentDTO;
import com.springboot.demo.dto.OldDocumentDTO;
import com.springboot.demo.exception.DemoNotFoundException;
import com.springboot.demo.exception.MissingRequestBodyException;
import com.springboot.demo.response.DocumentResponse;
import com.springboot.demo.response.HistoryResponse;
import com.springboot.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class DemoController {

    private DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/history")
    public List<HistoryResponse> findHistories() {
        List<HistoryResponse> lsHistoryResponse = this.demoService.findHistories();
        if (lsHistoryResponse.size() == 0) {
            throw new DemoNotFoundException("No exported PDF found at database. Please export PDF file at least 1 to record.");
        }

        return lsHistoryResponse;
    }

    @PostMapping("/new-doc")
    public ResponseEntity<byte[]> saveNewPDF(
            @RequestBody(required = false) NewDocumentDTO payload
    ) throws IOException, SQLException, InterruptedException {

        if (payload == null) {
            throw new MissingRequestBodyException("Request body is missing, but required at least query parameter in JSON.");
        }

        DocumentResponse documentResponse = demoService.saveNewPDF(payload);

        if (documentResponse.getStatus().equals(DemoCommon.ERROR)) {
            throw new DemoNotFoundException("There is something wrong when export PDF document.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documentResponse.getDocument() + "\""
                )
                .body(documentResponse.getBlob());
    }

    @PostMapping("/old-doc")
    public ResponseEntity<byte[]> returnOldPDF(
            @RequestBody(required = false) OldDocumentDTO payload
    ) throws SQLException, IOException {
        byte[] zipBytes;

        if (payload == null) {
            throw new MissingRequestBodyException("Request body is missing, but required at least empty JSON.");
        }
        List<DocumentResponse> documentResponseList = demoService.returnOldPDF(payload);

        if (documentResponseList.size() == 0) {
            throw new DemoNotFoundException("No exported PDF found at database. Please export PDF file at least 1 to record.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for (DocumentResponse documentResponse: documentResponseList) {
                if (documentResponse.getBlob() == null) {
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(documentResponse.getDocument());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(documentResponse.getBlob());
                zipOut.closeEntry();
            }
        }

        zipBytes=baos.toByteArray();

        Instant now = Instant.now();
        String tempZipTime = DateTimeFormatter.ofPattern(DemoCommon.DOCUMENT_PATTERN_FORMAT).withZone(ZoneId.of(DemoCommon.ZONE_ID))
                .format(now);

        String tempZipName = tempZipTime + "_"  + "OLD_DOCUMENTS" + DemoCommon.ZIP_EXTENSION;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentDispositionFormData("attachment", tempZipName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
    }
}
