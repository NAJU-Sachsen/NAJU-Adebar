package de.naju.adebar.web.controller.api;

/**
 * Basic response 'code' structure. Depending on the actual response, additional fields might be
 * added through subclasses.
 *
 * @author Rico Bergmann
 */
public class JsonResponse {

  /**
   * Default String to indicate correct behavior
   */
  public static final String RETURN_OK = "ok";

  protected String status;

  public static JsonResponse ok() {
    return new JsonResponse(RETURN_OK);
  }

  public static JsonResponse status(String status) {
    return new JsonResponse(status);
  }

  /**
   * @param status the response status (e.g. 'error' or 'ok')
   */
  protected JsonResponse(String status) {
    this.status = status;
  }

  /**
   * @return the response status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the response status
   */
  public void setStatus(String status) {
    this.status = status;
  }
}
