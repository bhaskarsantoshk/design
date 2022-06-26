# Object-Oriented Basics

* Object-oriented programming (OOP) is a style of programming that focuses on using objects to design and build applications. 
* Contrary to procedure-oriented programming where programs are designed as blocks of statements to manipulate data, OOP organizes the program to combine data and functionality and wrap it inside something called an “Object”.

#### Objects: 
* Objects represent a real-world entity and the basic building block of OOP. For example, an Online Shopping System will have objects such as shopping cart, customer, product item, etc.
#### Class:
* Class is the prototype or blueprint of an object. It is a template definition of the attributes and methods of an object. For example, in the Online Shopping System, the Customer object will have attributes like shipping address, credit card, etc., and methods for placing an order, canceling an order, etc.

The four principles of object-oriented programming are encapsulation, abstraction, inheritance, and polymorphism.

* Encapsulation: Encapsulation is the mechanism of binding the data together and hiding it from the outside world. Encapsulation is achieved when each object keeps its state private so that other objects don’t have direct access to its state. Instead, they can access this state only through a set of public functions.

* Abstraction: Abstraction can be thought of as the natural extension of encapsulation. It means hiding all but the relevant data about an object in order to reduce the complexity of the system. In a large system, objects talk to each other, which makes it difficult to maintain a large code base; abstraction helps by hiding internal implementation details of objects and only revealing operations that are relevant to other objects.

* Inheritance: Inheritance is the mechanism of creating new classes from existing ones.

* Polymorphism: Polymorphism (from Greek, meaning “many forms”) is the ability of an object to take different forms and thus, depending upon the context, to respond to the same message in different ways. Take the example of a chess game; a chess piece can take many forms, like bishop, castle, or knight and all these pieces will respond differently to the ‘move’ message.

## OO Analysis and Design

The process of OO analysis and design can be described as:

* Identifying the objects in a system;
* Defining relationships between objects;
* Establishing the interface of each object; and,
* Making a design, which can be converted to executables using OO languages.

## UML

* UML stands for Unified Modeling Language and is used to model the Object-Oriented Analysis of a software system.
* Important UML Diagrams:
  * Use Case Diagram: Used to describe a set of user scenarios, this diagram, illustrates the functionality provided by the system. 
  * Class Diagram: Used to describe structure and behavior in the use cases, this diagram provides a conceptual model of the system in terms of entities and their relationships. 
  * Activity Diagram: Used to model the functional flow-of-control between two or more class objects. 
  * Sequence Diagram: Used to describe interactions among classes in terms of an exchange of messages over time.

### Use Case Diagram:

* Use case diagrams describe a set of actions (called use cases) that a system should or can perform in collaboration with one or more external users of the system (called actors). 
* Use Case Diagrams describe the high-level functional behavior of the system. 
* It answers what system does from the user point of view. 
* Use case answers ‘What will the system do?’ and at the same time tells us ‘What will the system NOT do?’.
* To illustrate a use case on a use case diagram, we draw an oval in the middle of the diagram and put the name of the use case in the center of the oval. To show an actor (indicating a system user) on a use-case diagram, we draw a stick figure to the left or right of the diagram.

The different components of the use case diagram are:

* System boundary: A system boundary defines the scope and limits of the system. It is shown as a rectangle that spans all use cases of the system.

* Actors: An actor is an entity who performs specific actions. These roles are the actual business roles of the users in a given system. An actor interacts with a use case of the system. For example, in a banking system, the customer is one of the actors.

* Use Case: Every business functionality is a potential use case. The use case should list the discrete business functionality specified in the problem statement.

* Include: Include relationship represents an invocation of one use case by another use case. From a coding perspective, it is like one function being called by another function.

* Extend: This relationship signifies that the extended use case will work exactly like the base use case, except that some new steps will be inserted in the extended use case.

### Class Diagram

* A class diagram describes the attributes and operations of a class and also the constraints imposed on the system. 
* Class diagrams are widely used in the modeling of object-oriented systems because they are the only UML diagrams that can be mapped directly to object-oriented languages.

#### Association: 
* If two classes in a model need to communicate with each other, there must be a link between them. This link can be represented by an association. Associations can be represented in a class diagram by a line between these classes with an arrow indicating the navigation direction.

#### Aggregation: 
* Aggregation is a special type of association used to model a “whole to its parts” relationship. In a basic aggregation relationship, the lifecycle of a PART class is independent of the WHOLE class’s lifecycle. In other words, aggregation implies a relationship where the child can exist independently of the parent. In the above diagram, Aircraft can exist without Airline.

#### Composition: 
* The composition aggregation relationship is just another form of the aggregation relationship, but the child class’s instance lifecycle is dependent on the parent class’s instance lifecycle. In other words, Composition implies a relationship where the child cannot exist independent of the parent. In the above example, WeeklySchedule is composed in Flight which means when Flight lifecycle ends, WeeklySchedule automatically gets destroyed.

#### Generalization: 
* Generalization is the mechanism for combining similar classes of objects into a single, more general class. Generalization identifies commonalities among a set of entities. In the above diagram, Crew, Pilot, and Admin, all are Person.

#### Dependency: 
* A dependency relationship is a relationship in which one class, the client, uses or depends on another class, the supplier. In the above diagram, FlightReservation depends on Payment.

### Sequence diagram

* A sequence diagram has two dimensions: The vertical dimension shows the sequence of messages in the chronological order that they occur; the horizontal dimension shows the object instances to which the messages are sent.
* Sequence diagrams show a detailed flow for a specific use case or even just part of a particular use case. They are almost self-explanatory; they show the calls between the different objects in their sequence and can explain, at a detailed level, different calls to various objects.

### Activity Diagrams

* We use Activity Diagrams to illustrate the flow of control in a system. An activity diagram shows the flow of control for a system functionality; it emphasizes the condition of flow and the sequence in which it happens. We can also use an activity diagram to refer to the steps involved in the execution of a use case.
  
#### What is the difference between Activity diagram and Sequence diagram?

* Activity diagram captures the process flow. It is used for functional modeling. A functional model represents the flow of values from external inputs, through operations and internal data stores, to external outputs.
* Sequence diagram tracks the interaction between the objects. It is used for dynamic modeling, which is represented by tracking states, transitions between states, and the events that trigger these transitions.