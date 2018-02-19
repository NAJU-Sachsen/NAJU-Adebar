package de.naju.adebar.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class Arrays2UnitTests {

  private int[] intArr = {1, 0, 2, 9, 3, 8, 4, 7, 5, 6};
  private String[] strArr = {"Hello", "World", "how", "art", "thou", "?"};
  private String[] strArrWithNulls = {"Hello", "World", "how", null, "art", "thou", "?"};

  @Test
  public void linearSearch_findsExistingPrimitiveElements() {
    int key = 7;
    int position = Arrays2.linearSearch(intArr, key);
    assertThat(position).isEqualTo(7);
  }

  @Test
  public void linearSearch_doesNotFindNonExistingPrimitiveElements() {
    int key = 42;
    int position = Arrays2.linearSearch(intArr, key);
    assertThat(position).isEqualTo(-1);
  }

  @Test
  public void linearSearch_findsExistingComplexElements() {
    String key = "World";
    int position = Arrays2.linearSearch(strArr, key);
    assertThat(position).isEqualTo(1);
  }

  @Test
  public void linearSearch_doesNotFindNonExistingComplexElements() {
    String key = "foo";
    int position = Arrays2.linearSearch(strArr, key);
    assertThat(position).isEqualTo(-1);
  }

  @Test
  public void linearSearch_skipsNullObjectsIfKeyIsNotNull() {
    String key = "art";
    int position = Arrays2.linearSearch(strArrWithNulls, key);
    assertThat(position).isEqualTo(4);
  }

  @Test
  public void linearSearch_doesNotFindNullKeyIfElementsNotNull() {
    String key = null;
    int position = Arrays2.linearSearch(strArr, key);
    assertThat(position).isEqualTo(-1);
  }

  @Test
  public void linearSearch_findsNullObjectIfKeyIsNull() {
    String key = null;
    int position = Arrays2.linearSearch(strArrWithNulls, key);
    assertThat(position).isEqualTo(3);
  }

}
