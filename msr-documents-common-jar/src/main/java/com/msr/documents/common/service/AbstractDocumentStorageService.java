package com.msr.documents.common.service;

import com.msr.documents.common.exception.DocumentDeletingException;
import com.msr.documents.common.model.DocumentMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ranjan on 15/10/20.
 */
@Slf4j
public abstract class AbstractDocumentStorageService implements DocumentStorageService {

  //some storage service may not provide a delete capability
  @Override
  public void deleteFile(DocumentMetadata metadata) throws DocumentDeletingException {
    log.info("default delete method. no delete is being performed.");
  }

}
