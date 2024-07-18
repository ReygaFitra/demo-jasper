
package com.reyga.demojasper.constant;

import lombok.Getter;

public class RestConstants {

    @Getter
    public enum HEADER_NAME {
        REQUEST_ID("X-Request-ID"),
        CORRELATION_ID("X-Correlation-ID"),
        ;

        private final String value;

        HEADER_NAME(String value) {
            this.value = value;
        }

    }

    public interface TopicName {
        String DOCUMENT_PDF = "document-pdf";
        String SEND_EMAIL_BODY = "send-email-body";
        String SEND_EMAIL_HTML_TEMPLATE = "send-email-html-template";
        String SEND_EMAIL_ATTACHMENT = "send-email-attachment";
        String SEND_EMAIL_ATTACHMENT_MINIO = "send-email-attachment-minio";
    }
}
