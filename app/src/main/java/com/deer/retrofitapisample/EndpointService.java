package com.deer.retrofitapisample;

import com.deer.retrofitapisample.obj.LogResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public class EndpointService {
    /**
     * Check call is null or not and cancel it.
     *
     * @param call call to cancel
     */
    public static void cancelCall(Call call) {
        if (call != null && !call.isCanceled())
            call.cancel();
    }

    /*
    private interface Example {
        @POST("YOUR_API_ENDPOINT_URL")
        Call<YOUR_RESPONSE_OBJ> callApiHttpPostMethod(@Body PARAMETER_OBJ parameter);

        @GET("YOUR_API_ENDPOINT_URL")
        Call<YOUR_RESPONSE_OBJ> callApiHttpGetMethod(@Query("key") VALUE_TYPE value);
    }
    */

    private final static String api = "s/r9vz3f781zxklvm/logItem.json?dl=1";

    public interface LogInService {
        @GET(api)
        Call<LogResponse> getLogItem();
    }
}