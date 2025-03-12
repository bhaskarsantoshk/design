package LLD.Chess;

public class PieceFactory {
    public static Piece createPiece( String type, Color color, Square position){
        switch(type) {
            case "King" : return new King(color, position);
            default: throw new IllegalArgumentException("Invalid piece type");
        }
    }
}
