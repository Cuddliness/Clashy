package care.cuddliness.clashy.api;

import care.cuddliness.clashy.api.http.ApiEndpoints;
import care.cuddliness.clashy.api.http.OkHttpProvider;
import care.cuddliness.clashy.api.obj.clan.ClashClan;
import care.cuddliness.clashy.api.obj.player.ClashPlayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;

public class ClashApi {

    private String TOKEN = "";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpProvider provider = new OkHttpProvider();

    public ClashApi() {
        login();
    }
    public void login() {
        if(TOKEN.isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create("{\"email\":\"" + System.getenv("DEV_MAIL") + "\",\"password\":\"" + System.getenv("DEV_PASSWORD") + "\"}", JSON); // new
            Request request = new Request.Builder()
                    .url("https://developer.clashofclans.com/api/login")
                    .addHeader("cache-control", "no-cache")
                    .post(body).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected HTTP code " + response.code()
                            + " (" + response.message() + ")");
                }
                // response.body().string() consumes the stream once, so store it first
                String responsebody = response.body().string();
                System.out.println("Response body: " + body);

                // Parse safely – JsonParser throws a JsonSyntaxException if malformed
                TOKEN = String.valueOf(JsonParser.parseString(responsebody).getAsJsonObject().get("temporaryAPIToken"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refreshToken(){
        login();
    }

    //Get clash of clans player data
    public ClashPlayer getClashPlayer(String accountTag){
        return new Gson().fromJson(getAccount(accountTag), ClashPlayer.class);
    }
    public JsonObject getAccount(String accountId){
        return  provider.getData(ApiEndpoints.GET_PLAYERS, accountId, TOKEN);
    }
    private JsonObject getClanJson(String accountId){
        return  provider.getData(ApiEndpoints.GET_CLANS, accountId.replace("#", "%23"), TOKEN);
    }
    public boolean linkAccount(String accountId, String accountToken){
        return  provider.linkAccount(accountId, accountToken, TOKEN);
    }
    public ClashClan getClan(String clanId){
        return new Gson().fromJson(getClanJson(clanId), ClashClan.class);
    }

}
