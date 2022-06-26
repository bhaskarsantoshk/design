## Suggestions for Object Oriented Design
Whenever writing code in an object oriented language, 
sticking to the following list of suggestions will make your code amenable to changes with the least effort.

* Separate out parts of code that vary or change from those that remain the same. 
* Always code to an interface and not against a concrete implementation. 
* Encapsulate behaviors as much as possible. 
* Favor composition over inheritance. Inheritance can result in explosion of classes and also sometimes the base class is fitted with new functionality that isn't applicable to some of its derived classes. 
* Interacting components within a system should be as loosely coupled as possible. 
* Ideally, class design should inhibit modification and encourage extension. 
* Using patterns in your day to day work, allows exchanging entire implementation concepts with other developers via shared pattern vocabulary.

## Types of Design Patterns
* Creational 
* Structural 
* Behavioural

### Creational

Creational design patterns relate to how objects are constructed from classes. The creational design pattern come with powerful suggestions on how best to encapsulate the object creation process in a program.
  * Builder Pattern 
  * Prototype Pattern 
  * Singleton Pattern 
  * Abstract Factory Pattern

### Structural
Structural patterns are concerned with the composition of classes i.e. how the classes are made up or constructed. These include:
* Adapter Pattern 
* Bridge Pattern 
* Composite Pattern 
* Decorator Pattern 
* Facade Pattern 
* Flyweight Pattern 
* Proxy Pattern

### Behavioral
Behavioral design patterns dictate the interaction of classes and objects amongst each other and the delegation of responsibility. These include:
* Interpreter Pattern 
* Template Pattern 
* Chain of Responsibility Pattern 
* Command Pattern 
* Iterator Pattern 
* Mediator Pattern 
* Memento Pattern 
* Observer Pattern 
* State Pattern 
* Strategy Pattern 
* Visitor Pattern

### Important paterns to learn:

* All the creational design patterns, 
* decorator, 
* proxy, 
* iterator, 
* observer and 
* visitor patterns.

#### Builder Pattern

* Sometimes, the objects we create can be complex, made up of several sub-objects or require an elaborate construction process.
* The exercise of creating complex types can be simplified by using the builder pattern. 
* A composite or an aggregate object is what a builder generally builds.
* Formally, a builder pattern encapsulates or hides the process of building a complex object and separates the representation of the object and its construction. 
* The separation allows us to construct different representations using the same construction process.
* The class diagram consists of the following entities 
  * Builder 
  * Concrete Builder 
  * Director 
  * Product


* First we'll start with the abstract interface for our AircraftBuilder class.
* The builder contains a method for each component that can be part of the final product. 
* These methods are selectively overridden by concrete builders 
depending on if the builders will be including that part in 
the final product variant that they are responsible for building.

```java
public abstract class AircraftBuilder {

    public void buildEngine() {

    }

    public void buildWings() {

    }

    public void buildCockpit() {

    }

    public void buildBathrooms() {

    }

    abstract public IAircraft getResult();
}
```

* Now we'll implement two concrete builders, one for F-16 and one for Boeing-747.
```java
public class Boeing747Builder extends AircraftBuilder {

    Boeing747 boeing747;

    @Override
    public void buildCockpit() {

    }

    @Override
    public void buildEngine() {

    }

    @Override
    public void buildBathrooms() {
        
    }

    @Override
    public void buildWings() {

    }

    public IAircraft getResult() {
        return boeing747;
    }
}

public class F16Builder extends AircraftBuilder {

    F16 f16;

    @Override
    public void buildEngine() {
        // get F-16 an engine
        // f16.engine = new F16Engine();
    }

    @Override
    public void buildWings() {
        // get F-16 wings
        // f16.wings = new F16Wings();
    }

    @Override
    public void buildCockpit() {
        f16 = new F16();
        // get F-16 a cockpit
        // f16.cockpit = new F16Cockpit();
    }

    public IAircraft getResult() {
        return f16;
    }
}
```

