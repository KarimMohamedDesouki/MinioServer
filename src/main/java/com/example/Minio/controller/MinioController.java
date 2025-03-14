package com.example.Minio.controller;

import com.example.Minio.service.MinioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.Minio.Dto.ResponseDto;
import com.example.Minio.exception.FileUploadException;
import com.example.Minio.exception.FileDownloadException;
import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uuid = minioService.uploadFile(file);

            ResponseDto response = new ResponseDto(
                uuid,
                HttpStatus.OK.value(),
                "File uploaded successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
                throw new FileUploadException(e.getMessage());
            }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try (InputStream inputStream = minioService.getFile(fileId)) {
            if (inputStream == null) {
                throw new FileDownloadException("Reference number " + fileId + " does not exist");
            }
            byte[] content = inputStream.readAllBytes();
            String originalFileName = minioService.getOriginalFileName(fileId);

            // Encode the filename for Content-Disposition header
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replace("+", "%20"); 

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);
        } catch (IOException e) {  
            throw new FileDownloadException(e.getMessage());
        }
    }

    //     @GetMapping("/download/{fileId}")
    //     public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
    //         try (InputStream inputStream = minioService.getFile(fileId)) {
    //             if (inputStream == null) {
    //                 throw new FileDownloadException("Reference number " + fileId + " does not exist");
    //             }
    //             byte[] content = inputStream.readAllBytes();
    //         // try {
    //             String originalFileName = minioService.getOriginalFileName(fileId);

    //             // if (originalFileName == null) { 
    //             //         throw new FileDownloadException("Reference number " + fileId + " does not exist");
    //             //     }

    //             // InputStream inputStream = minioService.getFile(originalFileName);
    //             // byte[] content = inputStream.readAllBytes();
    //             // inputStream.close();

    //             // Encode the filename for Content-Disposition header
    //             String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
    //                     .replace("+", "%20"); 

                
    //             return ResponseEntity.ok()
    //                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
    //                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
    //                     .body(content);
    //         } catch (IOException e) {  
    //             throw new FileDownloadException("Error reading file: " + e.getMessage());
    //     }
    // }

}
