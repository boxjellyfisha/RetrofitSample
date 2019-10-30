package com.deer.retrofitapisample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create, set, and hold a retrofit instance.<br/>
 * Use Gson to serialize/deserialize parameter and response.<br/>
 */

public class EndPointGenerator {

    public static final String BASE_URL = "https://www.dropbox.com/";
    /**
     * Gson for retrofit. Add your custom TypeAdapter at {@link GsonTypeAdapterFactory}
     */
    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new GsonTypeAdapterFactory())
            .create();

    /**
     * Gson for GsonTypeAdapter parsing the raw data
     */
    static Gson dataGson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    /**
     * for OkHttpClient logging
     */
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS);

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

    /**
     * retrofit instance
     */
    private static Retrofit retrofit = retrofitBuilder
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    /**
     * Get api endpoint via retrofit. Define endpoint at {@link EndpointService}
     *
     * @param serviceClass endpoint you want to create.
     * @return created endpoint.
     */
    public static <E> E getApiEndpoint(Class<E> serviceClass) {
        /* if Client builder did not contain logging interceptor, add one and rebuild retrofit. */
        if (BuildConfig.DEBUG && !okHttpClientBuilder.interceptors().contains(httpLoggingInterceptor)) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            retrofitBuilder.client(okHttpClientBuilder.build());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(serviceClass);
    }
}
