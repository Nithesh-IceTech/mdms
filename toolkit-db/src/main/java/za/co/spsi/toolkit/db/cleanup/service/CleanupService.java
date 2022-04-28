package za.co.spsi.toolkit.db.cleanup.service;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.cleanup.entity.CleanupLogEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * Created by ettienne on 2017/02/06.
 */
public class CleanupService {

    public static final Logger TAG = Logger.getLogger(CleanupService.class.getName());

    public void doCleanup(String resourceFile, Connection connection) throws IOException, SQLException {
    
        StringBuilder allStatements = new StringBuilder();
        
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceFile)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
    
            while ((line = reader.readLine()) != null) {
                // Build statements
                if (line.startsWith("//") == false) {
                    allStatements.append(line + "\n");
                }
            }
        }

        String[] statements = allStatements.toString().split(";");

        CleanupLogEntity cleanupLogEntity = new CleanupLogEntity();

        for (String item : statements) {

            item = item.trim();

            if (item.trim().length() > 0) {
                TAG.info("Executing cleanup statement : " + item);
                Long startTimeTime = System.currentTimeMillis();
                int rowsUpdated = DataSourceDB.executeUpdate(connection, item, null);
                Long endTimeTime = System.currentTimeMillis();

                cleanupLogEntity = new CleanupLogEntity();
                cleanupLogEntity.startTime.set(new Timestamp(startTimeTime));
                cleanupLogEntity.endTime.set(new Timestamp(endTimeTime));
                cleanupLogEntity.statement.set(item.length() > 1024 ? item.substring(0, 1024) : item);
                cleanupLogEntity.durationMillisecond.set((int) (endTimeTime - startTimeTime));
                cleanupLogEntity.rowCount.set(rowsUpdated);

                DataSourceDB.set(connection, cleanupLogEntity);
            }
        }
    }
}


