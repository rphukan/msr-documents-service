package com.msr.documents.common.exception;

/**
 * checked exception for any kind of file retrieval failure
 *
 * Created by ranjan on 15/10/20.
 */
public class DocumentRetrievingException extends Exception {

  public DocumentRetrievingException(String message) {
    super(message);
  }

  public DocumentRetrievingException(Throwable cause) {
    super(cause);
  }

  public DocumentRetrievingException(String message, Throwable cause) {
    super(message, cause);
  }

}
