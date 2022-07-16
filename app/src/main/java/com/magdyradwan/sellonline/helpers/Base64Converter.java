package com.magdyradwan.sellonline.helpers;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Converter {
    public static String convertToStr(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data);
    }

    public static String convertToBase64(String str) {
        byte[] data = Base64.encode(str.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(data);
    }

    public static byte[] convertFromBase64ToByteArr(String base64) {
        return Base64.decode(base64.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }

    public static String convertFromByteArrToBase64(byte[] data) {
        return new String(Base64.encode(data, Base64.NO_WRAP));
    }
}
