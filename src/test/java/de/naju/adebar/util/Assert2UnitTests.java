package de.naju.adebar.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Assert2UnitTests {

	@Test
	public void noNullElements_detectsSingleNull() {
		assertThrows(IllegalArgumentException.class, () ->
			Assert2.noNullElements(Arrays.asList("bli", "bla", null, "blup")));
	}

	@Test
	public void noNullElements_detectsMultipleNulls() {
		assertThrows(IllegalArgumentException.class, () ->
			Assert2.noNullElements(Arrays.asList("bli", null, "bla", null, "blup", null, null)));
	}

	@Test
	public void noNullElements_doesNotThrowMistakenly() {
		Assert2.noNullElements(Arrays.asList("bli", "bla", "blup"));
	}

	@Test
	public void noNullElements_doesNotThrowOnEmptyIterable() {
		Assert2.noNullElements(Arrays.asList());
	}

	@Test
	public void noNullArguments_detectsSingleNull() {
		assertThrows(IllegalArgumentException.class, () ->
			Assert2.noNullArguments("Foo bar baz", "bli", "bla", null, "blup"));
	}

	@Test
	public void noNullArguments_detectsMultipleNulls() {
		assertThrows(IllegalArgumentException.class, () ->
			Assert2.noNullArguments("Foo bar baz", "bli", null, "bla", null, "blup", null, null));
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
