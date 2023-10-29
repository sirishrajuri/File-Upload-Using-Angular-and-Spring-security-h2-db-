package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepo extends MongoRepository<FileDocument, String> {
}
