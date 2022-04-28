package za.co.spsi.toolkit.crud.payment.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RestClient {

    public static ResponseEntity makeRequest(String uri, String json, Map<String, String> propertiesMap) throws IOException {
        final HttpURLConnection urlConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
        String data = json;
        String result;
        propertiesMap.forEach((s, s2) -> urlConnection.setRequestProperty(s, s2));
        urlConnection.setDoOutput(true);

        urlConnection.setRequestMethod("POST");
        urlConnection.connect();

        //Write
        OutputStream outputStream = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(data);
        writer.close();
        outputStream.close();

        if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
            return new ResponseEntity(HttpStatus.valueOf(urlConnection.getResponseCode()));
        }

        //Read
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        bufferedReader.close();
        result = sb.toString();

        return new ResponseEntity(result, null, HttpStatus.valueOf(urlConnection.getResponseCode()));
    }

}
