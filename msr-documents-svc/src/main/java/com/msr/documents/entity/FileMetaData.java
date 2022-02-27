package com.msr.documents.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * the document metadata stored
 *
 * Created by ranjan on 15/10/20.
 */
@Data
@NoArgsConstructor
@Document("doc_meta")
public class FileMetaData {

  @Id
  private String id;

  public FileMetaData(String docId, String fileName, String label, String mime, Long size,
      String storage, String storagePath) {
    this.docId = docId;
    this.fileName = fileName;
    this.label = label;
    this.mime = mime;
    this.size = size;
    this.storage = storage;
    this.storagePath = storagePath;
  }

  @Indexed(unique = true)
  private String docId;

  @Indexed(unique = true)
  private String trackingId;

  private String fileName;

  private String label;

  private String mime;

  private Long size;

  private String storage;

  private String storagePath;

  @CreatedDate
  private LocalDateTime createdOn;

  @CreatedBy
  private String createdBy;

  @LastModifiedDate
  private LocalDateTime lastModifiedOn;

  @LastModifiedBy
  private String lastModifiedBy;

}
