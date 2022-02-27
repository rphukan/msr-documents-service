package com.msr.documents.common.exception;

/**
 * checked exception for any kind of file delete failure
 *
 * Created by ranjan on 15/10/20.
 */
public class DocumentDeletingException extends Exception {

  public DocumentDeletingException(String message) {
    super(message);
  }

  public DocumentDeletingException(Throwable cause) {
    super(cause);
  }

  public DocumentDeletingException(String message, Throwable cause) {
    super(message, cause);
  }

}
