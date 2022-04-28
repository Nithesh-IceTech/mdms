package za.co.spsi.openmucdoa.entities.dbviews;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SignalDataViewRowMapper implements RowMapper<SignalDataViewEntity> {

    @Override
    public SignalDataViewEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        SignalDataViewEntity signalDataViewEntity = new SignalDataViewEntity();

        signalDataViewEntity.setEntrytime(rs.getTimestamp("time"));
        signalDataViewEntity.setIedName(rs.getString("ied_name"));
        signalDataViewEntity.setChannelName(rs.getString("channel_name"));
        signalDataViewEntity.setChannelDescription(rs.getString("channel_description"));
        signalDataViewEntity.setDoubleValue(rs.getDouble("double_value"));

        return signalDataViewEntity;
    }

}
