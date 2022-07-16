package com.magdyradwan.sellonline.helpers;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileReaderHelper {

    private static final String TAG = "FileReaderHelper";

    public static byte[] readUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor pdf = context.getContentResolver().openFileDescriptor(uri, "r");
        assert pdf != null;
        assert pdf.getStatSize() <= Integer.MAX_VALUE;
        byte[] data = new byte[(int) pdf.getStatSize()];

        FileDescriptor fd = pdf.getFileDescriptor();
        FileInputStream fileStream = new FileInputStream(fd);
        fileStream.read(data);

        return data;
    }
}
