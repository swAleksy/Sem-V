package LabDodClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class Z7map {
    public static void main(String[] args) {
        String address = "50.09996814548495, 19.99884764060669";
        String mapHtmlFile = "map.html";

        try {
            String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
            String mapApiUrl = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";
            
            URL url = URI.create(mapApiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); 

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());

                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonResponse.append(scanner.nextLine());
                }
                scanner.close();

                // Wyciąganie współrzędnych z JSON (prosty parsing)
                String response = jsonResponse.toString();
                String lat = extractJsonField(response, "lat");
                String lon = extractJsonField(response, "lon");

                if (lat != null && lon != null) {
                    System.out.println("coords: " + lat + ", " + lon);

                    // Tworzenie pliku HTML z mapą
                    String htmlContent = generateMapHtml(lat, lon);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(mapHtmlFile))) {
                        writer.write(htmlContent);
                    } 
                    System.out.println("mapa saved");
                } else {
                    System.out.println("ERROR");
                }
            } else {
                System.out.println("API ERROR " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractJsonField(String json, String field) {
        String searchKey = "\"" + field + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private static String generateMapHtml(String lat, String lon) {
        String html = 
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>Mapa</title>\n" +
            "    <meta charset=\"utf-8\" />\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Mapa Lokalizacji</h1>\n" +
            "    <iframe\n" +
            "        width=\"100%\"\n" +
            "        height=\"500\"\n" +
            "        frameborder=\"0\"\n" +
            "        scrolling=\"no\"\n" +
            "        marginheight=\"0\"\n" +
            "        marginwidth=\"0\"\n" +
            "        src=\"https://www.openstreetmap.org/export/embed.html?bbox=" +
            (Double.parseDouble(lon) - 0.01) + "," + 
			(Double.parseDouble(lat) - 0.01) + "," + 
			(Double.parseDouble(lon) + 0.01) + "," +
			(Double.parseDouble(lat) + 0.01) + "&layer=mapnik&marker=" + lat + "," + lon + "\">\n" +
            "    </iframe>\n" +
            "    <br/>\n" +
            "    <small>\n" +
            "        <a href=\"https://www.openstreetmap.org/?mlat=" + lat + "&mlon=" + lon + "#map=14/" + lat + "/" + lon + "\">\n" +
            "            View Larger Map\n" +
            "        </a>\n" +
            "    </small>\n" +
            "</body>\n" +
            "</html>";
        return html;
    }
}

