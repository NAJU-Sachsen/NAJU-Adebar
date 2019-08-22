package de.naju.adebar.web.model.persons.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.lang.Nullable;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.web.model.html.ElementId;

public class FilterGroup {

	abstract static class FilterGroupBuilder {

		FilterGroupBuilder delegate;

		FilterGroupBuilder(@Nullable FilterGroupBuilder delegate) {
			this.delegate = delegate;
		}

		ElementId getFilterGroupId() {
			Supplier<ElementId> idSupplier = delegate == null //
					? () -> {
				throw new IllegalStateException();
			}
					: () -> delegate.getFilterGroupId();

			return fetchFilterGroupId().orElseGet(idSupplier);
		}

		ElementId getTogglerId() {
			Supplier<ElementId> idSupplier = delegate == null //
					? () -> {
				throw new IllegalStateException();
			}
					: () -> delegate.getTogglerId();

			return fetchTogglerId().orElseGet(idSupplier);
		}

		String getName() {
			Supplier<String> idSupplier = delegate == null //
					? () -> {
				throw new IllegalStateException();
			}
					: () -> delegate.getName();

			return fetchName().orElseGet(idSupplier);
		}

		List<AbstractFilter> getContents() {
			Supplier<List<AbstractFilter>> idSupplier = delegate == null //
					? () -> {
				throw new IllegalStateException();
			}
					: () -> delegate.getContents();

			return fetchContents().orElseGet(idSupplier);
		}

		Optional<ElementId> fetchFilterGroupId() {
			return Optional.empty();
		}

		Optional<ElementId> fetchTogglerId() {
			return Optional.empty();
		}

		Optional<String> fetchName() {
			return Optional.empty();
		}

		Optional<List<AbstractFilter>> fetchContents() {
			return Optional.empty();
		}
	}


	static class TogglerExpectingFilterGroupBuilder extends FilterGroupBuilder {

		private final ElementId filterGroupId;
		private ElementId togglerId;

		TogglerExpectingFilterGroupBuilder(ElementId filterGroupId) {
			super(null);
			this.filterGroupId = filterGroupId;
		}

		public NameExpectingFilterGroupBuilder withoutToggler() {
			return null;
		}

		NameExpectingFilterGroupBuilder toggledBy(ElementId togglerId) {
			this.togglerId = togglerId;
			return new NameExpectingFilterGroupBuilder(this);
		}

		@Override
		Optional<ElementId> fetchFilterGroupId() {
			return Optional.of(filterGroupId);
		}

		@Override
		Optional<ElementId> fetchTogglerId() {
			return Optional.of(togglerId);
		}

	}

	static class NameExpectingFilterGroupBuilder extends FilterGroupBuilder {

		private String name;

		public NameExpectingFilterGroupBuilder(FilterGroupBuilder delegate) {
			super(delegate);
		}

		public ContentExpectingFilterGroupBuilder called(String name) {
			this.name = name;
			return new ContentExpectingFilterGroupBuilder(this);
		}

		@Override
		Optional<String> fetchName() {
			return Optional.of(name);
		}

	}


	static class ContentExpectingFilterGroupBuilder extends FilterGroupBuilder {

		private List<AbstractFilter> contents;

		ContentExpectingFilterGroupBuilder(FilterGroupBuilder delegate) {
			super(delegate);
		}

		FilterGroup withContents(AbstractFilter... filters) {
			this.contents = Arrays.asList(filters);
			return new FilterGroup(getFilterGroupId(), getTogglerId(), getName(), getContents());
		}

		@Override
		Optional<List<AbstractFilter>> fetchContents() {
			return Optional.of(contents);
		}

	}

	public static TogglerExpectingFilterGroupBuilder with(ElementId filterGroupId) {
		return new TogglerExpectingFilterGroupBuilder(filterGroupId);
	}

	private final ElementId groupId;
	private final ElementId togglerId;
	private final String name;
	private final List<AbstractFilter> content;

	public FilterGroup(ElementId groupId, ElementId togglerId, String name,
			List<AbstractFilter> content) {
		this.groupId = groupId;
		this.togglerId = togglerId;
		this.name = name;
		this.content = content;
	}

	public ElementId getGroupId() {
		return groupId;
	}

	public ElementId getTogglerId() {
		return togglerId;
	}

	public String getName() {
		return name;
	}

	public List<AbstractFilter> getContent() {
		return new ArrayList<>(content);
	}

	public void enhanceAllFiltersWith(FieldEnhancementService enhancementService) {
		content.forEach(filter -> filter.accept(enhancementService));

	}

	@Override
	public String toString() {
		return name;
	}

}
