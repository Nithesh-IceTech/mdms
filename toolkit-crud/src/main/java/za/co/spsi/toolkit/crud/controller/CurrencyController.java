package za.co.spsi.toolkit.crud.controller;

import org.json.JSONArray;
import za.co.spsi.toolkit.crud.entity.ExchangeRateEntity;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

public interface CurrencyController {

    static final Logger LOG = Logger.getLogger(CurrencyController.class.getName());

    public abstract DataSource getDataSource();

    public default Response  retrieveCurrency(String baseCurrency, String agencyId ) {
        List<ExchangeRateEntity> exchangeRateEntityList =
                new DataSourceDB<>(ExchangeRateEntity.class).getAllAsList(getDataSource(),
                        "WITH Currencies AS (SELECT ser.Target_Currency_Cd, max(ser.From_D) as Max_From_D\n" +
                                "from Exchange_Rate ser\n" +
                                "where ser.Base_Currency_Cd = '"+baseCurrency+"' and\n" +
                                "ser.From_D <= systimestamp and ser.Agency_Id = " + agencyId + "\n" +
                                "group by Target_Currency_Cd)\n" +
                                "Select ser.* from Currencies c, Exchange_Rate ser\n" +
                                "where c.Target_Currency_Cd = ser.Target_Currency_Cd and\n" +
                                "c.Max_From_D = ser.From_D and ser.Agency_Id = " + agencyId);

        JSONArray jsonArray = new JSONArray();
        exchangeRateEntityList.forEach(item -> jsonArray.put(item.getAsJson()));

        if (jsonArray.length() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
        }
    }

    @Path("retrieveCurrency/{agencyId}")
    @GET
    default Response retrieveCurrency(@Context HttpServletRequest request, @PathParam("agencyId") String agencyId) {
        return retrieveCurrency("USD",agencyId);

    }

    @Path("retrieveCurrency/{agencyId}/{baseCurrency}")
    @GET
    default Response retrieveCurrency(@Context HttpServletRequest request
            , @PathParam("agencyId") String agencyId,@PathParam("baseCurrency") String baseCurrency) {
        return retrieveCurrency(baseCurrency,agencyId);
    }

    @Path("retrieveCurrency/")
    @GET
    default Response retrieveCurrency(@Context HttpServletRequest request) {

        List<ExchangeRateEntity> exchangeRateEntityList =
                new DataSourceDB<>(ExchangeRateEntity.class).getAllAsList(getDataSource(),
                        "select * from EXCHANGE_RATE");

        JSONArray jsonArray = new JSONArray();
        exchangeRateEntityList.forEach(item -> jsonArray.put(item.getAsJson()));

        if (jsonArray.length() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.OK).entity(jsonArray.toString()).build();
        }
    }
}