* The process or algorithm required to construct the aircraft 
which in our case is the specific order 
in which the different parts are created is captured by 
another class called the Director. 
* The director is in a sense directing the construction of the aircraft. 
* The final product is still returned by the builders.

```java
public class Director {

    AircraftBuilder aircraftBuilder;

    public Director(AircraftBuilder aircraftBuilder) {
        this.aircraftBuilder = aircraftBuilder;
    }

    public void construct(boolean isPassenger) {
        aircraftBuilder.buildCockpit();
        aircraftBuilder.buildEngine();
        aircraftBuilder.buildWings();

        if (isPassenger)
            aircraftBuilder.buildBathrooms();
    }
}
```
* Notice how we can pass in the builder of our choice, 
and vary the aircraft product (representation) to be either an F-16 or a Boeing-747. 
* In our scenario, the builders return the same supertype 
however that may not be the case if the builders return products that aren't very similar.

```java
public class Client {

    public void main() {

        F16Builder f16Builder = new F16Builder();
        Director director = new Director(f16Builder);
        director.construct(false);

        IAircraft f16 = f16Builder.getResult();
    }
}
```

* The Jave api exposes a StringBuilder class that 
doesn't really conform to the strict reading of the GoF builder pattern
but can still be thought of as an example of it. 
* Using the StringBuilder instance we can successively create a string by using the append method.

* Another hypothetical example could be creating documents of type pdf or html. 
* Consider the snippet below:

```
public IDocument construct(DocumentBuilder documentBuilder) {

        documentBuilder.addTitle("Why use design patterns");
        documentBuilder.addBody("blah blah blah... more blah blah blah");
        documentBuilder.addAuthor("C. H. Afzal");
        documentBuilder.addConclusion("Happy Coding!");
        
        // Return the document and depending on the concrete
        // implementation of the DocumentBuilder, we could return
        // either a pdf or html document.
        return documentBuilder.buildDocument();
        
    }
```

### Singleton Pattern

* Singleton pattern as the name suggests is used to create one and only instance of a class. 
* There are several examples where only a single instance of a class should exist and the constraint be enforced. 
* Caches, thread pools, registries are examples of objects that should only have a single instance.
* Its trivial to new-up an object of a class but how do we ensure that only one object ever gets created?
* The answer is to make the constructor private of the class we intend to define as singleton. That way, only the members of the class can access the private constructor and no one else. 
* Formally the Singleton pattern is defined as ensuring that only a single instance of a class exists and a global point of access to it exists.

```java
public class AirForceOne {
    private static AirForceOne airForceOne;
    private AirForceOne(){
        
    }
    public static AirForceOne getInstance(){
        if ( airForceOne == null ){
            airForceOne = new AirForceOne();
        }
        return airForceOne;
    }
    
    public void fly() {
        
    }
}
```

```java
public class Client {
    public void main() {
        AirForceOne airforceOne = AirforceOne.getInstance();
        airforceOne.fly();
    }
}
```

#### Multithreading and Singleton

* The above code will work hunky dory as long as the application is single threaded. 
* As soon as multiple threads start using the class, 
* there's a potential that multiple objects get created. Here's one example scenario:
  * Thread A calls the method getInstance and finds the onlyInstance to be null
  but before it can actually new-up the instance it gets context switched out. 
  * Now thread B comes along and calls the getInstance method and goes on to new-up the instance 
  and returns the AirforceOne object. 
  * When thread A is scheduled again, is when the mischief begins. 
  The thread was already past the if null condition check and will proceed to new-up 
  another object of AirforceOne and assign it to onlyInstance. 
  * Now there are two different AirforceOne objects out in the wild, one with thread A and one with thread B.
* There are two trivial ways to fix this race condition.
  * adding synchronized to getInstance method:
  ``` synchronized public static AirforceOne getInstance() ```
  * The other is to undertake static initialization of the instance, which is guaranteed to be thread-safe.
  ```private static AirforceOne onlyInstance = new AirforceOne(); ```
