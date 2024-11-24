package org.dyplom.aplikacja.exceptions;

public class ReportGenerationException extends RuntimeException {

  public ReportGenerationException(String message) {
    super(message);
  }

  public ReportGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
