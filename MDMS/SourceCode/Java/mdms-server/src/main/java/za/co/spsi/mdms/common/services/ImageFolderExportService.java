package za.co.spsi.mdms.common.services;

import jcifs.smb.NtlmPasswordAuthentication;
import org.apache.commons.lang3.StringUtils;
import za.co.spsi.mdms.common.dao.MdmsExportPhotoDocument;
import za.co.spsi.mdms.common.db.ExportPhotosMDMSView;
import za.co.spsi.mdms.common.db.ExportPhotosUtilView;
import za.co.spsi.toolkit.crud.util.FileUtil;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn("MDMSUpgradeService")
public class ImageFolderExportService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private DataSource iceDataSource;

    @Inject
    @ConfValue(value = "imageExportProcessorEnabled", folder = "server")
    private boolean processEnabled = false;

    @Inject
    @ConfValue(value = "imageExportPath", folder = "server")
    private String imageExportPath;

    @Inject
    @ConfValue(value = "xmlExportPath", folder = "server")
    private String xmlExportPath;

    @Inject
    @ConfValue(value = "shareFolderUsername", folder = "server")
    private String shareFolderUsername;

    @Inject
    @ConfValue(value = "shareFolderPassword", folder = "server")
    private String shareFolderPassword;

    public static final Logger TAG = Logger.getLogger(ImageFolderExportService.class.getName());

    @Lock(LockType.WRITE)
    @Schedule(minute = "*", hour = "*", second = "*/120", persistent = false)
    public void atSchedule() {

        TAG.info("processEnabled : " + processEnabled + "imageExportPath : " + imageExportPath);
        if (processEnabled && !StringUtils.isEmpty(imageExportPath)) {

            TAG.info("Start schedule job : ");
            process(dataSource, iceDataSource);
        }
    }

    public void process(DataSource mdmsDataSource, DataSource iceDataSource) {
        try {
            try (Connection mdmsConnection = mdmsDataSource.getConnection();
                 Connection iceUtilConnection = iceDataSource.getConnection()) {

                mdmsConnection.setAutoCommit(false);
                process(mdmsConnection, iceUtilConnection);
            }
        } catch (SQLException e) {
            TAG.severe(e.getMessage());
        }
    }

    public String getFolderSuffix(String companyCode) {
        TAG.info("companyCode  : " + companyCode);
        if (companyCode == null)
            return "";
        if (companyCode.equalsIgnoreCase(""))
            return "";
        String result = "";
        if (companyCode.equalsIgnoreCase("AZA"))
            result = companyCode + "HF";
        else if (companyCode.equalsIgnoreCase("EZA"))
            result = companyCode + "PT";
        else
            result = companyCode + "01";

        TAG.info("getFolderSuffix  : " + result);
        return result;
    }

    /**
     * step through the batch data and process
     */
    public void process(Connection mdmsConnection, Connection iceUtilConnection) throws SQLException {
        for (ExportPhotosMDMSView exportPhotosMDMSView : new ExportPhotosMDMSView().getDataSource(mdmsConnection)) {
            String jpgImage = null;
            String xmlImage = null;

            NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(null, shareFolderUsername, shareFolderPassword);

            try {

                DataSourceDB<ExportPhotosUtilView> viewIterator =
                        new ExportPhotosUtilView().init(
                                exportPhotosMDMSView.pecMeterReadingEntity.reference_id.get()).getDataSource(iceUtilConnection);

                ExportPhotosUtilView exportPhotosUtilView = viewIterator.get();

                if (exportPhotosUtilView != null) {

                    String fileName = exportPhotosUtilView.iceCBpartner.value.getAsString() + "_" + System.currentTimeMillis();
                    String folderSuffix = getFolderSuffix(exportPhotosUtilView.adOrg.companyCode.getAsString());
                    String branchImageFolder = imageExportPath.replace("?", folderSuffix);
                    String branchXmlFolder = xmlExportPath.replace("?", folderSuffix);

                    TAG.info("branchImageFolder = " + branchImageFolder);
                    TAG.info("branchXmlFolder = " + branchXmlFolder);

                    // Check if the folders exist
                    if (!FileUtil.dirExist(authentication, branchImageFolder)) {
                        // Report folder does not exit
                        TAG.severe("The following folder does not exist : " + branchImageFolder);
                        continue;
                    }

                    if (!FileUtil.dirExist(authentication, branchXmlFolder)) {
                        // Report folder does not exit
                        TAG.severe("The following folder does not exist : " + branchXmlFolder);
                        continue;
                    }

                    jpgImage = branchImageFolder + File.separator + fileName + ".jpg";
                    xmlImage = branchXmlFolder + File.separator + fileName + ".xml";

                    TAG.info("jpgImage = " + jpgImage);
                    TAG.info("xmlImage = " + xmlImage);

                    exportPhotosMDMSView.pecMeterReadingPhotoEntity.createFileOnShare(jpgImage, authentication);
                    MdmsExportPhotoDocument docBaseDocument = new MdmsExportPhotoDocument(
                            "",
                            exportPhotosUtilView.iceCBpartner.value.getAsString(),
                            "",
                            exportPhotosUtilView.iceProperty.iceBuildingComplexName.getAsString(),
                            "",
                            "",
                            exportPhotosUtilView.iceCBpartner.value.getAsString(),
                            exportPhotosUtilView.iceCBpartner.name.getAsString(),
                            "MeterImages",
                            exportPhotosMDMSView.pecMeterEntity.meterN.get(),
                            "",
                            exportPhotosUtilView.iceCInvoice.documentNo.getAsString(),
                            exportPhotosMDMSView.pecMeterReadingEntity.readingDate.get().getTime(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            exportPhotosMDMSView.pecMeterReadingEntity.reference_id.getAsString(),
                            "");

                    docBaseDocument.createFileOnShare(xmlImage, authentication);
                    exportPhotosMDMSView.pecMeterReadingPhotoEntity.photoExported.set('Y');
                    DataSourceDB.set(mdmsConnection, exportPhotosMDMSView.pecMeterReadingPhotoEntity);
                    mdmsConnection.commit();
                }

            } catch (Throwable throwable) {
                try {
                    if (StringUtils.isNotBlank(jpgImage)) {
                        FileUtil.deleteSmbFile(authentication, jpgImage);
                    }
                } catch (Exception ex) {
                    TAG.log(Level.SEVERE, ex.getMessage(), ex);
                }

                try {
                    if (StringUtils.isNotBlank(jpgImage)) {
                        FileUtil.deleteSmbFile(authentication, xmlImage);
                    }

                } catch (Exception ex) {
                    TAG.log(Level.SEVERE, ex.getMessage(), ex);
                }

                TAG.log(Level.SEVERE, throwable.getMessage(), throwable);
                mdmsConnection.rollback();
            }
        }
    }
}