* The problem with the above approaches is that synchronization is expensive 
and static initialization creates the object even if it's not used in a particular run of the application. 
* If the object creation is expensive then static intialization can cost us performance.

#### Double-Checked Locking

```java
public class AirforceOneWithDoubleCheckedLocking {

    // The sole instance of the class. Note its marked volatile
    private volatile static AirforceOneWithDoubleCheckedLocking onlyInstance;

    // Make the constructor private so its only accessible to
    // members of the class.
    private AirforceOneWithDoubleCheckedLocking() {
    }

    public void fly() {
        System.out.println("Airforce one is flying...");
    }

    // Create a static method for object creation
    synchronized public static AirforceOneWithDoubleCheckedLocking getInstance() {

        // Only instantiate the object when needed.
        if (onlyInstance == null) {
            // Note how we are synchronizing on the class object
            synchronized (AirforceOneWithDoubleCheckedLocking.class) {
                if (onlyInstance == null) {
                    onlyInstance = new AirforceOneWithDoubleCheckedLocking();
                }
            }
        }

        return onlyInstance;
    }
}
```

### Prototype Pattern
Prototype pattern involves creating new objects by copying existing objects. 
The object whose copies are made is called the prototype. 
You can think of the prototype object as the seed object from which other objects get created
but you might ask why would we want to create copies of objects, why not just create them anew?
The motivations for prototype objects are as follows:

Sometimes creating new objects is more expensive than copying existing objects.

Imagine a class will only be loaded at runtime and you can't access its constructor statically.
The run-time environment creates an instance of each dynamically loaded class automatically 
and registers it with a prototype manager. 
The application can request objects from the prototype manager which in turn can return clones of the prototype.

The number of classes in a system can be greatly reduced
by varying the values of a cloned object from a prototypical instance.

Formally, the pattern is defined as specify the kind of objects to create 
using a prototypical instance as a model and making copies of the prototype to create new objects.

* Class Diagram#
The class diagram consists of the following entities 
  * Prototype 
  * Concrete Prototype 
  * Client
* Let's take an example to better understand the prototype pattern. 
* We'll take up our aircraft example. We created a class to represent the F-16. 
* However, we also know that F-16 has a handful of variants. We can subclass the F16 class to represent each one of the variants
* but then we'll end up with several subclasses in our system. Furthermore,
* let's assume that the F16 variants only differ by their engine types. 
* Then one possibility could be, we retain only a single F16 class to represent
all the versions of the aircraft but we add a setter for the engine. 
* That way, we can create a single F16 object as a prototype,
clone it for the various versions and compose
the cloned jet objects with the right engine type to represent the corresponding variant of the aircraft.

```java
public interface IAircraftPrototype {

    void fly();

    IAircraftPrototype clone();

    void setEngine(F16Engine f16Engine);
}


public class F16 implements IAircraftPrototype {

    // default engine
    F16Engine f16Engine = new F16Engine();

    @Override
    public void fly() {
        System.out.println("F-16 flying...");
    }

    @Override
    public IAircraftPrototype clone() {
        // Deep clone self and return the product
        return new F16();
    }

    public void setEngine(F16Engine f16Engine) {
        this.f16Engine = f16Engine;
    }
}


public class Client {

    public void main() {

        IAircraftPrototype prototype = new F16();

        // Create F16-A
        IAircraftPrototype f16A = prototype.clone();
        f16A.setEngine(new F16AEngine());

        // Create F16-B
        IAircraftPrototype f16B = prototype.clone();
        f16B.setEngine(new F16BEngine());
    }
}
```


### Factory Pattern

