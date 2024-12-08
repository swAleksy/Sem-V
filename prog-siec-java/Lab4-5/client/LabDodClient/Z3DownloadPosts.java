package LabDodClient;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Z3DownloadPosts {
    public static void downloadPosts(String[] args) throws IOException {
        String apiUrl = "https://jsonplaceholder.typicode.com/posts";

        URL url = URI.create(apiUrl).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK){
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null)
                    response.append(line).append("\n");
                
                System.out.println("posty: ");
                System.out.println(response.toString());
            }  
        } else {System.out.println("ERR: " + responseCode);} 
    }
}
