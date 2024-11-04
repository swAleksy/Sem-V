import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Listening on port 4444");
            try(Socket clientSocket = serverSocket.accept();
            	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println("Witaj świecie!");
                Thread.sleep(1000);
                out.println("Zapraszam Ponownie!");
                Thread.sleep(1000);
            }
            System.out.println("Zakończono działanie.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
