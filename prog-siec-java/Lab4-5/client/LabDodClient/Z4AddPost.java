package LabDodClient;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Z4AddPost {
    public static void addPost(String[] args) {
        String apiUrl = "https://jsonplaceholder.typicode.com/posts";
        String jsonInput = "{\"title\": \"foo\", \"body\": \"bar\"}"; 

        try{
            URL url = URI.create(apiUrl).toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            try(OutputStream outS = con.getOutputStream()){
                byte[] input = jsonInput.getBytes("utf-8");
                outS.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
                    String line;
                    StringBuilder response = new StringBuilder();
                    
                    while ((line = in.readLine()) != null)
                        response.append(line.trim());
                    
                    System.out.println("Response: ");
                    System.out.println(response.toString());
                }
            } else {System.out.println("ERR code: " + responseCode);}
        } catch (IOException e) {
            System.out.println("api err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}