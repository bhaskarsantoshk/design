package LLD.readerWriterProblem;

public class ReaderWriterProblem {
    public static void main(String[] args){
        SharedResource resource = new SharedResource();
        for ( int i=0; i<=2; i++){
            new Reader(resource, i).start();
        }
        for ( int i=0; i<=2; i++){
            new Writer(resource, i).start();
        }
    }
}
