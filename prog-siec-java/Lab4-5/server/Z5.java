import java.io.*;
import java.net.*;
import java.nio.file.*;

public class Z5 {
    public static void main(String[] args) {
        int port = 8080;
        String rootDirectory = "www";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket, rootDirectory);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket, String rootDirectory) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || !requestLine.startsWith("GET")) {
                sendResponse(out, "HTTP/1.1 400 Bad Request", "Invalid request");
                return;
            }

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                sendResponse(out, "HTTP/1.1 400 Bad Request", "Invalid request");
                return;
            }

            String filePath = parts[1].equals("/") ? "/index.html" : parts[1];
            File file = new File(rootDirectory + filePath);

            if (!file.exists() || file.isDirectory()) {
                sendResponse(out, "HTTP/1.1 404 Not Found", "File not found");
                return;
            }

            // Send the file contents
            sendFileResponse(out, file);
        } catch (IOException e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }

    private static void sendResponse(OutputStream out, String statusLine, String body) throws IOException {
        String response = statusLine + "\r\n" +
                          "Content-Length: " + body.length() + "\r\n" +
                          "Content-Type: text/plain\r\n\r\n" +
                          body;
        out.write(response.getBytes());
    }

    private static void sendFileResponse(OutputStream out, File file) throws IOException {
        String contentType = Files.probeContentType(file.toPath());
        byte[] fileContent = Files.readAllBytes(file.toPath());

        String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + fileContent.length + "\r\n" +
                        "Content-Type: " + (contentType != null ? contentType : "application/octet-stream") + "\r\n\r\n";
        out.write(header.getBytes());
        out.write(fileContent);
    }
}
