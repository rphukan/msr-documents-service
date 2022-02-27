package com.msr.documents.exception;

/**
 * Created by ranjan on 10/11/20.
 */
public class TenantIdMissingException extends RuntimeException {

  public TenantIdMissingException(String message) {
    super(message);
  }

}
