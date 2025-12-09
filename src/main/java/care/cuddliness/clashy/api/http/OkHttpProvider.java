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


    public static void setClient(OkHttpClient client, ApiEndpoints apiEndpoints, String[] parameters) {
        OkHttpProvider.client = client;
    }

    public JsonObject getData(String apiEndpoints, String parameter) {
        OkHttpClient client = getClient();
        Request request = new Request.Builder()
                .url(BASE_URL + apiEndpoints + parameter).addHeader("Authorization", "Bearer "+ System.getenv("CLASH_TOKEN"))
                .addHeader("cache-control", "no-cache")
                .get().build();
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
    }
