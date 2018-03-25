package de.naju.adebar.app.filter.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.app.filter.AbstractFilter;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * An {@link AbstractFilter} which consumes and produces a {@link Predicate} which may then be
 * applied to some {@link QuerydslPredicateExecutor}
 *
 * @author Rico Bergmann
 */
public interface AbstractPredicateBasedFilter extends AbstractFilter<BooleanBuilder> {

}
