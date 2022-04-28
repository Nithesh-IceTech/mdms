package za.co.spsi.toolkit.crud.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.entity.*;
import za.co.spsi.toolkit.util.Assert;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jaspervdb on 2016/08/18.
 */
public class DataSourceXls<E extends Entity> implements DataSource<E>, Closeable {

    private E entity;
    private POIFSFileSystem poiIs;
    private HSSFWorkbook workBook;
    private HSSFSheet sheet;

    private CreationHelper createHelper;
    private OutputStream os;
    private FieldList fields;

    private List<Field> fieldMap = null;
    private int entityIDindexInFieldMap;
    private HSSFRow row;
    private int rowNo = 0;
    private boolean keepHistory = false;

    private static String DATE_FORMAT = "m/d/yy", TIME_FORMAT = "h:mm", DATETIME_FORMAT = "m/d/yy h:mm";
    private CellStyle dateStyle, timeStyle, dateTimeStyle;

    public DataSourceXls() {
    }

    public DataSourceXls(E entity) {
        this.entity = entity;
    }

    private CreationHelper getCreationHelper(Workbook workbook) {
        if (createHelper == null) {
            createHelper = workBook.getCreationHelper();
        }
        return createHelper;
    }

    private CellStyle getDateFieldStyle(Workbook workbook, String format) {
        CellStyle dateStyle = workBook.createCellStyle();
        dateStyle.setDataFormat(getCreationHelper(workbook).createDataFormat().getFormat(format));
        return dateStyle;
    }

    private CellStyle getDateStyle(Workbook workbook) {
        dateStyle = dateStyle == null ? getDateFieldStyle(workbook, DATE_FORMAT) : dateStyle;
        return dateStyle;
    }

    private CellStyle getTimeStyle(Workbook workbook) {
        timeStyle = timeStyle == null ? getDateFieldStyle(workbook, TIME_FORMAT) : timeStyle;
        return timeStyle;
    }

    private CellStyle getDateTimeStyle(Workbook workbook) {
        dateTimeStyle = dateTimeStyle == null ? getDateFieldStyle(workbook, DATETIME_FORMAT) : dateTimeStyle;
        return dateTimeStyle;
    }

    public void init(InputStream is, String sheetName) throws IOException {
        this.poiIs = new POIFSFileSystem(is);
        workBook = new HSSFWorkbook(poiIs);
        sheet = workBook.getSheet(sheetName);
    }

    public void init(OutputStream os, FieldList fields, String sheetName) throws IOException {
        this.os = os;
        this.fields = fields;
        workBook = new HSSFWorkbook();
        createHelper = workBook.getCreationHelper();
        sheet = workBook.createSheet(sheetName);
    }

    public void init(OutputStream os, String sheetName) throws IOException {
        init(os, null, sheetName);
    }

    private HSSFCellStyle getMandatoryStyle() {
        HSSFCellStyle mandatoryCs = workBook.createCellStyle();

        HSSFFont mandatoryF = workBook.createFont();
        mandatoryCs.setFont(mandatoryF);
        mandatoryF.setFontHeightInPoints((short) 12);
        mandatoryF.setBold(true);
        mandatoryF.setColor((short) 0xA);
        return mandatoryCs;
    }

    private HSSFCellStyle getNonMandatoryStyle() {
        HSSFCellStyle nonMandatoryCs = workBook.createCellStyle();
        HSSFFont nonMandatoryF = workBook.createFont();
        nonMandatoryCs.setFont(nonMandatoryF);
        nonMandatoryF.setFontHeightInPoints((short) 12);
        nonMandatoryF.setBold(true);
        nonMandatoryF.setColor((short) 0x0);
        return nonMandatoryCs;
    }

    public void init(OutputStream os) throws IOException {
        init(os, entity.getFields(), entity.getName());
    }

