package com.echelon.wsdl.panoramix;
import lombok.SneakyThrows;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import java.net.URL;

@Singleton()
@TransactionManagement(value = TransactionManagementType.BEAN)
public class WSDLConfig {

    @Inject
    @ConfValue(value = "nes.wsdl.device", folder = "server")
    private String wsdlURLDeviceManager;

    private URL DEVICEMANAGER_WSDL_LOCATION;

    @Inject
    @ConfValue(value = "nes.wsdl.gateway", folder = "server")
    private String wsdlURLGatewayManager;

    private URL GATEWAYMANAGER_WSDL_LOCATION;

    public WSDLConfig() {
    }

    @PostConstruct
    @SneakyThrows
    public void Init()  {

        this.DEVICEMANAGER_WSDL_LOCATION = new URL( this.wsdlURLDeviceManager );

        this.GATEWAYMANAGER_WSDL_LOCATION = new URL( this.wsdlURLGatewayManager );

    }

    public URL getWsdlDeviceManager() {

        return this.DEVICEMANAGER_WSDL_LOCATION;
    }

    public String getDeviceManagerURL() {

        return this.wsdlURLDeviceManager;
    }

    public QName getDeviceManagerQname() {

        return new QName("http://wsdl.echelon.com/Panoramix/", "DeviceManager");
    }

    public URL getWsdlGatewayManager() {

        return this.GATEWAYMANAGER_WSDL_LOCATION;

    }

    public String getGatewayManagerURL() {

        return this.wsdlURLGatewayManager;
    }

    public QName getGatewayManagerQname() {

        return new QName("http://wsdl.echelon.com/Panoramix/", "GatewayManager");
    }

}
