package csp;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class BinaryCSP<D> {

	List<Variable<D>> vars;
	List<BinaryConstraint<D>> constraints;

	public BinaryCSP() {
		vars = new LinkedList<Variable<D>>();
		constraints = new LinkedList<BinaryConstraint<D>>();
	}

	public boolean ac3() {
		while (!constraints.isEmpty()) {
			BinaryConstraint<D> constraint = constraints.get(0);
			if (revise(constraint)) {
				if (constraint.getX().getPossibleValues().isEmpty()) {
					return false;
				} else {
					this.constraints.stream().filter(bc ->
									bc.concerns(constraint.getX()) &&
													!bc.depends(constraint.getX()).isAssigned() &&
													(bc.depends(constraint.getX()) != constraint.getY())
					).forEach(this.constraints::add);
				}
			}
		}
		return true;
	}

	// Make a constraint arc consistent
	public boolean revise(BinaryConstraint<D> c) {
		// modifier uniquement la partie non assignée
		return c.getY().getPossibleValues().stream().anyMatch(value ->
						c.getX().removePossibleValue(value) && !c.getX().isAssigned());

		//c.getY().getPossibleValues().forEach(value -> c.getX().removePossibleValue(value));
		// c.getX().getPossibleValues().forEach(value -> c.getY().removePossibleValue(value));
		//System.out.println("c = " + c);
		//return true;
	}

	public boolean initRevise(BinaryConstraint<D> c) {
		//System.out.println("c = " + c);
		if (c.getY().isAssigned() && c.getX().isAssigned()) {
			return true;
		} else if (c.getX().isAssigned()) {
			//System.out.println("cx = " + c);
			c.getX().getPossibleValues().forEach(value -> {
				c.getY().removePossibleValue(value);
				//System.out.println("c1 = " + c);
			});
			return (!c.getY().getPossibleValues().isEmpty());
		} else if (c.getY().isAssigned()) {
			c.getY().getPossibleValues().forEach(value -> {
				c.getX().removePossibleValue(value);
				//System.out.println("c2 = " + c);
			});
			return (!c.getX().getPossibleValues().isEmpty());
		}
		return true;
	}

	public boolean forwardCheckAC3() {
		System.out.println("\nFORWARD CHECK");
		init();

		// first unaffected variable
		Optional<Variable<D>> variableOptional =
						this.vars.stream().filter(dVariable ->
										!dVariable.isAssigned()).findFirst();

		if (!variableOptional.isPresent()) {
			return true;
		} else {
			Variable<D> variable = variableOptional.get();

			System.out.println("unassigned variable = " + variable);

			// possible values list
			List<D> values = new LinkedList<>(variable.getPossibleValues());

			System.out.println("values = " + values);

			// try each value
			for (D value : values) {
				System.out.println("value = " + value);

				// constraints concerning variable
				Stream<BinaryConstraint<D>> constraintStream = this.constraints.stream()
								.filter(binaryConstraint ->
												binaryConstraint.concerns(variable));

				// value satisfies all constraints
				if (this.constraints.stream()
								.filter(binaryConstraint ->
												binaryConstraint.concerns(variable)).allMatch(dBinaryConstraint ->
												dBinaryConstraint.depends(variable).satisfies(value))) {

					variable.setValue(value);

					System.out.println(variable.getName() + " takes the value of " + value);

					// arc consistency check (revise ?)
					/*constraintStream.forEach(dBinaryConstraint ->
									dBinaryConstraint.depends(variable).removePossibleValue(value));
*/

					ac3();

					// doesn't work for all possible values
					if (!this.forwardCheck()) {
						variable.reset();
						variable.resetPossibleValues(values);

						// constraints reset
						this.constraints.stream().filter(bc ->
										bc.concerns(variable) && !bc.depends(variable).isAssigned() &&
														!bc.depends(variable).getPossibleValues().contains(value))
										.forEach(bc -> bc.depends(variable).addPossibleValue(value));
					}
				} else {
					this.constraints.stream().filter(binaryConstraint ->
									binaryConstraint.concerns(variable)).filter(dBinaryConstraint ->
									!dBinaryConstraint.depends(variable).satisfies(value)).forEach(System.out::println);
				}

				variableOptional = this.vars.stream().filter(dVariable ->
								!dVariable.isAssigned()).findFirst();

				// finish
				if (!variableOptional.isPresent()) return true;
			}
		}
		return false;
	}


	/*
	si l’affectation a est complète alors retourne a
	var ← variable suivante non affectée
	Pour toutes valeurs val ∈ D(var)
		si {var=val} ne viole aucune contrainte
			a = a ∪ {var=val}
			pour toutes variables v connectée à var
				verifier arc-cohérence de var et v
				result ← Backtrack(net, a)
				si result ̸= échec
					retourne result
				sinon retourne échec
	 */
	public boolean forwardCheck() {
		System.out.println("\nFORWARD CHECK");
		init();

		// first unaffected variable
		Optional<Variable<D>> variableOptional =
						this.vars.stream().filter(dVariable ->
										!dVariable.isAssigned()).findFirst();

		if (!variableOptional.isPresent()) {
			return true;
		} else {
			Variable<D> variable = variableOptional.get();

			System.out.println("unassigned variable = " + variable);

			// possible values list
			List<D> values = new LinkedList<>(variable.getPossibleValues());

			System.out.println("values = " + values);

			// try each value
			for (D value : values) {
				System.out.println("value = " + value);

				// constraints concerning variable
				Stream<BinaryConstraint<D>> constraintStream = this.constraints.stream()
								.filter(binaryConstraint ->
												binaryConstraint.concerns(variable));

				// value satisfies all constraints
				if (this.constraints.stream()
								.filter(binaryConstraint ->
												binaryConstraint.concerns(variable)).allMatch(dBinaryConstraint ->
												dBinaryConstraint.depends(variable).satisfies(value))) {

					variable.setValue(value);

					System.out.println(variable.getName() + " takes the value of " + value);

					// arc consistency check (revise ?)
					constraintStream.forEach(dBinaryConstraint ->
									dBinaryConstraint.depends(variable).removePossibleValue(value));

					// doesn't work for all possible values
					if (!this.forwardCheck()) {
						variable.reset();
						variable.resetPossibleValues(values);

						// constraints reset
						this.constraints.stream().filter(bc ->
										bc.concerns(variable) && !bc.depends(variable).isAssigned() &&
														!bc.depends(variable).getPossibleValues().contains(value))
										.forEach(bc -> bc.depends(variable).addPossibleValue(value));
					}
				} else {
					this.constraints.stream().filter(binaryConstraint ->
									binaryConstraint.concerns(variable)).filter(dBinaryConstraint ->
									!dBinaryConstraint.depends(variable).satisfies(value)).forEach(System.out::println);
				}

				variableOptional = this.vars.stream().filter(dVariable ->
								!dVariable.isAssigned()).findFirst();

				// finish
				if (!variableOptional.isPresent()) return true;
			}
		}
		return false;
	}

	private void init() {
		vars.stream().filter(Variable::isAssigned).forEach(variable ->
						this.constraints.stream()
										.filter(dBinaryConstraint -> dBinaryConstraint.concerns(variable))
										.forEach(this::initRevise));
	}
}
