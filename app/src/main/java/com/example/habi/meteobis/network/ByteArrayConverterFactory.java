package com.example.habi.meteobis.network;

import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * Created by Gabriel Fortin
 */
public class ByteArrayConverterFactory extends Converter.Factory {
    private static final String TAG = ByteArrayConverterFactory.class.getSimpleName();

    @Override
    public Converter<ResponseBody, byte[]> fromResponseBody(Type type, Annotation[] annotations) {
        return body -> {
            byte[] result = body.bytes();
            body.close();
            return result;
        };
    }
}
