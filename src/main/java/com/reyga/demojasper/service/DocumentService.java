package com.reyga.demojasper.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reyga.demojasper.dto.request.SampleRequest;
import com.reyga.demojasper.dto.response.SampleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final ReportService reportService;

    public SampleResponse execute(SampleRequest request) {
        try {
            String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            log.debug("Saving Data");
            SampleResponse response = new SampleResponse();
            response.setId(request.getId());
            response.setName(request.getName());
            response.setFilename(request.getFileName());
            String documentFileName = dateFormat + "_" + request.getFileName() + ".pdf";
            log.debug("Start Process Print PDF : {} ", documentFileName);
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("id", response.getId());
            parameters.put("name", response.getName());
            parameters.put("filename", response.getFilename());
            parameters.put("title", dateFormat);
            reportService.compileReport(parameters, documentFileName, List.of(response));
            return response;
        } catch (Exception ex) {
            log.error("Exception : {}", ex.getMessage());
            return null;
        }
    }

    public byte[] executeFile(SampleRequest request) {
        try {
            String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            log.debug("Saving Data");
            SampleResponse data = SampleResponse.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .filename(request.getFileName())
                    .build();
            String documentFileName = dateFormat + "_" + request.getFileName() + ".pdf";
            log.debug("Start Process Print PDF: {}", documentFileName);
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("id", data.getId());
            parameters.put("name", data.getName());
            parameters.put("filename", data.getFilename());
            parameters.put("title", dateFormat);
            return reportService.compileReportFile(parameters, documentFileName, List.of(data));
        } catch (Exception ex) {
            log.error("Exception: {}", ex.getMessage());
            return null;
        }
    }

}
