package com.msr.documents.common.service;

import com.msr.documents.common.exception.DocumentDeletingException;
import com.msr.documents.common.exception.DocumentRetrievingException;
import com.msr.documents.common.exception.DocumentStoringException;
import com.msr.documents.common.model.DocumentMetadata;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implement this interface for a  new document storage type
 * <p>
 * Created by ranjan on 15/10/20.
 */
public interface DocumentStorageService {

  /**
   * store the document passed as a multi part file in the storage system
   */
  DocumentMetadata storeFile(String trackingId, String label, MultipartFile file)
      throws DocumentStoringException;

  /**
   * retrieve the file identified the passed metadata
   */
  byte[] retrieveFile(DocumentMetadata metadata) throws DocumentRetrievingException;

  /**
   * delete the file identified by the metadata
   *
   * @param metadata
   * @throws DocumentDeletingException
   */
  void deleteFile(DocumentMetadata metadata) throws DocumentDeletingException;

}

