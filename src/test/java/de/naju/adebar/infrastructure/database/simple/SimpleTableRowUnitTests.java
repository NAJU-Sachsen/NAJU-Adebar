package de.naju.adebar.infrastructure.database.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SimpleTableRowUnitTests {

	@Test
	public void getColumnIsCaseInsensitive() {
		SimplifiedTableRow row = new SimplifiedTableRow();
		row.append("foo", 42);
		row.append("BaR", 4.2);

		assertThat(row.getColumn("foO")).isEqualTo(42);
		assertThat(row.getColumn("bar")).isEqualTo(4.2);
	}

	@Test
	public void acceptsNullValues() {
		SimplifiedTableRow row = new SimplifiedTableRow();
		row.append("foo", null);
	}

	@Test
	public void getColumnIdxReturnsCorrectObject() {
		SimplifiedTableRow row = new SimplifiedTableRow();
		row.append("foo", "Hello");
		row.append("bar", 42);
		row.append("baz", "World");
		assertThat(row.getColumn(0)).isEqualTo("Hello");
		assertThat(row.getColumn(1)).isEqualTo(42);
		assertThat(row.getColumn(2)).isEqualTo("World");
	}

	/*
	 * It is *really* important that column names are always valid, so we are testing them for a lot
	 * of errors
	 */

	@Test
	public void rejectsNullColumnNames() {
		assertThrows(IllegalArgumentException.class, () -> {
			SimplifiedTableRow row = new SimplifiedTableRow();
			row.append(null, "foo");
		});
	}

	@Test
	public void rejectsEmptyColumnNames() {
		assertThrows(IllegalArgumentException.class, () -> {
			SimplifiedTableRow row = new SimplifiedTableRow();
			row.append("", "foo");
		});
	}

	@Test
	public void rejectsWhitespaceColumnNames() {
		assertThrows(IllegalArgumentException.class, () -> {
			SimplifiedTableRow row = new SimplifiedTableRow();
			row.append("\t\n", "foo");
		});
	}

}
