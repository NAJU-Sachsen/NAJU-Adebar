package de.naju.adebar.app.filter.predicates;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.app.filter.AbstractFilter;

/**
 * An {@link AbstractFilter} which consumes and produces a {@link Predicate} which may then be
 * applied to some {@link QueryDslPredicateExecutor}
 *
 * @author Rico Bergmann
 */
public interface AbstractPredicateBasedFilter extends AbstractFilter<BooleanBuilder> {

}
