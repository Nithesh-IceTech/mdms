package com.echelon.wsdl.panoramix;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "GatewayManager", targetNamespace = "http://wsdl.echelon.com/Panoramix/")
public class GatewayManager extends Service
{
    @Inject
    public GatewayManager(WSDLConfig config) {
        super(config.getWsdlGatewayManager(), config.getGatewayManagerQname());
    }

    /**
     * 
     * @return
     *     returns GatewayManagerSoap
     */
    @WebEndpoint(name = "GatewayManagerSoap")
    public GatewayManagerSoap getGatewayManagerSoap() {
        return super.getPort(new QName("http://wsdl.echelon.com/Panoramix/", "GatewayManagerSoap"), GatewayManagerSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GatewayManagerSoap
     */
    @WebEndpoint(name = "GatewayManagerSoap")
    public GatewayManagerSoap getGatewayManagerSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://wsdl.echelon.com/Panoramix/", "GatewayManagerSoap"), GatewayManagerSoap.class, features);
    }

}