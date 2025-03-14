package com.example.Minio.exception;

import com.example.Minio.Dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.io.FileNotFoundException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ResponseDto response = new ResponseDto(
            null,
            HttpStatus.PAYLOAD_TOO_LARGE.value(),
            "File size exceeds the maximum allowed limit"
        );

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ResponseDto> handleFileUploadException(FileUploadException ex) {
        ResponseDto response = new ResponseDto(
            null,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Upload file failed: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<ResponseDto> handleFileDownloadException(FileDownloadException ex) {
        ResponseDto response = new ResponseDto(
            null,
            HttpStatus.NOT_FOUND.value(),
            "Download file failed: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleOtherExceptions(Exception ex) {
        ResponseDto response = new ResponseDto(
            null,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
