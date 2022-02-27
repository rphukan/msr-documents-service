package com.msr.documents.controller;

import com.msr.documents.common.exception.DocumentDeletingException;
import com.msr.documents.common.exception.DocumentRetrievingException;
import com.msr.documents.common.exception.DocumentStoringException;
import com.msr.documents.common.model.DocumentInfo;
import com.msr.documents.common.model.DocumentMetadata;
import com.msr.documents.common.service.DocumentStorageService;
import com.msr.documents.entity.FileMetaData;
import com.msr.documents.service.FileMetaDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ranjan on 13/10/20.
 */
@Slf4j
@Data
@RestController
@RequestMapping("/v1/documents")
@SecurityRequirement(name = "security_auth")
@Tag(name = "Documents", description = "Document management APIs")
public class DocumentsController {

  @Autowired
  private FileMetaDataService metaDataService;

  @Autowired
  private DocumentStorageService storageService;

  @Operation(summary = "Upload a document",
      description =
          "Documents will be stored in the document storage system lie S3 or MongoDB etc. This depends on the implementation jar being used."
              + "The documents should be segregated based on the consumer application identifier(eg lending), the tenant identifier, the document type "
              + "and any other custom label provided in the request.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Uploaded",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentInfo.class))}
      ),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
      @ApiResponse(responseCode = "401", description = "Not authorised", content = @Content)
  })
  //@PreAuthorize("hasAuthority('LENDING.loan_application_view')")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadDoc(
      @Parameter(description = "a tracking id to group the docs") @RequestParam(name = "trackingId") String trackingId,
      @Parameter(description = "the document type") @RequestParam(name = "label", required = false) String label,
      @Parameter(description = "the key for attaching the file") @RequestParam("file") MultipartFile file)
      throws DocumentStoringException {

    log.info("uploading file");
    DocumentMetadata metadata = this.storageService.storeFile(trackingId, label, file);
    log.info("file uploaded, storing the metadata");
    FileMetaData fileMetaData = this.metaDataService.prepareMetadata(metadata);
    fileMetaData.setLabel(label);
    fileMetaData.setTrackingId(trackingId);
    DocumentInfo documentInfo = this.metaDataService.storeMetadata(fileMetaData);
    log.info("file upload completed");
    return ResponseEntity.status(HttpStatus.OK).body(documentInfo);
  }

  @Operation(summary = "Download a document",
      description = "Download a document using the metadata id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = @Content),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
      @ApiResponse(responseCode = "401", description = "Not authorised", content = @Content)
  })
  @GetMapping(path = "/{id}")
  public ResponseEntity<?> downloadDoc(
      @Parameter(description = "id of the document metadata") @PathVariable String id)
      throws DocumentRetrievingException {

    log.info("retrieving the document {}", id);
    DocumentMetadata metaData = this.metaDataService.fetchStorageMetadata(id);
    if (null != metaData) {
      byte[] doc = storageService.retrieveFile(metaData);
      String fileName = metaData.getFileName();
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .contentLength(doc.length)
          .body(doc);
    } else {
      log.info("file not found");
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "fetch the file metadata",
      description = "Fetch the details of the stored document using the metadata id. "
          + "The actual document id used by the storage system may be different.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentInfo.class))}
      ),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
      @ApiResponse(responseCode = "401", description = "Not authorised", content = @Content)
  })
  @GetMapping(path = "/{id}/info")
  public ResponseEntity<?> fetchDocInfo(
      @Parameter(description = "id of the file metadata") @PathVariable String id) {

    log.info("fetching the details for the doc {}", id);
    DocumentInfo documentInfo = this.metaDataService.fetchMetainfo(id);
    if (null != documentInfo) {
      return ResponseEntity.ok(documentInfo);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "delete a document",
      description = "delete a document using the metadata id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "deleted", content = @Content),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
      @ApiResponse(responseCode = "401", description = "Not authorised", content = @Content)
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteDoc(
      @Parameter(description = "id of the file metadata") @PathVariable String id)
      throws DocumentDeletingException {
    log.info("deleting the document {}", id);
    DocumentMetadata metaData = this.metaDataService.fetchStorageMetadata(id);
    if (null != metaData) {
      storageService.deleteFile(metaData);
      return ResponseEntity.noContent().build();
    } else {
      log.info("file not found");
    }
    return ResponseEntity.notFound().build();
  }

}
