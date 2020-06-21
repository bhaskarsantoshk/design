# SOLID PRINCIPLES

### Introduction  ( [source](https://www.youtube.com/watch?v=7wd-p20Fkbs) )
> Problems in Existing Software Development Process ( or What happens if we don't align our code with respect to these principles ) :   
* Software starts to rot
* Things go wrong
* As time goes by, it becomes difficult to maintain the software
* Eventually, it takes enarmous efforts to add small changes to the software

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
* I - Interface Segragation Principle
* D - Dependency Inversion Principle


### Liskov Substitution Principle | The L in the SOLID Principles
* Definition: Subtypes must be substitutable with their base types    
> Inditcations showing LSP violation:     
* A derives class that does less than it's badse class is not substitutable for that base class, and therefore violates LSP.
