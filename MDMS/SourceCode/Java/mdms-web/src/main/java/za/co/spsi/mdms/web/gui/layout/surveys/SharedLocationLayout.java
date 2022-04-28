package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.db.survey.SharedLocation;
import za.co.spsi.mdms.web.gui.fields.CityCdField;
import za.co.spsi.mdms.web.gui.fields.CountryCdField;
import za.co.spsi.mdms.web.gui.fields.ProvinceCdField;
import za.co.spsi.mdms.web.gui.fields.SuburbCdField;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.util.MaskId;

import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/03/27.
 */
public class SharedLocationLayout extends Layout {

    private SharedLocation sharedLocation = new SharedLocation();

    //@UIField(mandatory = true)
    public CountryCdField countryCd = new CountryCdField(sharedLocation.countryCd, this, MdmsLocaleId.COUNTRY);

    //@UIField(mandatory = true)
    public ProvinceCdField provinceCd = new ProvinceCdField(sharedLocation.provinceCd, this,countryCd);

    //@UIField(mandatory = true)
    public CityCdField cityCd = new CityCdField(sharedLocation.cityCd, this, provinceCd);

    //@UIField(mandatory = true)
    public SuburbCdField suburb = new SuburbCdField(sharedLocation.suburbCd, this, cityCd);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> standN = new LField<>(sharedLocation.standNumber, MdmsLocaleId.STAND_N, this);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> streetName = new LField<>(sharedLocation.streetName, MdmsLocaleId.STREET_NAME_NUMBER, this);

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
