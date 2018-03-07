package de.naju.adebar.util;

import java.util.Arrays;
import org.junit.Test;

public class Assert2UnitTests {

  @Test(expected = IllegalArgumentException.class)
  public void noNullElements_detectsSingleNull() {
    Assert2.noNullElements(Arrays.asList("bli", "bla", null, "blup"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noNullElements_detectsMultipleNulls() {
    Assert2.noNullElements(Arrays.asList("bli", null, "bla", null, "blup", null, null));
  }

  @Test
  public void noNullElements_doesNotThrowMistakenly() {
    Assert2.noNullElements(Arrays.asList("bli", "bla", "blup"));
  }

  @Test
  public void noNullElements_doesNotThrowOnEmptyIterable() {
    Assert2.noNullElements(Arrays.asList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noNullArguments_detectsSingleNull() {
    Assert2.noNullArguments("Foo bar baz", "bli", "bla", null, "blup");
  }

  @Test(expected = IllegalArgumentException.class)
  public void noNullArguments_detectsMultipleNulls() {
    Assert2.noNullArguments("Foo bar baz", "bli", null, "bla", null, "blup", null, null);
  }

  @Test
  public void noNullArguments_doesNotThrowMistakenly() {
    Assert2.noNullArguments("Foo bar baz", "bli", "bla", "blup");
  }

  @Test
  public void noNullArguments_doesNotThrowIfNoArguments() {
    Assert2.noNullArguments("Foo bar baz");
  }

}
