package de.naju.adebar.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class FunctionalUnitTests {

	@Test
	public void usesMatchingCase() {
		Object res = Functional.match("foo").caseOf(String.class, __ -> {
			return "Hello";
		}).run();

		assertThat(res).isEqualTo("Hello");
	}

	@Test
	public void skipsNonMatches() {
		Object res = Functional //
				.match("foo") //
				.caseOf(Integer.class, //
						__ -> {
							return "World";
						}) //
				.caseOf(String.class, //
						__ -> {
							return "Hello";
						}) //
				.run();

		assertThat(res).isEqualTo("Hello");
	}

	@Test
	public void passesMatchParam() {
		Object res = Functional.match("foo").caseOf(String.class, val -> {
			return val + " bar";
		}).run();

		assertThat(res).isEqualTo("foo bar");
	}

	@Test
	public void usesDefaultAction() {
		Object res = Functional.match("foo").defaultCase(__ -> {
			return "Hello";
		}).run();

		assertThat(res).isEqualTo("Hello");
	}

	// TODO add test for static cases

}
