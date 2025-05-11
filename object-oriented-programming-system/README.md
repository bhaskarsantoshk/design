# Object-Oriented Programming (OOP)

## What is Object-Oriented Programming?

- A programming paradigm based on the concept of **Objects**
- **Object** = `{ variables (data) + functions (methods) }`

### Example:
```java
class Abc {
    int a;
    int b;

    int sum() {
        return a + b;
    }
}
```

---

## Procedural vs Object-Oriented Programming

| Aspect          | Procedural Programming              | Object-Oriented Programming             |
|----------------|-------------------------------------|-----------------------------------------|
| Focus           | Functions and sequence of actions   | Objects and their interactions          |
| Data Access     | Data is exposed (global/shared)     | Data can be hidden (encapsulation)      |
| Reusability     | Low                                 | High (inheritance, polymorphism)        |
| Extendibility   | Hard to extend                      | Easy to extend                          |

---

## Why Use OOP?

- ✅ **Modular** – code is divided into logical parts
- ✅ **Reusable** – code can be reused across applications
- ✅ **Scalable** – easier to manage and grow
- ✅ **Secure** – encapsulation protects data

---

## Real-life Analogy of OOP

- **Car as an Object**:
    - Attributes: color, brand, model, engine
    - Methods: drive(), brake(), honk()
    - You interact with the car (object) using functions (methods) without knowing the internal engine (implementation)

---

## Why is OOP Better for Large-Scale Applications?

- 🔁 **Scalability** – Easy to manage and evolve
- 🔄 **Reusability** – Common functionality can be reused
- 🔐 **Security** – Sensitive data can be protected via encapsulation
- 🔧 **Maintainability** – Isolated changes with minimal side-effects

---
## Class

- A **blueprint** or logical representation of an object
- Example: `Employee` class with attributes `name`, `salary`, and methods `setName()`, `getSalary()` etc.

```java
class Employee {
    String name;
    int salary;

    void setDetails(String n, int s) {
        name = n;
        salary = s;
    }

    void printDetails() {
        System.out.println("Name: " + name + ", Salary: " + salary);
    }
}
```

---

## Object

- Creates an **instance** of a class
- Two different `Employee` objects with different data:

```java
public class Main {
    public static void main(String[] args) {
        Employee emp1 = new Employee();
        emp1.setDetails("Alice", 50000);

        Employee emp2 = new Employee();
        emp2.setDetails("Bob", 60000);

        emp1.printDetails();
        emp2.printDetails();
    }
}
```

---

## Attributes and Behaviours

- **Attributes** → Properties (e.g., name, age, salary)
- **Behaviours** → Actions (e.g., walk(), work(), eat())

---

## Object Creation and Destruction

- Objects are stored in **Heap Memory** (large, system-managed)
- References (like `employee1`) are stored in **Stack Memory** (limited, per-thread)

### Example:
```java
Employee employee1 = new Employee();
```
- `employee1` reference stored in **stack**
- Actual `Employee` object created in **heap**

### Destruction:
- Java uses **Garbage Collector** to destroy unreachable heap objects
- When no reference points to an object, it's eligible for garbage collection

---

## Attributes and Methods

- **Attributes** – variables that store object data
- **Methods** – functions that define object behavior or business logic

### Example:
```java
class BankAccount {
    private String name;
    private double balance; // private = access specifier

    // Constructor
    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    // Default Constructor
    public BankAccount() {
    }

    // Setter (write access)
    public void setName(String name) {
        this.name = name;
    }

    // Getter (read access)
    public String getName() {
        return this.name;
    }

    // Getter only (no setter for security)
    public double getBalance() {
        return this.balance;
    }

    // Add balance
    public void addBalance(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("Amount is negative");
        }
    }

    // Withdraw
    public boolean canWithdraw(double amount) {
        if (amount < balance) {
            balance -= amount;
            return true;
        }
        System.out.println("Balance too low");
        return false;
    }
}
```

### Notes:
- Sensitive data is **kept private**
- Public **getters and setters** are exposed
- This is called **Encapsulation** – hiding internal data

---

# Constructors in Java

## What is a Constructor?
- A **constructor** is a special method that has the **same name as the class**
- It is used to **create and initialize objects**
- Java provides a **default constructor** if no constructor is explicitly defined

### Key Features:
- Has **no return type** (not even `void`)
- Has the **same name as the class**
- Can be **overloaded**
- Java automatically provides a **default constructor** if none is specified

---

## Purpose of Constructors
- Create objects
- Set default or initial values
- Reuse initialization logic

---

## Types of Constructors

### 1. Non-Parameterized Constructor (Default Constructor)
```java
class Account {
    String name;
    double balance;

    // Default constructor
    public Account() {
        name = "Unknown";
        balance = 0.0;
    }
}
```

---

### 2. Parameterized Constructor
```java
class Account {
    String name;
    double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }
}

// Usage
Account obj1 = new Account("John", 1000.00);
System.out.println(obj1.getName());    // John
System.out.println(obj1.getBalance()); // 1000.00

Account obj2 = new Account();
System.out.println(obj2.getName());    // null
System.out.println(obj2.getBalance()); // 0.0
```

---

### Constructor Overloading
```java
class Account {
    String name;
    double balance;

    public Account() {
        this.name = "Default";
        this.balance = 0.0;
    }

    public Account(String name) {
        this.name = name;
        this.balance = 0.0;
    }

    public Account(double balance) {
        this.name = "Unknown";
        this.balance = balance;
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
}
```

---

### 3. Copy Constructor
```java
class Account {
    String name;
    double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    // Copy Constructor
    public Account(Account account) {
        this(account.getName(), account.getBalance());
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return this.balance;
    }
}

// Usage
Account obj1 = new Account("Jane", 1500.00);
Account obj2 = new Account(obj1);
```

---

## Notes
- A constructor can **call another constructor** using `this(...)`
- Constructors allow **initializing objects in multiple ways**
