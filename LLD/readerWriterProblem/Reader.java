package LLD.readerWriterProblem;

public class Reader extends Thread{
    private SharedResource resource;
    private int readerId;

    public Reader(SharedResource resource, int id){
        this.readerId = id;
        this.resource = resource;
    }

    @Override
    public void run(){
        while(true){
            resource.read(readerId);
            try{
                Thread.sleep(1500);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
