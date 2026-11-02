package de.naju.adebar.app.filter.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import de.naju.adebar.app.filter.AbstractFilterableField;

public interface FieldToPathConverter {

	Path<?> getPathFor(AbstractFilterableField field);

	EntityPath<?> getEntityFor(AbstractFilterableField field);

	Join getNecessaryJoinFor(AbstractFilterableField field);

	boolean needsJoinFor(AbstractFilterableField field);

	class Join {

		public static Join of(Path<?> source, Path<?> destination) {
			Join join = new Join();
			join.source = source;
			join.destination = destination;
			return join;
		}

		public Path<?> source;
		public Path<?> destination;

	}

}
