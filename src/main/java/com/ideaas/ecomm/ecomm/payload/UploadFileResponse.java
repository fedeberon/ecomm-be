package com.ideaas.ecomm.ecomm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contentAsString;
    private long size;

    public UploadFileResponse() { }

    public UploadFileResponse(String fileName, String contentAsString, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.contentAsString = contentAsString;
        this.fileDownloadUrl = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUrl = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

}