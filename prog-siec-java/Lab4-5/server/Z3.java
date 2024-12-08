import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Z3 {
    public static void main(String[] args) {
        
        try (ServerSocket serverSocket = new ServerSocket(4444)){
            System.out.println("Listening on port 4444 ");

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("new connection");

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e){
            System.err.println("err:" + e.getMessage());
            e.printStackTrace();
        }
    }
}


class ClientHandler implements Runnable{
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try (PrintWriter out  = new PrintWriter(clientSocket.getOutputStream(),true)){

            for (int i = 0; i <= 10; i++){
                out.println("msg: " + i + "/10");
                Thread.sleep(1000); 
            }

        } catch (IOException | InterruptedException e){
            System.err.println("err:" + e.getMessage());
            e.printStackTrace();            
        } finally {
            try {
                clientSocket.close();
                System.out.println("connection ended.");
            } catch (IOException e){
                System.err.println("err:" + e.getMessage());
                e.printStackTrace();     
            }
        }
    }
}