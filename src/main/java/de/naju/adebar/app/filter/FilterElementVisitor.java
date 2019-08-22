package de.naju.adebar.app.filter;

public interface FilterElementVisitor {

	void visit(ComparingFilter filter);

	void visit(ContainmentFilter filter);

	void visit(InvertableFilter filter);

	void visit(JoiningFilter filter);

	void visit(ListFilter filter);

	void visit(AbstractFilter filter);

}
