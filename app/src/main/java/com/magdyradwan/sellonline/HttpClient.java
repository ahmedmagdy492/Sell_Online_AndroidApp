package com.magdyradwan.sellonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpClient {

    private final SharedPreferences _sharedPreferences;
    private final Uri url;

    public HttpClient(Context context, SharedPreferences sharedPreferences)
            throws MalformedURLException {
        _sharedPreferences = sharedPreferences;
        url = Uri.parse(context.getString(R.string.api_url));
    }

    public String getRequest(String url_path) throws IOException, UnAuthorizedException {
        Uri.Builder builder = url.buildUpon();
        builder.appendEncodedPath(url_path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(builder.toString())
                .openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " +
                _sharedPreferences.getString("token", ""));
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        if(httpURLConnection.getResponseCode() == 401)
            throw new UnAuthorizedException("UnAuthorized");

        return readFromInputStream(httpURLConnection.getInputStream());
    }

    public String postRequest(String url_path, String data) throws IOException, UnAuthorizedException {
        Uri.Builder builder = url.buildUpon();
        builder.appendEncodedPath(url_path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(builder.toString())
                .openConnection();

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        if(_sharedPreferences.contains("token"))
        {
            httpURLConnection.setRequestProperty("Authorization", "Bearer "
                    + _sharedPreferences.getString("token", ""));
        }

        OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
        out.write(data);
        out.flush();
        out.close();

        Log.d("HttpClient", "post body: ");

        httpURLConnection.connect();

        Log.d("HttpClient", "after connection: " +
                httpURLConnection.getResponseCode());

        if(httpURLConnection.getResponseCode() == 401) {
            throw new UnAuthorizedException("Invalid Email or Password");
        }

        if(!String.valueOf(httpURLConnection.getResponseCode()).startsWith("2")) {
            String response = readFromInputStream(httpURLConnection.getInputStream());
            Log.d("HttpClient", "postRequest: " + response);
            throw new IOException(response);
        }

        return readFromInputStream(httpURLConnection.getInputStream());
    }

    private String readFromInputStream(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(reader);

        String line = br.readLine();

        while(line != null) {
            str.append(line);
            line = br.readLine();
        }

        br.close();

        return str.toString();
    }
}
