package de.naju.adebar.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility functions for {@link Map Maps}
 *
 * @author Rico Bergmann
 */
public class Maps {

  private Maps() {}

  /**
   * Generates a new map from the given collection
   *
   * @param collection the collection to use
   * @param keyGenerator function to compute the maps's keys from the collection's elements. If the
   *        generator produces two equal keys for different elements, the last value will be used
   * @return the map
   */
  public static <K, V> Map<K, V> fromCollection(Collection<V> collection,
      Function<V, K> keyGenerator) {
    Map<K, V> theMap = new HashMap<>(collection.size());

    collection.forEach(v -> theMap.put(keyGenerator.apply(v), v));

    return theMap;
  }

}
