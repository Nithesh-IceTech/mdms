/**
 * Created by marco on 30/05/16.
 */

window.za_co_spsi_toolkit_crud_gui_gis_Location = function () {

    var self = this;

    this.location = function () {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                function (position) {
                    var latitude = position.coords.latitude;
                    var longitude = position.coords.longitude;
                    var accuracy = position.coords.accuracy;
                    self.onLocationFound(latitude, longitude, accuracy);
                },
                function (error) {
                    self.onLocationError(error.code);
                }
            );
        }
        else {
            self.onLocationNotSupported();
        }
    };


};
