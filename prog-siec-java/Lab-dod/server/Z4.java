import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Z4 {
    private static Set<ClientHandler> clients = new HashSet<>();
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4444)){
            System.out.println("Server Z5, port 4444");
            int counter = 1;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, counter);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
                counter++;
            }


        } catch (IOException e ) {
            System.err.println("err: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clients){
            if (clientHandler != sender)
                clientHandler.sendMessage(sender.id + " >"+ message);
        }
    }

    private static class ClientHandler implements Runnable{
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
    
        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }        
    
        @Override
        public void run(){
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("Connected.");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Con " + id + " - " + message);
                    Z5.broadcastMessage(message, this);
                }

            } catch (IOException e) {
                System.err.println("err: " + e.getMessage());
                e.printStackTrace();

            } finally {
                closeConnection();
            }

        }

        public void sendMessage(String message){
            out.println(message);
        }

        private void closeConnection() {
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                System.err.println("err.");
            } finally {
                clients.remove(this);
                System.out.println("disconnected");
            }
        }
    }
}

