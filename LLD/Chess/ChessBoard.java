package LLD.Chess;

public class ChessBoard {
    private static ChessBoard board;

    ChessBoard(){
    }

    public ChessBoard getInstance(){
        if ( board == null) board = new ChessBoard();
        return board;
    }
}
