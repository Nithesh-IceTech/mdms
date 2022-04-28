package za.co.spsi.openmucdoa.interfaces;

public interface IChannelConfig {

    // Getters

    String getDescription();

    String getChannelName();

    String getDataType();

    String getChannelType();

    Boolean getListening();

    String getChannelAddressField();

    String getLoggingInterval();

    String getSamplingInterval();

    Boolean getDisabled();

    // Setters

    void setId( Long id );

    void setIedDeviceId( Long iedDeviceId );

    void setChannelStatus( String channelStatus );

    void setListening( Boolean listening );

    void setDisabled( Boolean disabled );

}
