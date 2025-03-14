# Spring Boot MinIO File Upload and Download

## Description

This project is a Spring Boot 3.2.3 application with Java 17 that provides endpoints to upload and download files to/from a MinIO server. The project uses the MinIO SDK to interact with the server, and each uploaded file is assigned a unique identifier (UUID).

## Features
- File upload endpoint
- File download endpoint
- Integration with MinIO server
- UUID generation for each uploaded file

## Prerequisites
- Java 17
- Spring Boot 3.2.3
- MinIO Server (can be local or remote)

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/spring-boot-minio-file-upload-download.git
cd spring-boot-minio-file-upload-download
```

### 2. Install Dependencies
Make sure to have [Maven](https://maven.apache.org/install.html) installed. Then, run:

```bash
mvn clean install
```

### 3. Configure MinIO
Create an `application.properties` (or `application.yml`) file in `src/main/resources/` and configure your MinIO server connection:

```properties
minio.url=http://localhost:9000
minio.access-key=your-access-key
minio.secret-key=your-secret-key
minio.bucket-name=your-bucket-name
```

### 4. Install and Run MinIO Server
#### Download MinIO Server
Download the MinIO server executable for Windows from the following link:
[MinIO Server Download](https://dl.min.io/server/minio/release/windows-amd64/minio.exe)

#### Setup MinIO Server
1. Create a folder anywhere you want; MinIO Server will save the files in it.
2. Open Command Prompt and navigate to the folder where `minio.exe` is downloaded:

```bash
cd C:\Users\<user-name>\Downloads
```

3. Run the MinIO server in the folder you created and on port `9090`:

```bash
minio.exe server D:\Minio\data --console-address ":9090"
```

### 5. Run the Application
Run the Spring Boot application using:

```bash
mvn spring-boot:run
```

### 6. Access Endpoints

- **Upload File**
  - URL: `POST /api/upload`
  - Request body: Multipart file
  - Response: UUID of the uploaded file

- **Download File**
  - URL: `GET /api/download/{fileId}`
  - Response: The requested file

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
