package util.rules.nonpayload;

import util.rules.Operators;

public class Icode {
    public int operation;
    public int max = 255;
    public int min = 0;

    public void setOperation(String operation) {
        this.operation = Operators.OPERATORS.get(operation);
    }

    public void parse(String option){
        max = Integer.parseInt(option.replaceAll("[^\\d]", ""));
        String op = option.replaceAll("[\\d]", "");
        if(!op.isEmpty())
            this.operation = Operators.OPERATORS.get(op);
    }
}

