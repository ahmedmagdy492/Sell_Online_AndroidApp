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
}
