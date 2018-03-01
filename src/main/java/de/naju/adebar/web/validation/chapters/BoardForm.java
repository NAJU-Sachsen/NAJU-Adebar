package de.naju.adebar.web.validation.chapters;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import de.naju.adebar.util.Validation;

/**
 * Model POJO for boards. The fields are set by Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class BoardForm {

  @NotNull
  private String chairmanId;
  private String email;
  private List<String> memberIds;

  public BoardForm() {
    this.chairmanId = "";
    this.memberIds = new ArrayList<>();
  }

  public BoardForm(String chairmanId, String email, List<String> memberIds) {
    this.chairmanId = chairmanId;
    this.email = email;
    this.memberIds = memberIds;
  }

  public String getChairmanId() {
    return chairmanId;
  }

  public void setChairmanId(String chairmanId) {
    this.chairmanId = chairmanId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getMemberIds() {
    return memberIds;
  }

  public void setMemberIds(List<String> memberIds) {
    this.memberIds = memberIds;
  }

  public boolean hasEmail() {
    return email != null && Validation.isEmail(email);
  }
}
