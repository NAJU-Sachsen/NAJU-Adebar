package de.naju.adebar.web.controller.api;

/**
 * Basic response 'code' to indicate failure of an operation. Additional information might be added
 * through an error message
 *
 * @author Rico Bergmann
 *
 */
public class ErrorResponse extends JsonResponse {

  /**
   * Default String to indicate failure
   */
  public static final String RETURN_ERROR = "error";

  private String msg;

  /**
   * Default constructor to solely indicate, that an operation failed
   */
  public ErrorResponse() {
    this("");
  }

  /**
   * Constructor to provide an additional error message
   *
   * @param msg the message
   */
  public ErrorResponse(String msg) {
    super(RETURN_ERROR);
    this.msg = msg;
  }

  /**
   * @return the error message
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @param msg the error message
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
}
