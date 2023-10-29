package com.bezkoder.springjwt.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Document(collection = "files")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class FileDocument {
    @Id
    private String id;
    private String filename;
    private Binary content;

}
