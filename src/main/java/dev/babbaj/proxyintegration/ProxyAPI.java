package dev.babbaj.proxyintegration;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProxyAPI {

    public record AccountList(String domain, List<String> accounts) {}

    // TODO: async
    public static AccountList getActiveAccounts() throws IOException, InterruptedException {
        String address = hasWireguardRoute() ? "http://192.168.69.1:6969" : "https://headlessapi.futureclient.gold";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(address + "/active")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        AccountList result = new Gson().fromJson(body, AccountList.class);
        return result;
    }

    private static boolean hasWireguardRoute() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows") ?
                hasWireguardRouteWindoze()
                : hasWireguardRouteLinux();
    }

    private static boolean hasWireguardRouteLinux() {
        try {
            Process process = Runtime.getRuntime().exec("ip address show to 192.168.69.0/24");
            try (InputStream stream = process.getInputStream()) {
                // if there's a route we get some info, if not we get no output
                return stream.read() != -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasWireguardRouteWindoze() {
        try {
            Process process = Runtime.getRuntime().exec("route print");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().anyMatch(l -> l.contains("192.168.69."));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
