package com.deer.retrofitapisample;

import android.support.annotation.Nullable;
import android.util.Log;

import com.deer.retrofitapisample.obj.BaseResponse;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiRequestCallBack<T extends BaseResponse> implements Callback<T> {
    private static final String TAG = ApiRequestCallBack.class.getSimpleName();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onResponseReady();
        if (response.isSuccessful()) {
            T result = response.body();
            if (result != null) {
                switch (result.getStatusCode()) {
                    case 200: {
                        onApiSuccess(call, result);
                        break;
                    }

                    default: { //Not success
                        String errorCode = result.getErrorCode();
                        Log.e(TAG, String.format(
                                "onResponse error: status code: %s", result.getStatusCode()));
                        Log.e(TAG, String.format(
                                "onResponse error: error code: %s", errorCode));
                        onAnyFailure(call, null, errorCode);
                    }
                }
            } else {
                onAnyFailure(call, null, null);
            }
        } else {
            if (response.errorBody() != null) {
                Log.e(TAG, "onResponse: response.message() = " + response.message());
                onAnyFailure(call, null, "");
            }
        }
        onResponseFinal();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t != null && t instanceof SocketTimeoutException) {
            // Time out failure
            onAnyFailure(call, t, "Internet Error");
        } else
            onAnyFailure(call, t, "");
        onResponseFinal();
    }

    /* *********************** */
    /* Custom callback methods */
    /* *********************** */

    /**
     * Call before handling this response. (Optional)
     */
    public void onResponseReady() {}

    /**
     * A success response with response object.
     */
    public abstract void onApiSuccess(Call<T> call, T response);

    /**
     * Any failure will call this method.
     */
    public abstract void onAnyFailure(Call<T> call, @Nullable Throwable t, @Nullable String errorCode);

    /**
     * Call after handling this response. (Optional)
     */
    public void onResponseFinal() {}
}
