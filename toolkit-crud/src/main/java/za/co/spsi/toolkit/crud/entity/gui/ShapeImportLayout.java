package za.co.spsi.toolkit.crud.entity.gui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.viritin.label.MLabel;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.entity.ShapeEntity;
import za.co.spsi.toolkit.crud.entity.ShapeImportEntity;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.custom.FileHelper;
import za.co.spsi.toolkit.crud.gui.fields.FileField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.gis.GeoMap;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.io.IOUtil;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Created by jaspervdb on 2016/04/19.
 */
@Qualifier(roles = {@Role(value = "Supervisor")})
public abstract class ShapeImportLayout extends Layout<ShapeImportEntity> implements FileHelper.Callback {

    public static final Logger TAG = Logger.getLogger(ShapeImportLayout.class.getName());

    @EntityRef(main = true)
    public ShapeImportEntity shapeImport = new ShapeImportEntity();

    public LField filename = new LField(shapeImport.filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this);

    public Group detail = new Group(ToolkitLocaleId.SHAPE_IMPORT, this);


    @UIField(mandatory = true)
    public LField description = new LField(shapeImport.description, ToolkitLocaleId.DATA_IMPORT_DESCRIPTION, this);
    public TextAreaField notes = new TextAreaField(shapeImport.notes, ToolkitLocaleId.DATA_IMPORT_NOTES, this);

    public UidField username = new UidField(shapeImport.username, ToolkitLocaleId.DATA_IMPORT_USERNAME, this);

    @UI(width = "-1px")
    @UIField(mandatory = true, writeOnce = true)
    public FileField file = new FileField(shapeImport.fileData, filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this).setCallback(this);

    @UIField(enabled = false)
    public LField<Timestamp> importTime = new LField<>(shapeImport.importTime, ToolkitLocaleId.DATA_IMPORT_TIME, this);

    @PlaceOnToolbar
    public ActionField showInMap = new ActionField(ToolkitLocaleId.SHAPE_SHOW_ON_MAP, FontAwesome.MAP, this, source -> showInMap());


    public ActionField process = new ActionField(ToolkitLocaleId.DATA_IMPORT_PROCESS, FontAwesome.CHECK, this, source -> process());

    public Group nameGroup = new Group("", this, description, filename, username).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public Pane shapePane = new Pane(ToolkitLocaleId.SHAPE_DETAIL, "select * from shape where shape_import_id = ? order by label asc",
            getShapeLayoutClass(), new Permission(0), this);

    public ShapeImportLayout() {
        super(ToolkitLocaleId.SHAPE_IMPORT);
    }

    public abstract Class<? extends ShapeLayout> getShapeLayoutClass();

    @Override
    public void intoControl() {
        process.getProperties().setEnabled(importTime.get() == null);
        showInMap.getProperties().setEnabled(importTime.get() != null);
        super.intoControl();
    }

    private void process() {
        if (save()) {
            processImport();
        }
    }

