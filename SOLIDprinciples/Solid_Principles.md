# SOLID PRINCIPLES
References: 
1. [CJ's youtube channel](https://www.youtube.com/channel/UCUS5-hVsSPXuWPEHRvnXXEg)

### Introduction  ( [source](https://www.youtube.com/watch?v=7wd-p20Fkbs) )
> Problems in Existing Software Development Process ( or What happens if we don't align our code with respect to these principles ) :   
* Software starts to rot
* Things go wrong
* As time goes by, it becomes difficult to maintain the software
* Eventually, it takes enormous efforts to add small changes to the software

> Design smells that start to degrade the quality of code written over time:    
* Rigidity : System is hard to change, because every change forces many other changes to other parts of the system.
* Fragility : Changes cause the system to break in places that have no conceptual relationship to the part that was changed
* Immobility : Hard to refactor components which could be reused.
* Needless Complexity
* Code is difficult to understand


### The Solid Principles    
* S - Single Responsibility Principle
* O - Open Closed Principle
* L - Liskov Substitution Principle
* I - Interface Segregation Principle
* D - Dependency Inversion Principle


### Single Responsibility Principle |  The S in SOLID Principles ( [source](https://www.youtube.com/watch?v=Y4L28C84CeA&t=27s) )
* Definition: A class should have only one responsibility. 
* Responsibility: It's defined as a reason to change. 
* Single Responsibility is the most powerful principle. To understand it well, you would need to know what are the reasons for which a class can change or how to find the number of reasons in a class due to which it can change. 
* Change : alteration in behaviour with respect to class, and the methods represent the behaviour of the class. This doesn't mean that the number of methods in a class is equal to number of reasons for which the class can change. 
* A change can be big, small and breaking change. 

> Example: A Web Calculations Engine     
>   * takes two numbers and perform add, subtract, mutiply and divide 
> This is version 1 and you want to add new features (support for decimal numbers) . Will the new feature break the old ones or work perfectly fine ?

```Java

class Calculator {
  
  public AdditionResult add ( Number one, Number two ) {
    if (one.isIteger() && two.isInteger()){
    // add logic
    }
    else if (one.isDecimal() && two.isDecimal()){
    // add logic
    }
    else if (one.isBigInteger() && two.isBigInteger()){
    // add logic
    }
    else{
    // add logic
    }
  }

  public SubtractResult subtract (Number one, Number two ){
    return (SubtractResult) this.add(one, two.negate()) ;
  }
 
}
```

>The above class will require changes when    
>   * You need to change to add method.   
>   * You need to change the subtract method    
>   * You need to add new kind of addition such as Complex Numbers    
>   * You need to add more operations such as divide or multiply    
>   * and more ...     

* Thus, the above class does not follow Single Responsibility Principle because it has already 5 reasons to change. 

* A SRP compliant class looks as below:

```Java
class Calculator{
    public CalculationResult computeBinaryOperation(
            Number one,
            Number two,
            OperationType operationType
            ) {
        return operationType.operate(one, two);
    }

    public CalculationResult computeUnaryOperation (
            Number one,
            OperationType operationType){
        return operationType.operate(one);
    }
}
```
>   * Calculator class acts as an interace for clients where they can supply input and the operation they want to execute.   

>   * The above class is not doing any number validation, type checking or performing addition, subtraction etc.. It has only one reason to change which is handling delegation for a class of expression.        

>   * i.e. This class changes only when you want to add a new kind of expression evaluation.

>   * Even If you add a ternary expression evaluation, the responsibility remains same as this won't increase the number of reasons for the class to change.   

* How to decide If a class is SRP Compliant :  
* It is not, if you observe the following in your class
>   * really Large classes and realy large methods
>   * If method has too many parameters
>   * Writing Unit test for a class or method is very difficult. 

* What to do to make it compliant :   

>   * Your ability to name classes in a beautiful way ( requires nice vocabulary). 
>   * Your ability to group similar kind of behaviour together. 
>   * Try solving the above mentioned problems.   

```Java
package SOLIDprinciples.SingleResponsibility.good_design;

public class MyResponsibleClass {
    private DependencyOne dependencyOne;
    private DependencyTwo dependencyTwo;
    private DependencyThree dependencyThree;

    public void myResponsibleMethod (Argument argument){
        ResultOne resultFromFirstDependency =
                dependencyOne.callingItsResponsibleMethodFor(argument);
        ResultTwo resultFromSecondDependency =
                dependencyTwo.callingItsResponsibleMethodFor(resultFromFirstDependency);
        ResultThree resultFromThirdDependency =
                dependencyThree.callingItsResponsibleMethodFor(resultFromSecondDependency);
        System.out.println(resultFromThirdDependency);
    }
}
```

>   * This class is SRP compliant, don't confuse number of responsibilities with number of dependencies the class have to compute it's result
>   * This class has a single responsibility i.e responsibility to orchestrate.
>   * Again. don't add too many orchestrations that make a class God class. Keep your class size in check

* Common responsibilities of a Class :
>   * An action taker
>   * A delegator (kind of a proxy to another class)
>   * An orchestrator

* What it doesn't depend on ? Number of methods in your class doesn't really tell number of reasons to change.   
For example:

```Java
// the class has SRP violation due to two responsibilities 
class FileUploader {
    public void uploadFile (File file, String url){}
    public void downloadFile (String url){}
}
```

```Java
// Just a name change for the class 
// and the methods under one responsibility group
class FileNetworkIOGateway {
    public void uploadFile (File file, String url){}
    public void downloadFile (String url){}
}
```

Advantages of SRP:

* Pluggable architecture
* Class is less fragile, less coupled, easily tested and debugged. 

### Open Closed Principle | The O in the SOLID Principles ( [source](https://www.youtube.com/watch?v=hRD8VX9AQck&list=PLn05u4nMKcB9yMCSCPzHiw-8rmVL4oFy_&index=3) )
* Definition: A class should be open for extension and closed for modification
* What does Open for extension mean ?
    * As required change, a module could be extended to satisfy those changes.
    * This means you model your features in such a way which requires you define a clear contract and you stick to it.
    * This requires a lot of thought before implementation. Once the contract is clear, this is exposed by interface or an abstract class and not the concrete implementation. 
    * It allows you to provide a different implementation for the interface- which allows you to dynamically change functionality at runtime.
     
* What does Closed for modification mean ?
    *  Any existing code should not be modified.
    * Concrete class need not be modified, you can simply add a new  class. 
    * Only in scenarios where you want to add a similar kind of feature
    * Scenarios where you need to add more methods - you're breaking all the contracts that you made initially - but if all the methods are with existing codebase,
    this will not be a problem. 
    * Problem arises when you update your contract, but the classes you've implemented before is outside the code base.
    * Java has a feature to add new methods to interface and still keep older classes from breaking . This is done by providing default methods to the interafce.
    * This makes Interface to be backward compatible.
    * As long as you're not breaking the existing contract - Open Closed principle is not violated.
     

### Liskov Substitution Principle | The L in the SOLID Principles ( [source](https://www.youtube.com/watch?v=4pt_l5U3PP0) )
* Definition: Subtypes must be substitutable with their base types    
* Indications showing LSP violation:     
> A derives class that does less than it's base class is not substitutable for that base class, and therefore violates LSP.

### Interface Segregation Principle | The I in the SOLID Principles ( [source](https://www.youtube.com/watch?v=fywebw8sjJc) )
* Definition: Client should not be forced to depend on the method they do not use.. 

* Ever faced this scenario while coding?
```Java
interface LivingThing{
  void breathe();
  void walk();
}


class Human implements Livingthing{
  void breathe(){
    System.out.println("Human breathes");
  }
  void walk(){
  System.out.println("Human walks");
  }
}

class Tree implements LivingThing{
void breathe(){
    System.out.println("Tree breathes");
  }
  
  // ISP violation 
  void walk(){
    // Intentionally left blank as Trees can't walk
  }
}
```

### Dependency Inversion Principle | The D in the SOLID Principle ( [source](https://www.youtube.com/watch?v=uEB_y01JMh4) )
* Definition:   
    > High level modules should not depend on low level modules, both should depend on abstractions
    > Abstractions should not depend on details, Details should depend on abstraction. 
* In Software engineering, we usually split the application architecture in terms of layers. Example: The UI Layer, The Controller Layer or The DAO layer and so on. 
