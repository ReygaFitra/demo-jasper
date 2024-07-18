package com.reyga.demojasper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RestResponse {
    private String statusCode;
    private String status;
    private String message;
    private Object data;
}
