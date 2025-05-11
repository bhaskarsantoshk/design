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
