package za.co.spsi.mdms.common.dao;

import jcifs.smb.NtlmPasswordAuthentication;
import za.co.spsi.toolkit.crud.util.FileUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ettienne on 2017/05/25.
 */
@XmlRootElement(name = "DocBaseDocument")
public class MdmsExportPhotoDocument {

    private String documentNo;
    private String siteNo;
    private String buildingName;
    private String costCentre;
    private String ticketNo;
    private String customerNo;
    private String customerName;
    private String documentCategory;
    private String electricMeterNo;
    private String waterMeterNo;
    private String utilityBillNo;
    private Long readingDate;
    private String portfolio;
    private String revenueType;
    private String feeBased;
    private String munic;
    private String contractNo;
    private String unitOwner;
    private String gasMeterNo;
    private String solarMeterNo;
    private String virtualMeterNo;
    private String pECCheckerMeter;
    private String readingCycle;
    private String meterReadingID;
    private String bulks;

    public MdmsExportPhotoDocument(String documentId, String documentNo, String siteNo, String buildingName,
                                   String costCentre, String ticketNo, String customerNo, String customerName,
                                   String documentCategory, String electricMeterNo, String waterMeterNo,
                                   String utilityBillNo, Long readingDate, String portfolio, String revenueType,
                                   String feeBased, String munic, String contractNo, String unitOwner, String gasMeterNo,
                                   String solarMeterNo, String virtualMeterNo, String pECCheckerMeter,
                                   String readingCycle, String meterReadingID, String bulks) {
        this.documentNo = documentNo;
        this.siteNo = siteNo;
        this.buildingName = buildingName;
        this.costCentre = costCentre;
        this.ticketNo = ticketNo;
        this.customerNo = customerNo;
        this.customerName = customerName;
        this.documentCategory = documentCategory;
        this.electricMeterNo = electricMeterNo;
        this.waterMeterNo = waterMeterNo;
        this.utilityBillNo = utilityBillNo;
        this.readingDate = readingDate;
        this.portfolio = portfolio;
        this.revenueType = revenueType;
        this.feeBased = feeBased;
        this.munic = munic;
        this.contractNo = contractNo;
        this.unitOwner = unitOwner;
        this.gasMeterNo = gasMeterNo;
        this.solarMeterNo = solarMeterNo;
        this.virtualMeterNo = virtualMeterNo;
        this.pECCheckerMeter = pECCheckerMeter;
        this.readingCycle = readingCycle;
        this.meterReadingID = meterReadingID;
        this.bulks = bulks;

    }

    public MdmsExportPhotoDocument() {
    }


    @XmlElement(name = "DocumentNo")
    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    @XmlElement(name = "SiteNo")
    public String getSiteNo() {
        return siteNo;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    @XmlElement(name = "BuildingName")
    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    @XmlElement(name = "CostCentre")
    public String getCostCentre() {
        return costCentre;
    }

    public void setCostCentre(String costCentre) {
        this.costCentre = costCentre;
    }

    @XmlElement(name = "TicketNo")
    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    @XmlElement(name = "CustomerNo")
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    @XmlElement(name = "CustomerName")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @XmlElement(name = "DocumentCategory")
    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    @XmlElement(name = "ElectricMeterNo")
    public String getElectricMeterNo() {
        return electricMeterNo;
    }

    public void setElectricMeterNo(String electricMeterNo) {
        this.electricMeterNo = electricMeterNo;
    }

    @XmlElement(name = "WaterMeterNo")
    public String getWaterMeterNo() {
        return waterMeterNo;
    }

    public void setWaterMeterNo(String waterMeterNo) {
        this.waterMeterNo = waterMeterNo;
    }

    @XmlElement(name = "UtilityBillNo")
    public String getUtilityBillNo() {
        return utilityBillNo;
    }

    public void setUtilityBillNo(String utilityBillNo) {
        this.utilityBillNo = utilityBillNo;
    }

    @XmlElement(name = "ReadingDate")
    public Long getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Long readingDate) {
        this.readingDate = readingDate;
    }

    @XmlElement(name = "Portfolio")
    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    @XmlElement(name = "RevenueType")
    public String getRevenueType() {
        return revenueType;
    }

    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
    }

    @XmlElement(name = "FeeBased")
    public String getFeeBased() {
        return feeBased;
    }

    public void setFeeBased(String feeBased) {
        this.feeBased = feeBased;
    }

    @XmlElement(name = "Munic")
    public String getMunic() {
        return munic;
    }

    public void setMunic(String munic) {
        this.munic = munic;
    }

    @XmlElement(name = "ContractNo")
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @XmlElement(name = "UnitOwner")
    public String getUnitOwner() {
        return unitOwner;
    }

    public void setUnitOwner(String unitOwner) {
        this.unitOwner = unitOwner;
    }

    @XmlElement(name = "GasMeterNo")
    public String getGasMeterNo() {
        return gasMeterNo;
    }

    public void setGasMeterNo(String gasMeterNo) {
        this.gasMeterNo = gasMeterNo;
    }

    @XmlElement(name = "SolarMeterNo")
    public String getSolarMeterNo() {
        return solarMeterNo;
    }

    public void setSolarMeterNo(String solarMeterNo) {
        this.solarMeterNo = solarMeterNo;
    }

    @XmlElement(name = "VirtualMeterNo")
    public String getVirtualMeterNo() {
        return virtualMeterNo;
    }

    public void setVirtualMeterNo(String virtualMeterNo) {
        this.virtualMeterNo = virtualMeterNo;
    }

    @XmlElement(name = "PECCheckerMeter")
    public String getpECCheckerMeter() {
        return pECCheckerMeter;
    }

    public void setpECCheckerMeter(String pECCheckerMeter) {
        this.pECCheckerMeter = pECCheckerMeter;
    }

    @XmlElement(name = "ReadingCycle")
    public String getReadingCycle() {
        return readingCycle;
    }

    public void setReadingCycle(String readingCycle) {
        this.readingCycle = readingCycle;
    }

    @XmlElement(name = "MeterReadingID")
    public String getMeterReadingID() {
        return meterReadingID;
    }

    public void setMeterReadingID(String meterReadingID) {
        this.meterReadingID = meterReadingID;
    }

    @XmlElement(name = "Bulks")
    public String getBulks() {
        return bulks;
    }

    public void setBulks(String bulks) {
        this.bulks = bulks;
    }


    public void createFileOnShare(String fileName, NtlmPasswordAuthentication authentication) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MdmsExportPhotoDocument.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(this, byteArrayOutputStream);
        FileUtil.writeSmbFile(authentication, fileName, byteArrayOutputStream.toByteArray());
    }
}