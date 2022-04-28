package za.co.spsi.mdms.common.services.broker;

import za.co.spsi.mdms.generic.meter.db.GenericBrokerCommandEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.security.Secured;

import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static za.co.spsi.mdms.common.services.broker.BrokerRequest.Command.CUT;
import static za.co.spsi.mdms.common.services.broker.BrokerRequestResponse.Status.*;

@Path("broker")
@DependsOn("MDMSUpgradeService")
public class BrokerController {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private BrokerService brokerService;

    @Secured("PecMeterCutter")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response action(@Context HttpServletRequest request, BrokerRequest brokerRequest) {
        KamstrupMeterEntity kamMeter =
                DataSourceDB.getFromSet(dataSource, (KamstrupMeterEntity) new KamstrupMeterEntity().serialN.set(brokerRequest.getSerialN()));

        NESMeterEntity nesMeter = kamMeter == null ?
                DataSourceDB.getFromSet(dataSource, (NESMeterEntity) new NESMeterEntity().serialN.set(brokerRequest.getSerialN())) : null;

        GenericMeterEntity genericMeterEntity = kamMeter == null && nesMeter == null ?
                DataSourceDB.getFromSet(dataSource, (GenericMeterEntity) new GenericMeterEntity().meterSerialN.set(brokerRequest.getSerialN())) : null;

        if (kamMeter != null || nesMeter != null || genericMeterEntity != null) {
            return Response.status(Response.Status.CREATED).
                    entity(kamMeter != null ?
                            brokerService.processKamstrupCommand(kamMeter,
                                    brokerRequest.getCommand() == CUT ? KamstrupBrokerCommandEntity.Command.CUT :
                                            KamstrupBrokerCommandEntity.Command.RELEASE, brokerRequest.getEffectDate()) :
                            nesMeter != null ?
                                    brokerService.processNesCommand(nesMeter,
                                            brokerRequest.getCommand() == CUT ? NESBrokerCommandEntity.Command.DISCONNECT :
                                                    NESBrokerCommandEntity.Command.CONNECT, brokerRequest.getEffectDate()) :

                                    brokerService.processGenericCommand(genericMeterEntity,
                                            brokerRequest.getCommand() == CUT ? GenericBrokerCommandEntity.Command.DISCONNECT :
                                                    GenericBrokerCommandEntity.Command.CONNECT, brokerRequest.getEffectDate())).build();

        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured("PecMeterCutter")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("{ref}")
    public Response query(@Context HttpServletRequest request, @PathParam("ref") String ref) {
        KamstrupBrokerCommandEntity kBroker = DSDB.getFromSet(dataSource, new KamstrupBrokerCommandEntity().brokerCommandId.set(ref));

        NESBrokerCommandEntity nBroker = kBroker == null ?
                DSDB.getFromSet(dataSource, new NESBrokerCommandEntity().nesBrokerCommandId.set(ref)) : null;

        GenericBrokerCommandEntity genericBrokerCommandEntity = kBroker == null && nBroker == null ?
                DSDB.getFromSet(dataSource, new GenericBrokerCommandEntity().genericBrokerCommandId.set(ref)) : null;

        if (kBroker != null || nBroker != null || genericBrokerCommandEntity != null) {
            return Response.status(
                    Response.Status.OK).entity(
                    kBroker != null ?
                            new BrokerQueryResponse(ref, kBroker.status.getNonNull() < 4 ? PENDING : kBroker.status.getNonNull() == 6 ? COMPLETED : FAILED,
                                    kBroker.failedReason.get()) :

                            nBroker != null ?
                                    new BrokerQueryResponse(ref, nBroker.commandStatus.getNonNull() < 4 ? PENDING : nBroker.commandStatus.getNonNull() == 6 ? COMPLETED : FAILED,
                                            nBroker.error.get()) :

                                    new BrokerQueryResponse(ref, genericBrokerCommandEntity.status.getNonNull() < 4 ? PENDING : genericBrokerCommandEntity.status.getNonNull() == 6 ? COMPLETED : FAILED,
                                            genericBrokerCommandEntity.error.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
