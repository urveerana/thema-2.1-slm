import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yuri on 8/5/2014.
 */
public class Receiver {
    public Receiver(int port, int threadsPerPool, int maxPools){
        try{
            int threadsInCurrentPool = 0;
            int currentPool = 0;

            int clusters = 0;

            DatabaseConnection databaseConnection = new DatabaseConnection();

            ServerSocket socket = new ServerSocket(port);
            Socket client = null;

            // Create threadpools
            ExecutorService[] threadPools = new ExecutorService[maxPools];

            System.out.println("Receiver running with " + maxPools + " thread pools");

            while(true){
                // Accept client and assign to thread
                client = socket.accept();

                clusters++;
                System.out.println("Cluster " + clusters + " accepted");

                if(threadsInCurrentPool < threadsPerPool){
                    if(threadPools[currentPool] == null){
                        threadPools[currentPool] = Executors.newFixedThreadPool(threadsPerPool);
                    }

                    threadPools[currentPool].submit(new ClusterConnection(client, databaseConnection));
                    threadsInCurrentPool++;
                } else {
                    threadsInCurrentPool = 0;
                    currentPool++;
                }
            }
        } catch (Exception e){

        }
    }
}