    public void writeHeaders(ResultSetMetaData meta) throws IOException, SQLException {
        Row row = sheet.createRow(rowNo++);

        HSSFCellStyle style = getNonMandatoryStyle();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(meta.getColumnName(i + 1));
            cell.setCellStyle(style);
        }
    }

    public void writeHeaders() throws IOException {
        HSSFCellStyle mandatoryCs = getMandatoryStyle();
        HSSFCellStyle nonMandatoryCs = getNonMandatoryStyle();

        Row row = sheet.createRow(rowNo++);
        // Create a cell and put a value in it.
        int x = 0;
        for (Field field : fields) {
            Cell cell = row.createCell(x++);
            cell.setCellValue(field.getName());
            Column column = (Column) field.getAnnotation(Column.class);
            Id id = (Id) field.getAnnotation(Id.class);
            cell.setCellStyle((id != null || (column != null && column.notNull())) ? mandatoryCs : nonMandatoryCs);
        }
        for (int i = 0; i < fields.size(); i++) {
            sheet.autoSizeColumn(i);
        }

    }

    public void init(InputStream is) throws IOException {
        init(is, entity.getName());
        getMap();
    }

    /*
      We get hold of the column number in the spreadsheet representing the id of the entity.
      We will use this id when iterating through the spreadsheet rows to check for nulls signifying end of spreadsheet.
      sheet.getPhysicalNumberOfRows() is not always reliable.
      As per the documentation for getPhysicalNumberOfRows() of HSSFSheet:
      Returns the number of physically defined rows (*NOT* the number of rows in the sheet)
     */
    private int getEntityIDIndexFromFieldMap() {
        FieldList idFieldList = entity.getFields().getAnnotation(Id.class);
        if (idFieldList == null || idFieldList.size() == 0)
            return -1;
        Field fieldToUse = null;
        for (Field field : idFieldList) {
            Id idAnnotation = (Id) field.getAnnotation(Id.class);
            if (idAnnotation.uuid()) {
                fieldToUse = field;
                break;
            }
        }
        if (fieldToUse == null)
            return -1;
        return fieldMap.indexOf(fieldToUse);
    }

    private List<Field> getMap() {
        if (fieldMap == null) {
            fieldMap = new ArrayList<>();
            row = sheet.getRow(rowNo);
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                int index = entity.getFields().getNames().indexOfIgnoreCase(row.getCell(i).getStringCellValue());
                Assert.isTrue(index != -1, "No Field found by name %s. row %s", row.getCell(i).getStringCellValue(), "" + row);
                fieldMap.add(entity.getFields().get(index));
            }
            entityIDindexInFieldMap = getEntityIDIndexFromFieldMap();
            rowNo++;
        }
        return fieldMap;
    }

    @Override
    public Iterator<E> iterator() {
        row = sheet.getRow(rowNo);
        return this;
    }

    @Override
    public boolean hasNext() {
        return (rowNo < sheet.getPhysicalNumberOfRows()) && !(isIdCellNull());
    }

    private void setCell(Field field, Cell cell) {
        setCell(field.getType(), field.get(), field.getSerial(), cell);
    }

    private void setCell(Class type, Object value, String serialValue, Cell cell) {
        if (value != null) {
            if (Date.class.isAssignableFrom(type)) {
                cell.setCellValue((Date) value);
                cell.setCellStyle(java.sql.Date.class.isAssignableFrom(type) ? getDateStyle(workBook) :
                        java.sql.Time.class.isAssignableFrom(type) ? getTimeStyle(workBook) :
                                getDateTimeStyle(workBook));
            } else if (Double.class.equals(type)) {
                cell.setCellValue((Double) value);
            } else if (Float.class.equals(type)) {
                cell.setCellValue((Float) value);
            } else if (Boolean.class.equals(type)) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue(serialValue);
            }
        }
    }

    private void setField(Field field, Cell cell) {
        if (cell != null) {
            if (Date.class.isAssignableFrom(field.getType())) {
                field.setSerial(cell.getDateCellValue() != null ? cell.getDateCellValue().getTime() + "" : null);
            } else if (Number.class.isAssignableFrom(field.getType())) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                field.setSerial("" + cell.getStringCellValue());
            } else {
                field.setSerial(cell.getStringCellValue());
            }
        } else {
            field.set(null);
        }
    }

    public boolean isIdCellNull() {
        //if we couldn't determine id column in the first place just return true so that this check doesn't have a influence
        if (entityIDindexInFieldMap == -1)
            return true;

        HSSFRow tempRow = sheet.getRow(rowNo);
        HSSFCell tempCell = tempRow.getCell(entityIDindexInFieldMap);

        return (tempCell == null);
    }

    public E get() {
        // read the current row
        if ((rowNo < sheet.getPhysicalNumberOfRows()) && !isIdCellNull()) {
            row = sheet.getRow(rowNo);
            for (int i = 0; i < fieldMap.size(); i++) {
                try {
                    setField(getMap().get(i), row.getCell(i));
                } catch (RuntimeException re) {
                    throw new RuntimeException(String.format("Error occurred on Field %s. Value %s: %s", getMap().get(i).toString(),
                            row.getCell(i), re.getMessage()), re);
                }
            }
            return keepHistory ? (E) entity.clone() : entity;
        }
        return null;
    }

    public void set(E entity) {
        HSSFRow row = sheet.createRow(rowNo++);
        int x = 0;
        for (Field field : entity.getFields().filterByName(fields)) {
            Cell cell = row.createCell(x++);
            try {
                setCell(field, cell);
            } catch (RuntimeException re) {
                throw new RuntimeException(String.format("Error occurred on Field %s. Value %s: %s", field.toString(),
                        field.getAsString(), re.getMessage()), re);

            }
        }
    }

    public void set(ResultSet rs) throws SQLException {
        HSSFRow row = sheet.createRow(rowNo++);
        int x = 0;
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            Cell cell = row.createCell(x++);
            try {
                Class type = Class.forName(rs.getMetaData().getColumnClassName(i + 1));
                setCell(type, rs.getObject(i + 1), rs.getObject(i + 1) != null ? rs.getObject(i + 1).toString() : "", cell);
            } catch (ClassNotFoundException re) {
                throw new RuntimeException(re.getMessage(), re);

            }
        }
    }

    @Override
    public E next() {
        E entity = get();
        rowNo++;
        return entity;
    }

    public static void exportTemplate(String sheetName, Entity entity, FieldList fields, OutputStream os) throws IOException {
        try (DataSourceXls dataSourceXls = new DataSourceXls(entity)) {
            dataSourceXls.init(os, fields, sheetName);
            dataSourceXls.writeHeaders();
        }
    }

    public static void exportTemplate(Entity entity, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            exportTemplate(entity.getName(), entity, entity.getFields(), fos);
        }
    }

    public static File exportTemplate(Entity entity, String filename) throws IOException {
        File file = File.createTempFile(filename, ".xls");
        file.deleteOnExit();
        exportTemplate(entity, file);
        return file;
    }

    public static void export(ResultSet rs, File file, String sheetName) throws IOException, SQLException {
        boolean wroteHeaders = false;
        try (FileOutputStream os = new FileOutputStream(file)) {
            try (DataSourceXls dataSourceXls = new DataSourceXls()) {
                dataSourceXls.init(os, sheetName);
                while (rs.next()) {
                    if (!wroteHeaders) {
                        wroteHeaders = true;
                        dataSourceXls.writeHeaders(rs.getMetaData());
                    }
                    dataSourceXls.set(rs);
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (os != null) {
            workBook.write(os);
        }
        workBook.close();
        if (poiIs != null) {
            poiIs.close();
        }
        if (os != null) {
            os.close();
        }
    }

    public List<E> getAllAsList() {
        keepHistory = true;
        List<E> entities = new ArrayList<E>();
        for (E entity : this) {
            entities.add(entity);
        }
        return entities;
    }


    public static void main(String args[]) throws Exception {
        DataSourceXls<Person> xls = new DataSourceXls<>(new Person());
        xls.init(new FileInputStream("/home/jaspervdb/tmp/out.xls"));
        for (Person p : xls) {
            System.out.println(p.getFields());
        }
//        exportTemplate(new Person(),true,new File("/home/jaspervdb/tmp/out.xls"));
    }

    @Exportable(name = "Persons")
    public static class Person extends Entity {

        @Exportable(name = "Person Name")
        public Field<String> names = new Field<>(this);

        @Exportable(name = "Date of Birth")
        public Field<java.sql.Date> dob = new Field<>(this);

        public Field<Integer> age = new Field<>(this);
    }
}