    private boolean validateZipFile(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            StringList filenames = new StringList();
            for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
                filenames.add(entries.nextElement().getName());
            }
            return filenames.getIndexOfAnyPartOf(".shp") != -1 && filenames.getIndexOfAnyPartOf(".shx") != -1 &&
                    filenames.getIndexOfAnyPartOf(".prj") != -1;
        } catch (Exception ex) {
            TAG.log(Level.INFO, ex.getMessage(), ex);
            return false;
        }
    }

    public static class FeatureTypeComboBox extends ComboBox {

        public FeatureTypeComboBox(String captionId, SimpleFeatureType featureType, Class<? extends AttributeType> type) {
            super(AbstractView.getLocaleValue(captionId));
            for (int i = 0; i < featureType.getAttributeCount(); i++) {
                if (type.isAssignableFrom(featureType.getTypes().get(i).getClass())) {
                    addItem(i);
                    setItemCaption(i, featureType.getDescriptor(i).getName().toString());
                }
            }
        }
    }

    class FeatureTypeLayout extends VerticalLayout {
        private SimpleFeatureType featureType;

        public FeatureTypeComboBox label, area, theGeom;

        public FeatureTypeLayout(SimpleFeatureType featureType) {
            this.featureType = featureType;
            init();
        }

        private void init() {
            setSizeUndefined();
            label = new FeatureTypeComboBox(ToolkitLocaleId.SHAPE_IMPORT_LABEL, featureType, AttributeTypeImpl.class);
            area = new FeatureTypeComboBox(ToolkitLocaleId.SHAPE_IMPORT_AREA, featureType, AttributeTypeImpl.class);
            theGeom = new FeatureTypeComboBox(ToolkitLocaleId.SHAPE_IMPORT_GEOM, featureType, GeometryType.class);
            addComponent(new MLabel(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT_MAP_HEADER)).withStyleName(ValoTheme.LABEL_H1));
            FormLayout form = new FormLayout(label, area, theGeom);
            form.setWidthUndefined();
            addComponent(form);
        }

        public boolean isOk() {
            if (label.getValue() != null && area.getValue() != null && theGeom.getValue() != null) {
                // no duplicate values
                if (label.getValue() != area.getValue()) {
                    return true;
                } else {
                    Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT_MAP_INCORRECT_VALUES), Notification.Type.ERROR_MESSAGE);
                }
            } else {
                Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT_MAP_ENTER_VALUES), Notification.Type.ERROR_MESSAGE);
            }
            return false;
        }
    }

    /**
     * remove all the non number values
     *
     * @param value
     * @return
     */
    private double getAreaValue(String value) {
        StringBuffer buffer = new StringBuffer();
        for (char c : value.toCharArray()) {
            if (c >= '0' && c <= '9' || c == '.') {
                buffer.append(c);
            }
        }
        return Double.parseDouble(buffer.toString());
    }

    private boolean processImport(FeatureSource featureSource, FeatureTypeLayout featureTypeLayout) {
        try {
            FeatureCollection collection = featureSource.getFeatures();
            FeatureIterator iterator = collection.features();

            FeatureType schema = featureSource.getSchema();
            CoordinateReferenceSystem sourceCRS = schema.getCoordinateReferenceSystem();
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

            try {
                while (iterator.hasNext()) {
                    SimpleFeatureImpl simpleFeature = (SimpleFeatureImpl) iterator.next();
                    if (simpleFeature.getDefaultGeometry() instanceof MultiPolygon) {
                        Geometry geometry = JTS.transform((com.vividsolutions.jts.geom.Geometry) simpleFeature.getDefaultGeometry(), transform);
                        ShapeEntity shapeEntity = new ShapeEntity();
                        shapeEntity.setGeo(geometry);
                        shapeEntity.label.set(simpleFeature.getAttribute((Integer) featureTypeLayout.label.getValue()).toString());
                        shapeEntity.area.set(getAreaValue(simpleFeature.getAttribute((Integer) featureTypeLayout.area.getValue()).toString()));
                        shapeEntity.shapeImportId.set(shapeImport.shapeImportId.get());
                        shapeEntity.type.set("Polygon");
                        DataSourceDB.set(getDataSource(), shapeEntity);
                    }
                }
                return true;
            } finally {
                iterator.close();
            }
        } catch (Exception ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            TAG.log(Level.INFO, ex.getMessage(), ex);
            return false;
        }
    }


    private void processImport(File folder, File shpFile) throws IOException, FactoryException {
        Map connect = new HashMap();
        connect.put("url", shpFile.toURI().toURL());

        ShapefileDataStore dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(connect);
        String[] typeNames = dataStore.getTypeNames();
        String typeName = typeNames[0];

        final FeatureSource featureSource = dataStore.getFeatureSource(typeName);

        final FeatureTypeLayout typeLayout = new FeatureTypeLayout((SimpleFeatureType) featureSource.getSchema());
        MessageBox.create().withMessage(typeLayout).withOkButton(new Runnable() {
            @Override
            public void run() {
                if (typeLayout.isOk()) {
                    // process the import
                    if (processImport(featureSource, typeLayout)) {
                        Util.getComponentParent(Window.class, typeLayout).close();
                        Arrays.asList(folder.listFiles()).stream().forEach(f -> f.delete());
                        // processed ok
                        file.getProperties().setEnabled(false);
                        process.getProperties().setEnabled(false);
                        save();
                        intoControl();
                    }
                } else {
                }
            }
        }, ButtonOption.closeOnClick(false)).withCancelButton(() -> Arrays.asList(folder.listFiles()).stream().
                forEach(f -> f.delete()), ButtonOption.closeOnClick(true)).open();

    }

    // assume its a multi sheet xls file
    private void processImport() {
        // process the shape file impprt
        try {
            File shapeFiles = IOUtil.unzipFile(file.getOrCreateFile(), true);
            File shpFile = Arrays.asList(shapeFiles.listFiles()).stream().filter(f -> f.getName().endsWith(".shp")).findFirst().get();
            Assert.isTrue(shpFile != null, "No Shape file found");
            processImport(shapeFiles, shpFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getMainSql() {
        return "select * from SHAPE_IMPORT";
    }

    public void uploadSucceeded(String filename, File file) {
        // validate the details
        if (!filename.toLowerCase().endsWith(".zip") || !validateZipFile(file)) {
            ShapeImportLayout.this.filename.set(null);
            ShapeImportLayout.this.file.set(null);
            intoControl();
            Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT_EXPECTED_ZIP),
                    AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT_ZIP_DETAIL), Notification.Type.ERROR_MESSAGE);
        } else {
            process.getProperties().setEnabled(true);
        }
    }

    public void uploadFailed(Exception reason) {
    }

    public void uploadCancelled() {
    }


    private void showInMap() {
        LLayerGroup zoneLayer = new LLayerGroup();
        LMap map = GeoMap.initMap(new LMap());
        map.addLayer(zoneLayer);
        DataSourceDB.executeInTx(getDataSource(), connection -> {
            for (ShapeEntity shape : shapeImport.shapes.get(connection)) {
                zoneLayer.addComponent(shape.getGeo());
            }
            map.zoomToContent();
            Util.showInWindow(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_SHOW_ON_MAP), map, "95%", "95%");
        });
    }

}
