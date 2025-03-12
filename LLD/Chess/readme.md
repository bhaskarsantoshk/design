# Low-Level Design (LLD) of Chess

## Components
1. Board ( 8*8 grid )
2. Piece
3. Player
4. Game
5. Game State
6. Moves

## Class Diagrams

+----------------------+
|      ChessBoard      |
|----------------------|
| - squares[][]        |
| + resetBoard()       |
| + movePiece()        |
| + getValidMoves()    |
+----------------------+
|
v
+----------------------+
|       Square        |
|----------------------|
| - x: int            |
| - y: int            |
| - piece: Piece      |
+----------------------+

+----------------------+
|       Piece          |
|----------------------|
| - color: Color       |
| - position: Square   |
| + isValidMove()      |
| + move()            |
+----------------------+
/   |    |   |   |   \
/    |    |   |   |    \
v     v    v   v   v     v
King Queen Bishop Knight Rook Pawn

+----------------------+
|        Player        |
|----------------------|
| - name: String       |
| - color: Color       |
| + makeMove()         |
+----------------------+

+----------------------+
|        Game         |
|----------------------|
| - board: ChessBoard |
| - players[]: Player |
| - moves: Move[]     |
| + startGame()       |
| + makeMove()        |
| + isCheckmate()     |
| + isStalemate()     |
+----------------------+

+----------------------+
|        Move         |
|----------------------|
| - from: Square      |
| - to: Square        |
| - piece: Piece      |
| + execute()         |
| + undo()            |
+----------------------+

## How SOLID Principles Are Applied

Single Responsibility Principle (SRP) - Each class has a single responsibility.

* ChessBoard manages board state. 
* Piece and its subclasses encapsulate behavior of individual pieces. 
* Game manages game logic. 
* Move is responsible for a single move. 
* Player handles player-related operations.

Open/Closed Principle (OCP)

* The Piece class is open for extension but closed for modification. Instead of modifying Piece for each new rule, we extend it into King, Queen, etc. 
* The Move class can be extended to include special move types like castling, en passant, and pawn promotion.

Liskov Substitution Principle (LSP)

* Subclasses like King, Queen, etc., can replace the Piece class without breaking functionality.
* Any Piece object can be replaced by one of its subclasses without modifying other parts of the system.

Interface Segregation Principle (ISP)
* Instead of a large Piece class handling all move logic, we use specialized interfaces:
  * ICastleable for castling (implemented by King & Rook). 
  * IPromotable for pawn promotion (Pawn class).
  * IJumpable for knights.

Dependency Inversion Principle (DIP)
* Game depends on Piece via an abstraction (i.e., Piece class and its interfaces). 
* Board interacts with Piece through abstractions rather than concrete classes.

## Where Design Patterns Are Used

* Factory Pattern (For Creating Pieces)

```Java
class PieceFactory {
    public static Piece createPiece(String type, Color color, Square position) {
        switch (type) {
            case "King": return new King(color, position);
            case "Queen": return new Queen(color, position);
            case "Bishop": return new Bishop(color, position);
            case "Knight": return new Knight(color, position);
            case "Rook": return new Rook(color, position);
            case "Pawn": return new Pawn(color, position);
            default: throw new IllegalArgumentException("Invalid piece type");
        }
    }
}
```

* Strategy Pattern (For Piece Movements)

Each piece has a different movement logic, so we use a strategy pattern to encapsulate it.

```Java
interface MoveStrategy {
    boolean isValidMove(Square from, Square to, ChessBoard board);
}

class QueenMoveStrategy implements MoveStrategy {
    public boolean isValidMove(Square from, Square to, ChessBoard board) {
        // Combination of Rook and Bishop logic
    }
}

class KnightMoveStrategy implements MoveStrategy {
    public boolean isValidMove(Square from, Square to, ChessBoard board) {
        // L-shaped moves
    }
}
```

* Observer Pattern (For Game Status Updates)

Whenever a move is made, observers (UI, logs, AI engine) are notified.

```Java
interface GameObserver {
    void update(Game game);
}

class Logger implements GameObserver {
    public void update(Game game) {
        System.out.println("Move made: " + game.getLatestMove());
    }
}

class ChessGame {
    private List<GameObserver> observers = new ArrayList<>();
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this);
        }
    }
}
```

* Command Pattern (For Moves & Undo)

Encapsulates moves into objects, allowing undo functionality.

```Java
class MoveCommand {
    private Piece piece;
    private Square from, to;
    
    public MoveCommand(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }
    
    public void execute() {
        piece.move(to);
    }
    
    public void undo() {
        piece.move(from);
    }
}
```

* State Pattern (For Game Status)
  Manages game states like ongoing, check, checkmate, stalemate.
```Java
interface GameState {
    void handle(Game game);
}

class OngoingState implements GameState {
    public void handle(Game game) {
        System.out.println("Game is in progress");
    }
}

class CheckmateState implements GameState {
    public void handle(Game game) {
        System.out.println("Checkmate! Game over.");
    }
}
```

* Singleton Pattern (For ChessBoard)

Ensures only one board exists at a time.

```Java
class ChessBoard {
    private static ChessBoard instance;

    private ChessBoard() { /* Private constructor */ }

    public static ChessBoard getInstance() {
        if (instance == null) {
            instance = new ChessBoard();
        }
        return instance;
    }
}
```
