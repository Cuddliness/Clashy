package care.cuddliness.clashy.api.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;

public class OkHttpProvider {
    public static final String BASE_URL = "https://api.clashofclans.com/v1";
    @Getter
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void setClient(OkHttpClient client, ApiEndpoints apiEndpoints, String[] parameters) {
        OkHttpProvider.client = client;
    }

    public JsonObject getData(String apiEndpoints, String parameter, String token) {
        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url(BASE_URL + apiEndpoints + parameter).addHeader("Authorization", "Bearer " + token)
                .addHeader("cache-control", "no-cache")
                .get().build();
        System.out.println(BASE_URL + apiEndpoints + parameter);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP code " + response.code()
                        + " (" + response.message() + ")");
            }

            // response.body().string() consumes the stream once, so store it first
            String body = response.body().string();
            System.out.println("Response body: " + body);

            // Parse safely – JsonParser throws a JsonSyntaxException if malformed
            return JsonParser.parseString(body).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean linkAccount(String playerTag, String token, String applicationToken) {
        RequestBody body = RequestBody.create("{ \"token\": \"" + token + "\"}", JSON); // new

        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url(BASE_URL + ApiEndpoints.GET_PLAYERS + playerTag + "/verifytoken").addHeader("Authorization", "Bearer " + applicationToken)
                .addHeader("cache-control", "no-cache")
                .post(body).build();
        try (Response response = client.newCall(request).execute()) {
            String responsebody = response.body().string();
            JsonObject tokenToPlayer = JsonParser.parseString(responsebody).getAsJsonObject();
            return tokenToPlayer.get("status").getAsString().equalsIgnoreCase("ok");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