* Usually, object creation in Java takes place like so:
``` SomeClass someClassObject = new SomeClass(); ```
* The problem with the above approach is that the code using the SomeClass's object,
suddenly now becomes dependent on the concrete implementation of SomeClass. 
* There's nothing wrong with using new to create object 
but it comes with the baggage of tightly coupling our code to the concrete implementation class,
which is a violation of code to an interface and not to an implementation.

* Formally, the factory method is defined as 
providing an interface for object creation 
but delegating the actual instantiation of objects to subclasses.

* Naive implementation:
```java
public class F16 {

    F16Engine engine;
    F16Cockpit cockpit;

    protected void makeF16() {
        engine = new F16Engine();
        cockpit = new F16Cockpit();
    }

    public void fly() {
        makeF16();
        System.out.println("F16 with bad design flying");
    }
}

public class Client {

    public void main() {

        // We instantiate from a concrete class, thus tying
        // ourselves to it
        F16 f16 = new F16();
        f16.fly();
    }
}
```

In the above code, we have committed ourselves to using a concrete implementation of the F16 class. 
What if the company comes up with newer versions of the aircraft and we are required to represent them in the program? 
That would make us change the client code where we new-up the F16 instance. 
One way out, is to encapsulate the object creation in another object that is solely responsible for new-ing up the requested variants of the F-16. 
For starters, let's say we want to represent the A and B variants of F16, then the code would look like:

```java
public class F16SimpleFactory {

    public F16 makeF16(String variant) {

        switch (variant) {
        case "A":
            return new F16A();
        case "B":
            return new F16B();
        default:
            return new F16();
        }
    }
}
```

The above is an example of a Simple Factory and isn't really a pattern but a common programming idiom. 
You could also mark the make method static to skip the factory object creation step.
However, since static methods can't be overridden in subclasses because they are unique to a class,
we won't be able to subclass the Static Factory. 
Remember simple and static factories aren't the same as the factory method pattern.

However, if we want to keep the creation of the F16 object parts within the same class and still be able to introduce new F16 variants as they come along, we could subclass F16 and delegate the creation of the right F16 variant object to the subclass handling that variant. This is exactly the factory method pattern! The method here is the makeF16() which we'll make behave like a factory that produces the appropriate F16 variants. 
Proceeding forward we introduce two subclasses like so

```java
public class F16 {

    IEngine engine;
    ICockpit cockpit;

    protected F16 makeF16() {
        engine = new F16Engine();
        cockpit = new F16Cockpit();
        return this;
    }

    public void taxi() {
        System.out.println("F16 is taxing on the runway !");
    }

    public void fly() {
        // Note here carefully, the superclass F16 doesn't know
        // what type of F-16 variant it was returned.
        F16 f16 = makeF16();
        f16.taxi();
        System.out.println("F16 is in the air !");
    }
}

public class F16A extends F16 {

    @Override
    public F16 makeF16() {
        super.makeF16();
        engine = new F16AEngine();
        return this;
    }
}

public class F16B extends F16 {

    @Override
    public F16 makeF16() {
        super.makeF16();
        engine = new F16BEngine();
        return this;
    }
}

```

A factory method may or may not provide a default or generic implementation 
but lets subclasses specialize or modify the product by overriding the create/make methods.
In our example the variant models only have a different engine but the same cockpit.
```
public class Client {
    public void main() {
        Collection<F16> myAirForce = new ArrayList<F16>();
        F16 f16A = new F16A();
        F16 f16B = new F16B();
        myAirForce.add(f16A);
        myAirForce.add(f16B);

        for (F16 f16 : myAirForce) {
            f16.fly();
        }
    }
}
```

### Decorator

* The decorator pattern can be thought of as a wrapper or more formally 
a way to enhance or extend the behavior of an object dynamically. 
* The pattern provides an alternative to subclassing when new functionality is desired.

