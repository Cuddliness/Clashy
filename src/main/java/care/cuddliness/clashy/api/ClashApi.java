package care.cuddliness.clashy.api;

import care.cuddliness.clashy.api.http.ApiEndpoints;
import care.cuddliness.clashy.api.http.OkHttpProvider;
import com.google.gson.JsonObject;

public class ClashApi {

    OkHttpProvider provider = new OkHttpProvider();

    public ClashApi(OkHttpProvider provider) {
        this.provider = provider;
    }

    public JsonObject getAccount(String accountId){
        return  provider.getData(ApiEndpoints.GET_PLAYERS, accountId);
    }
}
