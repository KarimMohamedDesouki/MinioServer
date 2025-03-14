package com.example.Minio.Dto;

import java.time.LocalDateTime;

public class ResponseDto {
    private String referenceNumber;
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ResponseDto(String referenceNumber, int statusCode, String message) {
        this.referenceNumber = referenceNumber;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getreferenceNumber() {
        return referenceNumber;
    }

    public void setreferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
