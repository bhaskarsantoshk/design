# Low-Level Design (LLD) for Battleship

## Overview of Battleship
* A Grid (Board) where players place their ships. 
* Ships of different sizes. 
* Players, each with a board. 
* Attacks where players take turns guessing coordinates. 
* Game Logic to determine hits, misses, and win conditions.

## Class Diagram
```text
+----------------------+
|      BattleshipGame  |
|----------------------|
| - players: Player[]  |
| - boardSize: int     |
| - currentPlayer: int |
| + startGame()        |
| + attack(x, y)       |
| + isGameOver()       |
+----------------------+
        |
        v
+----------------------+
|       Player        |
|----------------------|
| - name: String      |
| - board: Board      |
| + placeShip()       |
| + takeAttack()      |
+----------------------+
        |
        v
+----------------------+
|       Board         |
|----------------------|
| - grid: Square[][]  |
| - ships: Ship[]     |
| + placeShip()       |
| + receiveAttack()   |
| + isAllShipsSunk()  |
+----------------------+
        |
        v
+----------------------+
|       Square        |
|----------------------|
| - x: int            |
| - y: int            |
| - hasShip: boolean  |
| - isHit: boolean    |
| + markHit()         |
+----------------------+

+----------------------+
|        Ship         |
|----------------------|
| - size: int         |
| - isSunk: boolean   |
| - positions: Square[] |
| + hit()             |
| + isDestroyed()     |
+----------------------+
```

## How SOLID Principles Are Applied

Single Responsibility Principle (SRP)
* Board handles grid management. 
* Ship manages ship properties. 
* Player manages actions. 
* BattleshipGame handles turn logic and overall game management.

Open/Closed Principle (OCP)
* The Ship class can be extended for special types of ships (e.g., Carrier, Destroyer).
* AttackStrategy can be introduced for AI or different attack rules.

Liskov Substitution Principle (LSP)

* Any derived class of Ship (e.g., Submarine, Battleship) can replace Ship without affecting the board or game logic.

Interface Segregation Principle (ISP)

* Instead of one large class, multiple smaller classes (Board, Ship, Square, Player) keep responsibilities separate.

Dependency Inversion Principle (DIP)

* BattleshipGame depends on Player and Board via abstraction, allowing flexibility in rules, AI opponents, etc.

## Where Design Patterns Are Used

* Factory Pattern (For Creating Ships)

```Java
class ShipFactory {
    public static Ship createShip(String type, int size) {
        switch (type) {
            case "Battleship": return new Battleship(size);
            case "Destroyer": return new Destroyer(size);
            case "Carrier": return new Carrier(size);
            default: throw new IllegalArgumentException("Invalid ship type");
        }
    }
}
```

*  Strategy Pattern (For Attacking)

```Java
interface AttackStrategy {
    void attack(Board board, int x, int y);
}

class RandomAttack implements AttackStrategy {
    public void attack(Board board, int x, int y) {
        // Generate random x, y and attack
    }
}
class SmartAttack implements AttackStrategy {
    public void attack(Board board, int x, int y) {
        // Prioritize hitting adjacent squares after a hit
    }
}
```

* Observer Pattern (For Game Updates)

```Java
interface GameObserver {
    void update(Player player, int x, int y, boolean hit);
}

class ConsoleLogger implements GameObserver {
    public void update(Player player, int x, int y, boolean hit) {
        System.out.println(player.getName() + " attacked (" + x + ", " + y + ") - " + (hit ? "Hit!" : "Miss"));
    }
}
```

* Command Pattern (For Undo Feature)

```Java
class AttackCommand {
    private Board board;
    private int x, y;
    
    public AttackCommand(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }
    
    public void execute() {
        board.receiveAttack(x, y);
    }
    
    public void undo() {
        board.revertAttack(x, y);
    }
}
```

* State Pattern (For Game States)

Manages different game states like ongoing, player won, game over.

```Java
interface GameState {
    void handle(BattleshipGame game);
}

class OngoingState implements GameState {
    public void handle(BattleshipGame game) {
        System.out.println("Game is in progress.");
    }
}

class GameOverState implements GameState {
    public void handle(BattleshipGame game) {
        System.out.println("Game over!");
    }
}
```

* Singleton Pattern (For Game Management)

```Java
class BattleshipGame {
    private static BattleshipGame instance;

    private BattleshipGame() { /* Private constructor */ }

    public static BattleshipGame getInstance() {
        if (instance == null) {
            instance = new BattleshipGame();
        }
        return instance;
    }
}
```

## How Observer, Strategy, and Command Patterns Are Similar and Different at same time

Similarities
•	Encapsulate behavior inside separate objects.
•	Use interfaces to define a contract.
•	Help in decoupling components.
•	Provide flexibility and extensibility.

Differences
•	Observer: One-to-many relationship; notifies multiple components when an event occurs.
•	Strategy: One-to-one relationship; dynamically switches behavior at runtime.
•	Command: Encapsulates actions as objects; allows undo/redo functionality.

