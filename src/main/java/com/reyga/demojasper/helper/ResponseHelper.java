package com.reyga.demojasper.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reyga.demojasper.dto.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseHelper {
    public RestResponse errorResponse(String statusCode, String message, ObjectMapper mapper) throws JsonProcessingException {
        RestResponse response = RestResponse.builder()
                .statusCode(statusCode)
                .status("FALSE")
                .message(message)
                .build();
        log.debug("Response {}", mapper.writeValueAsString(response));
        return response;
    }
    public RestResponse successResponse(String statusCode,String message,Object data,ObjectMapper mapper) throws JsonProcessingException {
        RestResponse response = RestResponse.builder()
                .statusCode(statusCode)
                .status("TRUE")
                .message(message)
                .data(data)
                .build();
        log.debug("Response {}", mapper.writeValueAsString(response));
        return response;
    }
}

