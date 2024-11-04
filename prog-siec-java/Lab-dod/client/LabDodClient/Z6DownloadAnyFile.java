package LabDodClient;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.io.InputStream;

public class Z6DownloadAnyFile {
    public static void downloadFile(String[] args) {
        String downloadURL = "https://i.sstatic.net/NCpJd.png";
        String fileName = downloadURL.substring(downloadURL.lastIndexOf('/') + 1);
        String saveDir = System.getProperty("user.home") + "/" + fileName;

        try {
            URL url = URI.create(downloadURL).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozzila/5.0");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try (   InputStream inputStream = new BufferedInputStream(con.getInputStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(saveDir)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) 
                        fileOutputStream.write(buffer, 0, bytesRead);

                    System.out.println("Plik został pobrany i zapisany w: " + saveDir);
                }
            } else {
                System.err.println("err code: " + responseCode);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
