package com.deer.retrofitapisample;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Define your custom gson type adapter.
 */

public class GsonTypeAdapterFactory implements TypeAdapterFactory {
    private static final String TAG = GsonTypeAdapterFactory.class.getSimpleName();

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/*      [Example 1: use newTypeErrorHandleInstance]
        if (targetType.isAssignableFrom(TARGET_CLASS.class))
            return newTypeErrorHandleInstance(TARGET_CLASS.class, IS_JSON_OBJECT_OR_NOT);

        [Example 2: use other TypeAdapter]
        if (targetType.isAssignableFrom(TARGET_CLASS.class))
            return new YOUR_TYPE_ADAPTER();     */

        Class<? super T> targetType = type.getRawType();

        if (targetType.isAssignableFrom(Long.class) || targetType.isAssignableFrom(long.class))
            // register BiteLongTypeAdapter to avoid that use long adapter to read empty string.
            return newBiteLongTypeAdapter();
        else if (targetType.isAssignableFrom(Integer.class) || targetType.isAssignableFrom(int.class))
            return newBiteNumberTypeAdapter(Integer.class);
        else if (targetType.isAssignableFrom(Float.class) || targetType.isAssignableFrom(float.class))
            return newBiteNumberTypeAdapter(Float.class);
        else if (targetType.isAssignableFrom(Double.class) || targetType.isAssignableFrom(double.class))
            return newBiteNumberTypeAdapter(Double.class);
        else
            return null;    // use default
    }

    /**
     * Notice: dataGson only set "excludeFieldsWithoutExposeAnnotation". (not set any TypeAdapter)
     */
    private static <D> TypeAdapter newTypeErrorHandleInstance(final Class<D> typeOfDataClass, final boolean isDataAnObject) {
        return new TypeAdapter<D>() {
            @Override
            public void write(JsonWriter out, D value) throws IOException {
                Log.e(TAG, String.format("TypeAdapter: %s.write(): do nothing", typeOfDataClass.getSimpleName()));
            }

            @Override
            public D read(JsonReader in) throws IOException {
                JsonToken targetJsonToken = isDataAnObject ?
                        JsonToken.BEGIN_OBJECT : JsonToken.BEGIN_ARRAY;
                if (in.peek() == targetJsonToken) {
                    return EndPointGenerator.dataGson.fromJson(in, typeOfDataClass);
                } else {
                    Log.e(TAG, String.format("read: not a target (%s), path = %s", typeOfDataClass.getSimpleName(), in.getPath()));
                    in.skipValue();
                    return null;
                }
            }
        };
    }


    /**
     * Use this custom TypeAdapter when server return ""(empty string) at long field.
     * It will parse empty string to null. (long field will set 0L)
     */
    private static TypeAdapter newBiteLongTypeAdapter() {
        return new TypeAdapter<Long>() {
            @Override
            public void write(JsonWriter writer, Long value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            }

            @Override
            public Long read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                try {
                    return Long.valueOf(reader.nextString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };
    }

    private static TypeAdapter newBiteNumberTypeAdapter(final Class<?> N) {
        return new TypeAdapter<Number>() {
            @Override
            public void write(JsonWriter writer, Number value) throws IOException {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            }

            @Override
            public Number read(JsonReader reader) throws IOException {
                return readNumber(reader, N);
            }
        };
    }

    @Nullable
    private static Number readNumber(JsonReader reader, Class<?> N) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        try {
            if(N == Long.class)
                return Long.valueOf(reader.nextString());
            else if(N == Integer.class)
                return Integer.valueOf(reader.nextString());
            else if(N == Float.class)
                return Float.valueOf(reader.nextString());
            else if(N == Double.class)
                return Double.valueOf(reader.nextString());
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
