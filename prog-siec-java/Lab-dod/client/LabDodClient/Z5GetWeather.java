package LabDodClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

public class Z5GetWeather {
    public static void getWeather(String[] args) {
        String apiKey = "1adc852a1d29fadba897097aef617a8c";
        String city = "Krakow";
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line.trim());
                    }
                    System.out.println("Temperatura " + city);
                    // System.out.println(response.toString());
                    JSONObject jsonObject = new JSONObject(response.toString());
                    double temperature = jsonObject.getJSONObject("main").getDouble("temp");
                    System.out.println("Temperature: " + temperature + "°C");
                }
            } else {
                System.out.println("err code: " + responseCode);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}