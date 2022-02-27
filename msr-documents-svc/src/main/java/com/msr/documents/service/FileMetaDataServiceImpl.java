package com.msr.documents.service;

import com.msr.documents.common.model.DocumentInfo;
import com.msr.documents.common.model.DocumentMetadata;
import com.msr.documents.entity.FileMetaData;
import com.msr.documents.repository.FileMetaDataRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service to store the metadata of the document that was stored in the document storage system
 *
 * Created by ranjan on 15/10/20.
 */
@Slf4j
@Service
public class FileMetaDataServiceImpl implements FileMetaDataService {

  @Autowired
  private FileMetaDataRepository fileMetaDataRepository;

  @Override
  public FileMetaData prepareMetadata(DocumentMetadata metadata) {
    log.debug("preparing the file metadata for storing");
    FileMetaData fileMetaData = new FileMetaData(metadata.getId(), metadata.getFileName(), null,
        metadata.getMime(), metadata.getSize(), metadata.getStorage(), metadata.getStoragePath());
    return fileMetaData;
  }

  @Override
  public DocumentInfo prepareInfo(FileMetaData fileMetaData) {
    log.debug("preparing the model with file metadata");
    DocumentInfo documentInfo = new DocumentInfo(fileMetaData.getId(), fileMetaData.getFileName(),
        fileMetaData.getLabel(), fileMetaData.getStorage(), fileMetaData.getStoragePath());
    return documentInfo;
  }

  @Override
  public DocumentMetadata prepareStorageMetadata(FileMetaData metadata) {
    log.debug("preparing the storage system metadata");
    DocumentMetadata documentMetadata = new DocumentMetadata(metadata.getDocId(),
        metadata.getLabel(), metadata.getFileName(), metadata.getMime(), metadata.getSize(),
        metadata.getStorage(), metadata.getStoragePath());
    return documentMetadata;
  }

  @Override
  public DocumentInfo storeMetadata(FileMetaData fileMetaData) {
    log.debug("store the file metadata and return a metadata model");
    fileMetaData = fileMetaDataRepository.insert(fileMetaData);
    DocumentInfo documentInfo = prepareInfo(fileMetaData);
    return documentInfo;
  }

  @Override
  public DocumentInfo storeMetadata(DocumentMetadata metadata) {
    log.debug("convert and store the file metadata and return a metadata model");
    FileMetaData fileMetaData = prepareMetadata(metadata);
    fileMetaData = fileMetaDataRepository.insert(fileMetaData);
    DocumentInfo documentInfo = prepareInfo(fileMetaData);
    return documentInfo;
  }

  @Override
  public FileMetaData fetchMetadata(String id) {
    Optional<FileMetaData> opt = fileMetaDataRepository.findById(id);
    if (opt.isPresent()) {
      log.debug("fetching file metadata with id {}", id);
      return opt.get();
    } else {
      log.debug("file metadata not found with id {}", id);
    }
    return null;
  }

  @Override
  public DocumentInfo fetchMetainfo(String id) {
    DocumentInfo info = null;
    Optional<FileMetaData> opt = fileMetaDataRepository.findById(id);
    if (opt.isPresent()) {
      log.debug("fetching file metadata with id {}", id);
      info = prepareInfo(opt.get());
    } else {
      log.debug("file metadata not found with id {}", id);
    }
    return info;
  }

  @Override
  public DocumentMetadata fetchStorageMetadata(String id) {
    DocumentMetadata documentMetadata = null;
    Optional<FileMetaData> opt = fileMetaDataRepository.findById(id);
    if (opt.isPresent()) {
      log.debug("fetching file metadata with id {}", id);
      documentMetadata = this.prepareStorageMetadata(opt.get());
    } else {
      log.debug("file metadata not found with id {}", id);
    }
    return documentMetadata;
  }
}
