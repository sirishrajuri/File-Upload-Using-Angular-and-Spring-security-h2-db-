package com.lavanya.springjwt.service;


import com.lavanya.springjwt.models.FileDocument;
import com.lavanya.springjwt.repository.FileRepo;
import net.lingala.zip4j.ZipFile;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepo fileRepository;

    public FileDocument compressAndUploadFiles(MultipartFile[] files) throws Exception {
        // Temporary folder to store the files before zipping
        File tempDir = Files.createTempDirectory("tempFiles").toFile();

        // Save files to temporary directory
        for (MultipartFile file : files) {
            File tempFile = new File(tempDir, Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(tempFile);
        }

        // Create a zip file
        File zipFile = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        ZipFile zip = new ZipFile(zipFile);
        System.out.println("Contents of tempDir:");
        for (File file : tempDir.listFiles()) {
            System.out.println(file.getName());
        }

        zip.addFolder(tempDir);

        // Read the compressed file into Binary format for MongoDB
        Binary binaryData = new Binary(Files.readAllBytes(zipFile.toPath()));

        // Store in MongoDB
        FileDocument document = new FileDocument();
        document.setFilename(zipFile.getName());
        document.setContent(binaryData);
        return fileRepository.save(document);
    }

    public FileDocument getFile(String id) {
        return fileRepository.findById(id).orElse(null);
    }

    // In your FileService class:

    public List<FileDocument> getAllFiles() {
        // This assumes you have a method in your repository to find all documents.
        return fileRepository.findAll();
    }

}
