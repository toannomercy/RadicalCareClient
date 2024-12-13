package org.example.radicalmotor.Util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class RecaptchaUtil {
    public static final String RECAPTCHA_SERVER_URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET_KEY = "6LeAVwsqAAAAAKIwclT0jCHAzZAjwMTXhKsrToCj";

    public static boolean verify(String gRecaptchaResponse) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RECAPTCHA_SERVER_URL + "?secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return false;
            }

            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.getBoolean("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
