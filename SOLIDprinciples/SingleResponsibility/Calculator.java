package SOLIDprinciples.SingleResponsibility;

public class Calculator {
    public AdditionResult add(Number one, Number two){
        AdditionResult res = null;
        if (one.isInteger() && two.isInteger()){
            // add logic
        }
        else if ( one.isDecimal() && two.isDecimal() ){
            // add logic
        }
        return res;
    }

    public SubtractResult subtract(Number one, Number two){
        SubtractResult res = null;
        // add logic
        return res;
    }
}

/*
The above class will require changes when

You need to change to add method.
You need to change the subtract method
You need to add new kind of addition such as Complex Numbers
You need to add more operations such as divide or multiply
and more ...

 */
