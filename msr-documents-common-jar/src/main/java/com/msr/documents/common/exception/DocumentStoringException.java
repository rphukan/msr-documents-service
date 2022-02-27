package com.msr.documents.common.exception;

/**
 * checked exception for any kind of file storage failure
 *
 * Created by ranjan on 15/10/20.
 */
public class DocumentStoringException extends Exception {

  public DocumentStoringException(String message) {
    super(message);
  }

  public DocumentStoringException(Throwable cause) {
    super(cause);
  }

  public DocumentStoringException(String message, Throwable cause) {
    super(message, cause);
  }

}
