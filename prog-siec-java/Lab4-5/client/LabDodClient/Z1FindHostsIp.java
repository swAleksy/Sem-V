package LabDodClient;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.URL;

public class Z1FindHostsIp {
    public static void findHostsIp(String[] args) {
        try {
            URL url = URI.create("https://news.ycombinator.com/").toURL();
            String host = url.getHost();
            InetAddress address = InetAddress.getByName(host);
            System.out.println("IP Address of " + host + ": " + address.getHostAddress());

        } catch (UnknownHostException e) {
            System.out.println("Failed to resolve IP address.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
