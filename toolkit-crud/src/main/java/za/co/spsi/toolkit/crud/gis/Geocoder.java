package za.co.spsi.toolkit.crud.gis;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

/**
 * Created by francoism on 2017/05/30.
 */
public class Geocoder extends com.google.code.geocoder.Geocoder {

    @Override
    public GeocodeResponse geocode(GeocoderRequest geocoderRequest) throws IOException {
        Gson gson = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        String urlString = this.getURL(geocoderRequest);
        return this.request(gson, urlString.replace("http","https") + "&key=AIzaSyAGi3AezGbJ4BHt7TBxDXXECWJtsiwhr0E");
    }
}
