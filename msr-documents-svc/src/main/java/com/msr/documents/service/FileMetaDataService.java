package com.msr.documents.service;

import com.msr.documents.common.model.DocumentInfo;
import com.msr.documents.common.model.DocumentMetadata;
import com.msr.documents.entity.FileMetaData;

/**
 * Created by ranjan on 15/10/20.
 */
public interface FileMetaDataService {

  /**
   * convert the details of the stored document to a FileMetaData entity for storing into database
   */
  FileMetaData prepareMetadata(DocumentMetadata metadata);

  /**
   * populate the metadata required by the storage system from the stored metadata
   */
  DocumentMetadata prepareStorageMetadata(FileMetaData metadata);

  /**
   * convert the store file metadata to the DocumentInfo model
   */
  DocumentInfo prepareInfo(FileMetaData fileMetaData);


  /**
   * store the file metadata and return the DocumentInfo to the consumer, the consumer can use these
   * details later for retrieving the same document
   */
  DocumentInfo storeMetadata(FileMetaData fileMetaData);

  /**
   * convert the DocumentMetadata to FileMetaData first and store the file metadata. Return the
   * DocumentInfo to the consumer which the consumer can then use  later for retrieving the
   * same document
   */
  DocumentInfo storeMetadata(DocumentMetadata metadata);

  /**
   * fetch the document metadata with the id
   */
  DocumentInfo fetchMetainfo(String id);

  /**
   * fetch the file metadata
   */
  FileMetaData fetchMetadata(String id);

  /**
   * fetch the document metadata with the id and convert it to the metadata accepted by the storage
   * system
   */
  DocumentMetadata fetchStorageMetadata(String id);

}
