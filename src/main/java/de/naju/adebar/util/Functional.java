package de.naju.adebar.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Contains a number of useful classes to mimic
 *
 * @author Rico Bergmann
 */
public class Functional {

	/**
	 *
	 * @param something
	 * @return
	 */
	@NonNull
	public static <R> FunctionalMatch<R> match(@NonNull Object something) {
		return new FunctionalMatch<>(something);
	}

	/**
	 *
	 * @author Rico Bergmann
	 *
	 * @param <R>
	 */
	public static class FunctionalMatch<R> extends Functional {

		@NonNull
		private final Object matchInstance;

		@NonNull
		private final List<Pair<Class<?>, CaseResult<R>>> functions;

		@Nullable
		private CaseResult<R> defaultAction;

		/**
		 *
		 * @param matchInstance
		 */
		private FunctionalMatch(@NonNull Object matchInstance) {
			Assert.notNull(matchInstance, "Object to match on may not be null");
			this.matchInstance = matchInstance;
			this.functions = new ArrayList<>();
		}

		/**
		 * @param clazz
		 * @param action
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <T> FunctionalMatch<R> caseOf(@NonNull Class<T> clazz, @NonNull Function<T, R> action) {
			Assert.notNull(clazz, "Class to match on may not be null");
			Assert.notNull(action, "Action to perform may not be null");
			functions.add(
					Pair.of(clazz, new RunnableCaseResult<>((Function<Object, R>) action, matchInstance)));
			return this;
		}

		/**
		 * @param clazz
		 * @param result
		 * @return
		 */
		public <T> FunctionalMatch<R> caseOf(@NonNull Class<T> clazz, @NonNull R result) {
			Assert.notNull(clazz, "Class to match on may not be null");
			Assert.notNull(result, "Match result may not be null");
			functions.add(Pair.of(clazz, new StaticCaseResult<>(result)));
			return this;
		}

		/**
		 * @param action
		 * @return
		 */
		public FunctionalMatch<R> defaultCase(@NonNull Function<? super Object, R> action) {
			Assert.notNull(action, "Default action may not be null");
			this.defaultAction = new RunnableCaseResult<>(action, matchInstance);
			return this;
		}

		/**
		 * @param result
		 * @return
		 */
		public FunctionalMatch<R> defaultCase(R result) {
			Assert.notNull(result, "Match result may not be null");
			this.defaultAction = new StaticCaseResult<>(result);
			return this;
		}

		/**
		 * Performs the match.
		 * <p>
		 * This will check for the first case to which the match object is assignable and execute the
		 * associated action.
		 *
		 * @return the result of the action or {@code null} if no matching case was specified.
		 */
		public R run() {
			try {
				R res = runOrThrow();
				return res;
			} catch (MatchException e) {
				return null;
			}
		}

		/**
		 * Attempts to perform the match.
		 * <p>
		 * This will check for the first case to which the match object is assignable and execute the
		 * associated action. If no matching action was specified, an exception will be raised.
		 *
		 * @return the result of the action or {@code null} if no matching case was specified.
		 * @throws MatchException if no matching action was given.
		 */
		public R runOrThrow() {
			for (Pair<Class<?>, CaseResult<R>> caseEntry : functions) {
				Class<?> entryClass = caseEntry.getFirst();
				if (entryClass.isAssignableFrom(matchInstance.getClass())) {
					return caseEntry.getSecond().calculate();
				}
			}
			if (defaultAction != null) {
				return defaultAction.calculate();
			}
			throw new MatchException(matchInstance.getClass(), "No case specified: " + matchInstance.getClass());
		}

		private static abstract class CaseResult<R> {

			abstract R calculate();

		}

		private static class RunnableCaseResult<R> extends CaseResult<R> {

			private final Function<Object, R> action;
			private final Object param;

			/**
			 * @param action
			 * @param param
			 */
			RunnableCaseResult(@NonNull Function<Object, R> action, @NonNull Object param) {
				Assert.notNull(action, "Action may not be null");
				Assert.notNull(param, "Param may not be null");
				this.action = action;
				this.param = param;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see de.naju.adebar.util.Functional.FunctionalMatch.CaseResult#calculate()
			 */
			@Override
			R calculate() {
				return action.apply(param);
			}

		}

		private static class StaticCaseResult<R> extends CaseResult<R> {

			private final R result;

			/**
			 * @param result
			 */
			public StaticCaseResult(R result) {
				this.result = result;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see de.naju.adebar.util.Functional.FunctionalMatch.CaseResult#calculate()
			 */
			@Override
			R calculate() {
				return result;
			}

		}

	}

	public static class MatchException extends RuntimeException {

		private static final long serialVersionUID = -3903882205980399140L;

		private final Class<?> actualClass;

		MatchException(Class<?> actualClass) {
			this.actualClass = actualClass;
		}

		MatchException(Class<?> actualClass, String message) {
			super(message);
			this.actualClass = actualClass;
		}

		/**
		 * @return the actualClass
		 */
		public Class<?> getActualClass() {
			return actualClass;
		}

	}


}
