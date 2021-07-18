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

/*
* This class is SRP compliant, don't confuse number of responsibilities with number of dependencies the class have to compute it's result
* This class has a single responsibility i.e responsibility to orchestrate.
* Again. don't add too many orchestrations that make a class God class. Keep your class size in check
* Class can be an orchestrator, delegator or action taker.
 */
