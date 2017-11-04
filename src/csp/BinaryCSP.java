package csp;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class BinaryCSP<D> {

  List<Variable<D>> vars;
  List<BinaryConstraint<D>> constraints;

  BinaryCSP() {
    vars = new LinkedList<>();
    constraints = new LinkedList<>();
  }

  // Make all constraints arc consistent
  private boolean ac3() {
    LinkedList<BinaryConstraint<D>> queue = new LinkedList<>(constraints);
    LinkedList<BinaryConstraint<D>> temp = new LinkedList<>();

    while (!queue.isEmpty()) {
      BinaryConstraint<D> constraint = queue.pop();

      if (revise(constraint)) {
        if (constraint.getX().getPossibleValues().isEmpty()) {
          return false;
        } else {
          queue.stream().filter(bc -> bc.concerns(constraint.getX()) &&
                  (bc.depends(constraint.getX()) != constraint.getY()))
                  .forEach(bc -> temp.add(new BinaryConstraint<>(bc.getY(), bc.getX())));

          temp.forEach(queue::push);
          temp.clear();
        }
      }
    }
    return true;
  }

  // Make the left part of a constraint arc consistent
  private boolean revise(BinaryConstraint<D> c) {
    return c.getX().getPossibleValues().removeIf(x -> !c.getY().satisfies(x) && !c.getX().isAssigned());
  }

  private boolean initRevise(BinaryConstraint<D> c) {
    if (c.getY().isAssigned() && c.getX().isAssigned()) return true;

    c.getY().getPossibleValues().removeIf(val -> !c.getX().satisfies(val) && c.getX().isAssigned());
    c.getX().getPossibleValues().removeIf(val -> !c.getY().satisfies(val) && c.getY().isAssigned());

    return (!c.getY().getPossibleValues().isEmpty() || !c.getX().getPossibleValues().isEmpty());
  }

  boolean forwardCheckAC3() {
    init();

    Optional<Variable<D>> variableOptional = this.vars.stream().filter(var -> !var.isAssigned()).findFirst();

    if (!variableOptional.isPresent()) {
      return true;
    } else {
      Variable<D> variable = variableOptional.get();

      List<D> values = new LinkedList<>(variable.getPossibleValues());

      for (D value : values) {

        if (this.constraints.stream().filter(bc -> bc.concerns(variable)).allMatch(bc ->
                bc.depends(variable).satisfies(value))) {

          variable.setValue(value);

          this.constraints.stream().filter(bc -> bc.concerns(variable))
                  .forEach(bc -> bc.depends(variable).removePossibleValue(value));

          if (!ac3()) {

            variable.reset();
            variable.resetPossibleValues(values);
            this.vars.stream().filter(x -> !x.isAssigned).forEach(Variable::reset);
            init();

          } else if (!this.forwardCheck()) {

            variable.reset();
            variable.resetPossibleValues(values);
            this.constraints.stream().filter(bc -> bc.concerns(variable) && !bc.depends(variable).isAssigned() &&
                    !bc.depends(variable).getPossibleValues().contains(value))
                    .forEach(bc -> bc.depends(variable).reset());
            init();
          }
        }

        variableOptional = this.vars.stream().filter(var -> !var.isAssigned()).findFirst();
        if (!variableOptional.isPresent()) return true;
      }
    }
    return false;
  }

  boolean forwardCheck() {
    init();

    Optional<Variable<D>> variableOptional = this.vars.stream().filter(var -> !var.isAssigned()).findFirst();

    if (!variableOptional.isPresent()) {
      return true;
    } else {
      Variable<D> variable = variableOptional.get();

      List<D> values = new LinkedList<>(variable.getPossibleValues());

      for (D value : values) {

        if (this.constraints.stream().filter(bc -> bc.concerns(variable))
                .allMatch(bc -> bc.depends(variable).satisfies(value))) {

          variable.setValue(value);

          this.constraints.stream().filter(bc -> bc.concerns(variable)).forEach(bc ->
                  bc.depends(variable).removePossibleValue(value));

          if (!this.forwardCheck()) {
            variable.reset();
            variable.resetPossibleValues(values);
            this.constraints.stream().filter(bc -> bc.concerns(variable) && !bc.depends(variable).isAssigned() &&
                    !bc.depends(variable).getPossibleValues().contains(value))
                    .forEach(bc -> bc.depends(variable).addPossibleValue(value));
          }
        }

        /*else {
          this.constraints.stream().filter(binaryConstraint ->
                  binaryConstraint.concerns(variable)).filter(dBinaryConstraint ->
                  !dBinaryConstraint.depends(variable).satisfies(value)).forEach(System.out::println);
        }*/

        variableOptional = this.vars.stream().filter(var -> !var.isAssigned()).findFirst();
        if (!variableOptional.isPresent()) return true;
      }
    }
    return false;
  }

  private void init() {
    vars.stream().filter(Variable::isAssigned).forEach(variable ->
            this.constraints.stream().filter(bc -> bc.concerns(variable)).forEach(this::initRevise));
  }
}