```java

public interface IAircraft {

    float baseWeight = 100;

    void fly();

    void land();

    float getWeight();

}

public class Boeing747 implements IAircraft {

    @Override
    public void fly() {
        System.out.println("Boeing-747 flying ...");
    }

    @Override
    public void land() {
        System.out.println("Boeing-747 landing ...");
    }

    @Override
    public float getWeight() {
        return baseWeight;
    }
}


public abstract class BoeingDecorator implements IAircraft {

}


public class LuxuryFittings extends BoeingDecorator {

    IAircraft boeing;

    public LuxuryFittings(IAircraft boeing) {
        this.boeing = boeing;
    }

    @Override
    public void fly() {
        boeing.fly();
    }

    @Override
    public void land() {
        boeing.land();
    }

    @Override
    public float getWeight() {
        return (30.5f + boeing.getWeight());
    }
}

public class BulletProof extends BoeingDecorator {

    IAircraft boeing;

    public BulletProof(IAircraft boeing) {
        this.boeing = boeing;

    }

    @Override
    public void fly() {
        boeing.fly();
    }

    @Override
    public void land() {
        boeing.land();
    }

    @Override
    public float getWeight() {
        return 50f + boeing.getWeight();
    }
}

public class Client {

    public void main() {
        IAircraft simpleBoeing = new Boeing747();
        IAircraft luxuriousBoeing = new LuxuryFittings(simpleBoeing);
        IAircraft bulletProofBoeing = new BulletProof(luxuriousBoeing);
        float netWeight = bulletProofBoeing.getWeight();
        System.out.println("Final weight of the plane: " + netWeight);
    }
}

```

Other examples:

```
   public void main() {
        // FileInputStream is responsible for reading the file
        FileInputStream fileInputStream = new FileInputStream("myFile.txt");
        // BufferedInputStream extends FilterInputStream and not FileInputStream, it is
        // a decorator which enhances the functionality of the original object by wrapping over it.
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        // The read operation becomes buffered now
        bufferedInputStream.read();
    }
```

### Proxy Pattern 
* a mechanism to provide a surrogate or placeholder for another object to control access to it.
```java
public interface IDrone {

    void turnLeft();

    void turnRight();

    void firstMissile();
}


public class DroneProxy implements IDrone {

    @Override
    public void turnLeft() {
        // forward request to the real drone to
        // turn left over the internet
    }

    @Override
    public void turnRight() {
        // forward request to the real drone to
        // turn right over the internet
    }

    @Override
    public void firstMissile() {
        // forward request to the real drone to
        // fire missile
    }
}


public class Client {

    public void main(DroneProxy droneProxy) {

        // perpetual loop that received pilot actions
        while (true) {

            Scanner scanner = new Scanner(System.in);
            String action = scanner.nextLine();

            switch (action) {
                case "left": {
                    droneProxy.turnLeft();
                    break;
                }

                case "right": {
                    droneProxy.turnRight();
                    break;
                }

                case "fire": {
                    droneProxy.firstMissile();
                    break;
                }

                default:
                    System.out.println("Invalid Action");
            }
        }
    }
}

public class Drone implements IDrone {

    @Override
    public void turnLeft() {
        // receives the request and any method parameters
        // over the internet to turn the drone to its left.
    }

    @Override
    public void turnRight() {
        // receives the request and any method parameters
        // over the internet to turn the drone to its right.
    }

    @Override
    public void firstMissile() {
        // receives the request and any method parameters
        // over the internet to fire a missile
    }
}
```

### Iterator Pattern
* a pattern that allows traversing the elements of an aggregate or a collection sequentially 
without exposing the underlying implementation.
```
ArrayList<String> companiesIWantToInterviewFor = new ArrayList<>();
        companiesIWantToInterviewFor.add("SnapChat");
        companiesIWantToInterviewFor.add("Twitter");
        companiesIWantToInterviewFor.add("Tesla");

        Iterator<String> it = companiesIWantToInterviewFor.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
```

### Visitor Pattern
* defining operations to be performed on elements
of an object structure without changing the classes of the elements it works on.

