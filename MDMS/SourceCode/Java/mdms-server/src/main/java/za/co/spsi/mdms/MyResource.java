package za.co.spsi.mdms;

import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.util.NESConstants;
import za.co.spsi.toolkit.crud.util.VaadinVersionUtil;
import za.co.spsi.toolkit.db.DataSourceDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyResource {
    public static void main(String[] args) {

        // Create a variable for the connection string.
        String connectionUrl = "jdbc:sqlserver://192.1.0.185:1433;databaseName=NES_Core;user=mdms;password=MdMs@PEC128n#s";

        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement()) {

            String CommandHistoryID = "54a93ad0c75a4470aebbaa1f3cfe2cc4";

            String SQL = "select * from [5_3_000].CommandHistory where CommandHistoryID = '%s'";
            SQL = String.format(SQL, CommandHistoryID );

            try (ResultSet rs = stmt.executeQuery(SQL)) {

            String commandStatus = "";

            if( rs.next() != false ) {

                commandStatus = rs.getString("StatusTypeID");

                System.out.println("StatusTypeID: " + commandStatus);

                if (commandStatus.equalsIgnoreCase(NESConstants.CommandHistoryStatus.SUCCESS)) {
                    System.out.println("Command Success\n");
                } else if (commandStatus.equalsIgnoreCase(NESConstants.CommandHistoryStatus.WAITING) || commandStatus.equalsIgnoreCase(NESConstants.CommandHistoryStatus.IN_PROGRESS)) {
                    System.out.println("Command In Progress\n");
                } else if (commandStatus.equalsIgnoreCase(NESConstants.CommandHistoryStatus.FAILURE)) {
                    System.out.println("Command Failed\n");
                }
            }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
