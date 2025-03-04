package LLD.readerWriterProblem;

public class Writer extends Thread{
    private SharedResource resource;
    private int writerId;

    public Writer(SharedResource resource, int id){
        this.writerId = id;
        this.resource = resource;
    }

    @Override
    public void run(){
        while(true){
            String newData = "Data from Writer " + writerId;
            resource.write(writerId, newData);
            try{
                Thread.sleep(1500);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
