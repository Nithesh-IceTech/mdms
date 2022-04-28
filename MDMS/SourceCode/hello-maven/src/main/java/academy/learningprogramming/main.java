package academy.learningprogramming;

import javax.swing.text.html.HTML;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.logging.Logger;

public class main {

    public static final Logger TAG = Logger.getLogger(main.class.getName());

    public static void main(String[] args) throws JAXBException {

//        String registers [] = {"1.1.1.8.0.255" ,"1.1.2.8.0.255" ,"1.1.3.8.0.255" ,"1.1.4.8.0.255"};

        String registers [] = {"1.1.5.8.0.255" ,"1.1.6.8.0.255" ,"1.1.7.8.0.255" ,"1.1.8.8.0.255"};

        if( Arrays.asList(registers).stream().anyMatch( reg -> reg.matches("1.1.[1,2,3,4].8.0.255") ) ) {
            TAG.info("Register matched !");
        } else {
            TAG.info("No registers matched.");
        }

//        KamstrupAutoCollectionAPI autoCollectionAPI = new KamstrupAutoCollectionAPI();
//
//        String results = autoCollectionAPI.restGet("http://192.1.0.181/UtiliDriver/api",String.class,
//                "/collection/meters/4B414D000000013940B0/readings/");
//
//        System.out.println( String.format("API Results: \n%s\n", results) );

//        File file = new File( "resultEntries.xml" );
//        JAXBContext jaxbContext = JAXBContext.newInstance( ResultEntries.class );
//
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//        ResultEntries resultEntries = (ResultEntries) jaxbUnmarshaller.unmarshal( file );
//        System.out.println( resultEntries );

//        String tableName = "PEC_EMPLOYEE_FILTER_TEST_TABLE_XXX_YYY_ZZZ";
//
//        GenerateTriggerName generateTriggerName = new GenerateTriggerName();
//
//        String sqlTriggerName = generateTriggerName.getManageTriggerOnUpdateDSql(tableName.toLowerCase(), true);
//
//        System.out.println(String.format("Trigger Name SQL: %s", sqlTriggerName));

//        String current_field_name = "rmsL1C";
//        String voltage_field_name = "rmsL1V";
//
//        if( current_field_name.matches("rmsL.*V") ) {
//            System.out.println("Current Field Name Matched Regex Pattern");
//        } else {
//            System.out.println("Current Field Name Didn't Match Regex Pattern");
//        }
//
//        if( voltage_field_name.matches("rmsL.*V") ) {
//            System.out.println("Voltage Field Name Matched Regex Pattern");
//        } else {
//            System.out.println("Voltage Field Name Didn't Match Regex Pattern");
//        }

//        AdjustTimestampService timestampService = new AdjustTimestampService();
//
//        Timestamp testTS = new Timestamp(System.currentTimeMillis());
//
//        Timestamp adjTestTS = timestampService.adjustTimestamp( 30, testTS );
//
//        Timestamp ts1 = Timestamp.valueOf(LocalDateTime.now());
//
//        LocalDateTime ts2 = LocalDateTime.ofEpochSecond(testTS.getTime()/1000, 0,
//                ZoneOffset.ofHours(Integer.parseInt("0")/60) )
//                .truncatedTo(ChronoUnit.MINUTES);
//
//        Timestamp ts3 = Timestamp.valueOf(ts2);
//
//        System.out.println(String.format("%s".format(adjTestTS.toString())));

    }

}
