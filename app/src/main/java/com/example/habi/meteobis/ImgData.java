package com.example.habi.meteobis;

import android.hardware.camera2.CaptureFailure;
import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Call;
import retrofit.CallAdapter;
import retrofit.Converter;
import retrofit.Retrofit;

public class ImgData {
//    public final byte[] data;
    public final int row;
    public final int col;

    public ImgData(/*byte[] data,*/ int row, int col) {
//        this.data = data;
        this.row = row;
        this.col = col;
    }

    public static class ByteArray {
        public final byte[] data;
        public ByteArray(byte[] data) {
            this.data = data;
        }
    }

    public static class CallAdapterFactory implements CallAdapter.Factory {
        private static final String TAG = CallAdapterFactory.class.getSimpleName();

        @Override
        public CallAdapter<ByteArray> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            return new CallAdapter<ByteArray>() {
                @Override
                public Type responseType() {
                    return ByteArray.class;
                }

                @Override
                public <R> ByteArray adapt(Call<R> call) {
                    Log.d(TAG, "adapt: begin");
//                    if (!(call instanceof Call<ResponseBody>)) {
//                        Log.w(TAG, "cannot adapt");
//                        return null;
//                    }

                    Call<ResponseBody> resp = (Call<ResponseBody>) call;
                    ResponseBody body = null;
                    try {
                        body = resp.execute().body();
                        return new ByteArray(body.bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        Log.d(TAG, "adapt: end");
                        try {
                            if (body != null) {
                                body.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
    }

    public static class ConverterFactory extends Converter.Factory {
        private static final String TAG = ConverterFactory.class.getSimpleName();

        @Override
        public Converter<ResponseBody, byte[]> fromResponseBody(Type type, Annotation[] annotations) {
            return body -> {
                Log.d(TAG, "fromResponseBody");
//                ByteArray result = new ByteArray(body.bytes());
                byte[] result = body.bytes();
                body.close();
                return result;
            };
        }

//        @Override
//        public Converter<ResponseBody, ImgData> fromResponseBody(Type type, Annotation[] annotations) {
//            return new Converter<ResponseBody, ImgData>() {
//                @Override
//                public ImgData convert(ResponseBody value) throws IOException {
//                    return new ImgData(value.bytes());
//                }
//            };
//        }

//        @Override
//        public ImgData convert(ResponseBody value) throws IOException {
//            return new ImgData(value.bytes());
//        }
    }
}
