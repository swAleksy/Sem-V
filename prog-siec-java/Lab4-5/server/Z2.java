import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Z2 {
    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Listening on port 4444");

            while (true){
                try (Socket clientSocket = serverSocket.accept();
            	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        System.out.println("polaczono ");

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            String reversedMsg = new StringBuilder(inputLine).reverse().toString();
                            out.println(reversedMsg);
                        }
                        System.out.println("zakonczenie polaczenia");     

                } catch (IOException e) {
                    System.err.println("connection err");
                    e.printStackTrace();
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}