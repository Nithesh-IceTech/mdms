package za.co.spsi.toolkit.crud.gis.service;

import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.spsi.toolkit.crud.gis.Geocoder;
import za.co.spsi.toolkit.crud.gis.db.TripEntity;
import za.co.spsi.toolkit.crud.service.MasterNodeService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.db.DefaultConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@Dependent
public class TripAddressService extends ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(TripAddressService.class);

    protected Processor processor = getProcessor();

    @Inject
    private DefaultConfig defaultConfig;

    @Inject
    @ConfValue("tripAddressProcessorEnabled")
    private boolean processEnabled = true;


    public void reverseGeoAddress(Connection connection) throws SQLException, InvalidKeyException, IOException {


        final Geocoder geocoder = new Geocoder();

        //From addresses:
        DataSourceDB<TripEntity> fromTripEntities = new DataSourceDB<>(TripEntity.class).getAll(connection,
                DriverFactory.getDriver().limitSql("select * from trip where from_address is null and from_lat is not null " +
                        "and from_lon is not null", 50));
        for (TripEntity tripEntity : fromTripEntities) {
            LOG.info("Reverse FROM address for: " + tripEntity.tripId);

            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().
                    setLocation(new LatLng(BigDecimal.valueOf(tripEntity.fromLat.get()),
                            BigDecimal.valueOf(tripEntity.fromLon.get()))).getGeocoderRequest();

            List<GeocoderResult> geocoderResultList = geocoder.geocode(geocoderRequest).getResults();

            if (!geocoderResultList.isEmpty()) {
                tripEntity.fromAddress.set(geocoderResultList.get(0).getFormattedAddress());
            }  else {
                tripEntity.fromAddress.set("UNKNOWN");
            }
            DataSourceDB.set(connection, tripEntity);
        }

        //To addresses:
        DataSourceDB<TripEntity> toTripEntities = new DataSourceDB<>(TripEntity.class).getAll(connection,
                DriverFactory.getDriver().limitSql("select * from trip where to_address is null and to_lat is not null " +
                        "and to_lon is not null", 50));
        for (TripEntity tripEntity : toTripEntities) {
            LOG.info("Reverse TO address for: " + tripEntity.tripId);

            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().
                    setLocation(new LatLng(BigDecimal.valueOf(tripEntity.toLat.get()),
                            BigDecimal.valueOf(tripEntity.toLon.get()))).getGeocoderRequest();

            List<GeocoderResult> geocoderResultList = geocoder.geocode(geocoderRequest).getResults();

            if (!geocoderResultList.isEmpty()) {
                tripEntity.toAddress.set(geocoderResultList.get(0).getFormattedAddress());
            }  else {
                tripEntity.toAddress.set("UNKNOWN");
            }
            DataSourceDB.set(connection, tripEntity);
        }
    }

    /**
     * step through the trip data and get to and from addresses
     */
    @PostConstruct
    public void init() {
        if (processEnabled) {
            processor.minutes(1).repeat(() -> {
                    if (MasterNodeService.getMaster()) {
                        LOG.info("Master Service enabled, so Trip Servive will be executed");
                        DataSourceDB.executeInTx(defaultConfig.getDataSource(), connection -> reverseGeoAddress(connection));
                    } else {
                        LOG.info("Master Service disabled, so Trip Servive will not be be executed");
                    }
            }  );
        }
    }
}
