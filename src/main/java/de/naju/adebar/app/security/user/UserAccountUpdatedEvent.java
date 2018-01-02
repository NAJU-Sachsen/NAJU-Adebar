package de.naju.adebar.app.security.user;

import java.util.Collection;
import de.naju.adebar.model.ChangeSetEntry;
import de.naju.adebar.model.EntityUpdatedEvent;

public class UserAccountUpdatedEvent extends EntityUpdatedEvent<UserAccount> {

  public static UserAccountUpdatedEvent forAccount(UserAccount a) {
    return new UserAccountUpdatedEvent(a);
  }

  public static UserAccountUpdatedEvent withChangeset(UserAccount account,
      Collection<ChangeSetEntry> changeset) {
    return new UserAccountUpdatedEvent(account, changeset);
  }


  protected UserAccountUpdatedEvent(UserAccount account) {
    super(account);
  }

  protected UserAccountUpdatedEvent(UserAccount account, Collection<ChangeSetEntry> changeset) {
    super(account, changeset);
  }

}
