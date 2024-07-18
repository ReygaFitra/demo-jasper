package com.reyga.demojasper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reyga.demojasper.dto.request.SampleRequest;
import com.reyga.demojasper.dto.response.RestResponse;
import com.reyga.demojasper.dto.response.SampleResponse;
import com.reyga.demojasper.helper.ResponseHelper;
import com.reyga.demojasper.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/document")
public class DocumentController {

    private final DocumentService documentService;
    private final ResponseHelper responseHelper;

    @PostMapping(value = "/generate")
    public ResponseEntity<RestResponse> generateDocument(@RequestBody SampleRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        log.debug("==================== Document Services Start =============================");
        SampleResponse response = documentService.execute(request);
        try {
            return ResponseEntity.ok()
                    .body(responseHelper.successResponse(HttpStatus.OK.toString(), "Succes", response, mapper));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(responseHelper.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Failed", mapper));
        }
    }

    @PostMapping(value = "/generate-file")
    public ResponseEntity<Resource> generateFileDocument(@RequestBody SampleRequest request) {
        log.debug("==================== Document File Services Start =============================");
        byte[] response = documentService.executeFile(request);
        if (response == null) {
            return ResponseEntity.internalServerError().body(null);
        }
        ByteArrayResource resource = new ByteArrayResource(response);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(request.getFileName().concat(".pdf"))
                                .build().toString())
                .body(resource);
    }
}
