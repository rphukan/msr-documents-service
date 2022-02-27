package com.msr.documents.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The details of the stored document returned by the storage service implementation.
 *
 * Created by ranjan on 15/10/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {

  //unique id used by the storage system to identify the document
  private String id;

  private String name;

  private String fileName;

  private String mime;

  private Long size;

  //the storage medium like s3, mongodb etc
  private String storage;

  private String storagePath;

}
