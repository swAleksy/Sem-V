import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Z1GiveTime {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("Listening on port 4444");

            try(Socket clientSocket = serverSocket.accept();
            	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(Calendar.getInstance().getTime());
                out.println(timeStamp);
            }
            
            System.out.println("Zakończono działanie.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}