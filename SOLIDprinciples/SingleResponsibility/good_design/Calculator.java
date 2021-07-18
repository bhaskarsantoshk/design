package SOLIDprinciples.SingleResponsibility.good_design;

public class Calculator {
    public CalculatorResult computeBinaryExecution ( Number one,
                                                     Number two,
                                                     OperationType operationType){
        return operationType.operate(one, two);
    }
}

/*
Calculator class acts as an interace for clients where they can supply input and the operation they want to execute.
The above class is not doing any number validation, type checking or performing addition, subtraction etc.. It has only one reason to change which is handling delegation for a class of expression.
i.e. This class changes only when you want to add a new kind of expression evaluation.
Even If you add a ternary expression evaluation, the responsibility remains same as this won't increase the number of reasons for the class to change.
 */
