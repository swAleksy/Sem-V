package LabDodClient;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Z2DownloadHtml {
    public static void downloadHtml(String[] args) throws IOException {
        String targetUrl = "https://save-walt.onrender.com/";
        URL url = URI.create(targetUrl).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try(InputStream inputStream = con.getInputStream();
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {

                System.out.println(targetUrl +": ");
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
        }
        else { System.out.println("err; kod: " + responseCode); }
    }
}
