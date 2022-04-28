package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

public class ICEMeterPortfolioView extends EntityDB {

    @Column(name = "ICE_METER_ID")
    public Field<String> iceMeterId = new Field<>(this);

    @Column(name = "ICE_METER_NUMBER")
    public Field<String> iceMeterNumber = new Field<>(this);

    @Column(name = "METER_NAME")
    public Field<String> meterName = new Field<>(this);

    @Column(name = "PORTFOLIO_MANAGER_EMAIL")
    public Field<String> portfolioManagerEmail = new Field<>(this);

    @Column(name = "BRANCH_MANAGER_EMAIL")
    public Field<String> branchManagerEmail = new Field<>(this);

    @Column(name = "BUILDING_NUMBER")
    public Field<String> buildingNumber = new Field<>(this);

    @Column(name = "PROPERTY_NAME")
    public Field<String> propertyName = new Field<>(this);

    public ICEMeterPortfolioView() {
        super("ICE_METER_PORTFOLIO_V");
    }
}