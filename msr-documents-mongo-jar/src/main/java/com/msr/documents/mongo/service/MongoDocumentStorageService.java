package com.msr.documents.mongo.service;

import com.msr.documents.common.exception.DocumentRetrievingException;
import com.msr.documents.common.exception.DocumentStoringException;
import com.msr.documents.common.model.DocumentMetadata;
import com.msr.documents.common.service.AbstractDocumentStorageService;
import com.msr.documents.mongo.entity.MongoDocument;
import com.msr.documents.mongo.repository.MongoDocumentRepository;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ranjan on 15/10/20.
 */
@Slf4j
@Service
public class MongoDocumentStorageService extends AbstractDocumentStorageService {

  @Autowired
  private MongoDocumentRepository repository;

  @Override
  public DocumentMetadata storeFile(String trackingId, String label, MultipartFile file) throws DocumentStoringException {
    DocumentMetadata metadata = null;
    try {
      log.info("storing the file : {} in mongodb", file.getOriginalFilename());
      MongoDocument document = new MongoDocument(
          new Binary(BsonBinarySubType.BINARY, file.getBytes()));
      repository.insert(document);
      log.info("file stored, id : {}", document.getDocId());
      metadata = new DocumentMetadata(document.getDocId(), file.getName(),
          file.getOriginalFilename(), file.getContentType(), file.getSize(), "MongoDB", null);
    } catch (IOException ex) {
      log.error("failed to store file : {} in mongodb.", file.getName());
      throw new DocumentStoringException("Error in reading the file bytes", ex);
    } catch (Exception ex) {
      log.error("failed to store file in mongodb.");
      throw new DocumentStoringException("failed to store file in mongodb", ex);
    }
    return metadata;
  }

  @Override
  public byte[] retrieveFile(DocumentMetadata metadata) throws DocumentRetrievingException {
    Optional<MongoDocument> opt = repository.findById(metadata.getId());
    if (opt.isPresent()) {
      MongoDocument document = opt.get();
      return document.getDoc().getData();
    }
    return null;
  }

}
