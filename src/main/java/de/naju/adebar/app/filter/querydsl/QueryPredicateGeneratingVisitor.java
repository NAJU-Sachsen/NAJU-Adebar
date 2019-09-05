package de.naju.adebar.app.filter.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.CompoundField;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.FilterElementVisitor;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.JoiningFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.app.filter.StringField;
import de.naju.adebar.app.filter.querydsl.FieldToPathConverter.Join;
import de.naju.adebar.documentation.DesignPattern;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.util.Functional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.util.Assert;

/**
 * @param <T>
 * @author Rico Bergmann
 */
@DesignPattern("Visitor")
public class QueryPredicateGeneratingVisitor<T> implements FilterElementVisitor {

	private final FieldToPathConverter pathConverter;
	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;
	private final EntityPath<T> root;
	private final List<Join> joins;
	private final Deque<Predicate> predicateStack;

	/**
	 * @param entityManager
	 * @param root
	 */
	public QueryPredicateGeneratingVisitor( //
			FieldToPathConverter pathConverter, //
			EntityManager entityManager, //
			EntityPath<T> root) {

		Assert.notNull(pathConverter, "Path converter may not be null");
		Assert.notNull(entityManager, "Entity manager may not be null");
		Assert.notNull(root, "Root may not be null");
		this.pathConverter = pathConverter;
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.root = root;
		this.joins = new ArrayList<>();
		this.predicateStack = new LinkedList<>();
	}

	/**
	 * @return
	 */
	public Predicate getResultingCondition() {
		return predicateStack.peek();
	}

	/**
	 * @return
	 */
	public Collection<Join> getJoins() {
		return joins;
	}

	/**
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public JPAQuery<T> getResultingQuery() {
		JPAQuery<T> query = queryFactory.selectFrom(root);

		if (!joins.isEmpty()) {
			// this is a bit ugly but well ¯\_(ツ)_/¯
			joins.forEach(join -> {
				if (join.source instanceof EntityPath) {
					query.join((EntityPath) join.source, join.destination);
				} else if (join.source instanceof CollectionExpression) {
					query.join((CollectionExpression) join.source, join.destination);
				} else if (join.source instanceof MapExpression) {
					query.join((MapExpression) join.source, join.destination);
				}
			});
		}

		query.distinct().where(predicateStack.peek());
		return query;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.ComparingFilter)
	 */
	@Override
	public void visit(ComparingFilter filter) {
		Path<?> dbColumn = pathConverter.getPathFor(filter.getFilteredField());
		Expression<?> comparisonValue = Expressions.constant(filter.getValue());
		Ops operation;
		switch (filter.getComparison()) {
			case EQUAL:
				operation = Functional.<Ops> //
						match(filter.getFilteredField()) //
						.caseOf(StringField.class, field -> {
							if (field.isLargeText()) {
								return Ops.STRING_CONTAINS_IC;
							} else {
								return Ops.EQ_IGNORE_CASE;
							}
						}) //
						.caseOf(CompoundField.class, field -> {
							/*
							 * Compound fields are boolean fields that may either be enabled ('true') meaning that
							 * all matching entities must have the field present or disabled ('false'), meaning
							 * that all matching entities may not have the field present.
							 */
							if (filter.getValue().equals(true)) {
								return Ops.IS_NOT_NULL;
							} else {
								return Ops.IS_NULL;
							}
						}) //
						.defaultCase(__ -> Ops.EQ) //
						.run();
				break;
			case LESS_THAN:
				operation = Ops.LT;
				break;
			case GREATER_THAN:
				operation = Ops.GT;
				break;
			default:
				throw new AssertionError(filter.getComparison());
		}
		Predicate resultingPredicate = Expressions.predicate(operation, dbColumn, comparisonValue);
		predicateStack.push(resultingPredicate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.
	 * ContainmentFilter)
	 */
	@Override
	public void visit(ContainmentFilter filter) {
		Path<?> dbColumn = pathConverter.getPathFor(filter.getField());
		Expression<?> value = Expressions.constant(filter.getValue());
		Predicate resultingPredicate = Expressions.predicate(Ops.STRING_CONTAINS_IC, dbColumn, value);
		predicateStack.push(resultingPredicate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.
	 * InvertableFilter)
	 */
	@Override
	public void visit(InvertableFilter filter) {
		Assert2.isFalse(predicateStack.isEmpty(), "Need a predicate to invert");
		if (filter.isInvertResults()) {
			Predicate currentPredicate = predicateStack.pop();
			Predicate invertedPredicate = Expressions.predicate(Ops.NOT, currentPredicate);
			predicateStack.push(invertedPredicate);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.JoiningFilter)
	 */
	@Override
	public void visit(JoiningFilter filter) {

		// if only one filter is present that has already been visited with so we simply skip execution
		// TODO add test cases for this condition
		if (!filter.getFirstFilter().isPresent() || !filter.getSecondFilter().isPresent()) {
			return;
		} else if (predicateStack.size() < 2) {
			return;
		}

		Assert.isTrue(predicateStack.size() >= 2, "Need at least 2 predicates to join");

		Predicate firstPredicate = predicateStack.pop();
		Predicate secondPredicate = predicateStack.pop();
		Ops join;
		switch (filter.getJoinType()) {
			case AND:
				join = Ops.AND;
				break;
			case OR:
				join = Ops.OR;
				break;
			default:
				throw new AssertionError(filter.getJoinType());
		}
		Predicate joinedPredicate = Expressions.predicate(join, firstPredicate, secondPredicate);
		predicateStack.push(joinedPredicate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.ListFilter)
	 */
	@Override
	public void visit(ListFilter filter) {
		Path<?> dbColumn = pathConverter.getPathFor(filter.getFilteredField());
		BooleanBuilder predicateBuilder = new BooleanBuilder();
		boolean addedAnything = false;
		for (AbstractFilter listFilter : filter) {
			EntityPath<?> dbSubColumn = pathConverter.getEntityFor(listFilter.getFilteredField());
			QueryPredicateGeneratingVisitor<?> subVisitor = //
					new QueryPredicateGeneratingVisitor<>( //
							pathConverter, //
							entityManager, //
							dbSubColumn);
			listFilter.accept(subVisitor);

			if (pathConverter.needsJoinFor(listFilter.getFilteredField())) {
				joins.add(pathConverter.getNecessaryJoinFor(listFilter.getFilteredField()));

				// if we are joining we are not interested in the whole query but only in its conditions
				// and sub-joins
				this.joins.addAll(subVisitor.getJoins());
				predicateBuilder.and(subVisitor.getResultingCondition());
			} else {
				Predicate resultingPredicate = //
						Expressions.predicate( //
								Ops.IN, //
								dbColumn, //
								subVisitor.getResultingQuery());
				switch (filter.getType()) {
					case ALL_MATCH:
						predicateBuilder.and(resultingPredicate);
						break;
					case ANY_MATCHES:
						predicateBuilder.or(resultingPredicate);
						break;
					default:
						throw new AssertionError(filter.getType());
				}
			}
			addedAnything = true;
		}
		if (addedAnything) {
			predicateStack.push(predicateBuilder);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.FilterElementVisitor#visit(de.naju.adebar.app.filter.AbstractFilter)
	 */
	@Override
	public void visit(AbstractFilter filter) {
		throw new IllegalStateException("Unkown filter: " + filter.getClass());
	}

}
