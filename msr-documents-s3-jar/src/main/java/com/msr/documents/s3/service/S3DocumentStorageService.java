package com.msr.documents.s3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.msr.documents.common.exception.DocumentDeletingException;
import com.msr.documents.common.exception.DocumentRetrievingException;
import com.msr.documents.common.exception.DocumentStoringException;
import com.msr.documents.common.model.DocumentMetadata;
import com.msr.documents.common.service.AbstractDocumentStorageService;
import com.msr.documents.s3.util.SecurityUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class S3DocumentStorageService extends AbstractDocumentStorageService {

  public static final String S3_BUCKET_ROOT_PATH = "com.msr.lending.documents.";
  public static final String S3_DEFAULT_BUCKET = "com.msr.lending.documents.msr";

  private AmazonS3 amazonS3;

  public S3DocumentStorageService(@Autowired AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  private String getBucketName() {
    String tenantId = SecurityUtils.getTenantId();
    if (null != tenantId) {
      return S3_BUCKET_ROOT_PATH.concat(tenantId.toLowerCase(Locale.ROOT));
    }
    log.error("Could not determine the tenant, storing the document under msr");
    return S3_DEFAULT_BUCKET;
  }

  @Override
  public DocumentMetadata storeFile(String trackingId, String label, MultipartFile file)
      throws DocumentStoringException {

    Assert.notNull("a tracking id must be provided", trackingId);
    Assert.notNull("a label must be provided", label);

    String bucketName = getBucketName();

    try (InputStream stream = file.getInputStream()) {
      if (!this.amazonS3.doesBucketExistV2(bucketName)) {
        log.error("bucket {} does not exist", bucketName);
        throw new DocumentStoringException("The storage location does not exist");
      }
      String path = trackingId + "/" + label + "/" + file.getOriginalFilename();
      PutObjectResult result = this.amazonS3.putObject(bucketName, path, stream, null);
      DocumentMetadata metadata = new DocumentMetadata(result.getETag(), file.getName(),
          file.getOriginalFilename(), file.getContentType(), file.getSize(), "S3", path);
      return metadata;
    } catch (IOException ex) {
      log.error("Could not read the file for storing", ex);
      throw new DocumentStoringException("Could not read the file for storing.");
    } catch (SdkClientException ex) {
      log.error("failed to store file on S3", ex);
      throw new DocumentStoringException("Could not store the file on storage");
    }
  }

  @Override
  public byte[] retrieveFile(DocumentMetadata metadata) throws DocumentRetrievingException {
    String bucketName = getBucketName();
    try {
      log.info("retrieving file {} from bucket {}", metadata.getStoragePath(), bucketName);
      S3Object s3object = this.amazonS3.getObject(bucketName, metadata.getStoragePath());
      log.info("file {} retrieved from bucket {}", metadata.getStoragePath(), bucketName);
      try (S3ObjectInputStream inputStream = s3object.getObjectContent()) {
        log.info("reading file {} retrieved from bucket {}", metadata.getStoragePath(), bucketName);
        return FileCopyUtils.copyToByteArray(inputStream);
      } catch (IOException | SdkClientException ex) {
        log.error("failed to read file retrieved from S3", ex);
        throw new DocumentRetrievingException("Could not read the file retrieved from storage");
      }
    } catch (SdkClientException ex) {
      log.error("failed to retrieve file from S3", ex);
      throw new DocumentRetrievingException("Could not fetch the file from storage");
    }
  }

  @Override
  public void deleteFile(DocumentMetadata metadata) throws DocumentDeletingException {
    String bucketName = getBucketName();
    try {
      log.info("deleting file {} from bucket {}", metadata.getStoragePath(), bucketName);
      this.amazonS3.deleteObject(bucketName, metadata.getStoragePath());
      log.info("file {} deleted from bucket {}", metadata.getStoragePath(), bucketName);
    } catch (SdkClientException ex) {
      log.error("failed to delete file {} from S3 bucket {}",
          metadata.getStoragePath(), bucketName, ex);
      throw new DocumentDeletingException("Exception while deleting file");
    }
  }
}
