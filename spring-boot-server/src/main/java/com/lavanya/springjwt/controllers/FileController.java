package com.lavanya.springjwt.controllers;

import com.lavanya.springjwt.models.FileDocument;
import com.lavanya.springjwt.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fileApi")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            FileDocument savedDocument = fileService.compressAndUploadFiles(files);
            return new ResponseEntity<>(savedDocument.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        FileDocument fileDocument = fileService.getFile(id);
        if (fileDocument != null) {
            byte[] content = fileDocument.getContent().getData();  // This is your zipped content
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData(fileDocument.getFilename(), fileDocument.getFilename() + ".zip");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // In your controller class:

    @GetMapping("/files")
    public ResponseEntity<List<FileDocument>> listAllFiles() {
        List<FileDocument> files = fileService.getAllFiles();
        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You might decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }



}
