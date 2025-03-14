package com.example.Minio.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import org.springframework.stereotype.Service;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import io.minio.Result;

import java.io.FileNotFoundException;
import com.example.Minio.exception.FileUploadException;
import com.example.Minio.exception.FileDownloadException;


@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName = "mybucket";

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
        createBucketIfNotExists();
    }

    private void createBucketIfNotExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking/creating bucket", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileId = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();

            Map<String, String> metadata = new HashMap<>();
            metadata.put("original-filename", originalFilename);

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileId)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .userMetadata(metadata) 
                    .build()
            );
            return  fileId;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }

    public InputStream getFile(String fileId) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileId)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("File doesnot exist", e);
        }
    }

    public String getOriginalFileName(String fileId) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileId)
                    .build()
            );
            return stat.userMetadata().get("original-filename");
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve original filename", e);
        }
    }

    /**
     * Uploads a file to MinIO, storing it with its original filename but associating a UUID in metadata.
     *
     * @param file Multipart file to be uploaded
     * @return The UUID assigned to the file
     */
//    public String uploadFile(MultipartFile file) {
//         try {
//             String fileId = UUID.randomUUID().toString();
//             String originalFilename = file.getOriginalFilename();

//             if (originalFilename == null || originalFilename.isBlank()) {
//                 throw new RuntimeException("Filename cannot be empty");
//             }

//             // Store the UUID as metadata so we can retrieve the file by UUID later
//             Map<String, String> metadata = new HashMap<>();
//             metadata.put("uuid", fileId);

//             // Upload the file with its original filename (instead of UUID)
//             minioClient.putObject(
//                 PutObjectArgs.builder()
//                     .bucket(bucketName)
//                     .object(originalFilename) // Store with original filename
//                     .stream(file.getInputStream(), file.getSize(), -1)
//                     .contentType(file.getContentType())
//                     .userMetadata(metadata)
//                     .build()
//             );
//             return fileId; // Return the UUID to be used for reference
//         } catch (Exception e) {
//             throw new FileUploadException("Error uploading file");
//         }
//     }

    /**
     * Retrieves a file from MinIO using the stored UUID.
     *
     * @param fileId The UUID of the file
     * @return An InputStream containing the file data
     */
    // public InputStream getFile(String fileId) {
    //     try {

    //         // List all objects in the bucket and search for the one with matching UUID metadata
    //         Iterable<Result<Item>> results = minioClient.listObjects(
    //             ListObjectsArgs.builder().bucket(bucketName).build()
    //         );

    //         // Fetch the file's metadata
    //         for (Result<Item> result : results) {
    //             Item item = result.get();
    //             StatObjectResponse stat = minioClient.statObject(
    //                 StatObjectArgs.builder()
    //                     .bucket(bucketName)
    //                     .object(item.objectName()) // Get metadata for each file
    //                     .build()
    //             );
                
    //             // If the file's UUID matches the requested fileId, return the file
    //             if (fileId.equals(stat.userMetadata().get("uuid"))) {
    //                 return minioClient.getObject(
    //                     GetObjectArgs.builder()
    //                         .bucket(bucketName)
    //                         .object(item.objectName()) 
    //                         .build()
    //                 );
    //             }
    //         }
    //         throw new FileDownloadException("File with UUID " + fileId + " not found.");
    //     } catch (Exception e) {
    //         throw new FileDownloadException("Error retrieving file");
    //     }
    // }

    /**
     * Retrieves the original filename for a file using its UUID.
     *
     * @param fileId The UUID of the file
     * @return The original filename stored in MinIO
     */
    // public String getOriginalFileName(String fileId) {
    //     try {

    //         // List all objects in the bucket and search for the one with matching UUID metadata
    //         Iterable<Result<Item>> results = minioClient.listObjects(
    //             ListObjectsArgs.builder().bucket(bucketName).build()
    //         );

    //         for (Result<Item> result : results) {
    //             Item item = result.get();

    //             // Fetch metadata for each file
    //             StatObjectResponse stat = minioClient.statObject(
    //                 StatObjectArgs.builder()
    //                     .bucket(bucketName)
    //                     .object(item.objectName())
    //                     .build()
    //             );

    //             // If the UUID matches, return the actual filename stored in MinIO
    //             if (fileId.equals(stat.userMetadata().get("uuid"))) {
    //                 return item.objectName(); // Return the original filename
    //             }
    //         }
    //         throw new FileDownloadException("Original filename not found for UUID:" + fileId);
    //     } catch (Exception e) {
    //         throw new FileDownloadException("Could not retrieve original filename");
    //     }
    // }




}
