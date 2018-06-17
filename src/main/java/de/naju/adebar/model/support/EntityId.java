package de.naju.adebar.model.support;

import java.io.Serializable;

public interface EntityId<T> extends Serializable {

  T getIdentifier();

}
