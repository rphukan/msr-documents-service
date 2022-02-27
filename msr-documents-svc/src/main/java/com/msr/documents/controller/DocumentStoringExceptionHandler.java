package com.msr.documents.controller;

import com.msr.documents.common.exception.DocumentRetrievingException;
import com.msr.documents.common.exception.DocumentStoringException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * global exception handler
 *
 * Created by ranjan on 7/10/20.
 */
@Slf4j
@RestControllerAdvice
public class DocumentStoringExceptionHandler {

  @ExceptionHandler(DocumentStoringException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleDocumentStoringException(DocumentStoringException ex) {
    log.error("DocumentStoringExceptionHandler >> ", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DocumentRetrievingException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleDocumentRetrievingException(DocumentRetrievingException ex) {
    log.error("DocumentStoringExceptionHandler >> ", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex) {
    log.error("MaxUploadSizeExceededException >> ", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }


}
