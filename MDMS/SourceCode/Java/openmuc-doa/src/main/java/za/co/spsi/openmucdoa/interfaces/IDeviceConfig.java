package za.co.spsi.openmucdoa.interfaces;

public interface IDeviceConfig {

    Long getId();

    Long getIedDeviceId();

    Long getDriverId();

    String getIedName();

    String getDescription();

    String getDeviceAddressField();

    String getDefaultDeviceAddressField();

    String getSettingsField();

    String getDefaultSettings();

    Boolean getDisabled();

}
