package ml.empee.templateplugin.model.exceptions;

import lombok.Getter;

public class MessageException extends RuntimeException {
  @Getter
  private final String message;

  public MessageException(String message) {
    super(message);

    this.message = message;
  }
}
