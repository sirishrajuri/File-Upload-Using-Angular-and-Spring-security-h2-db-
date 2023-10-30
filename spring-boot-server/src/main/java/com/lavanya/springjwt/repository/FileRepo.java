package com.lavanya.springjwt.repository;

import com.lavanya.springjwt.models.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepo extends MongoRepository<FileDocument, String> {
}
