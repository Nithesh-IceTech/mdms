package za.co.spsi.toolkit.crud.entity.gui;

import com.vaadin.server.FontAwesome;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.entity.ShapeEntity;
import za.co.spsi.toolkit.crud.entity.ShapeImportEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.gis.GeoMap;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.util.Util;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.logging.Logger;


/**
 * Created by jaspervdb on 2016/04/19.
 */
@Qualifier(roles = {@Role(value = ToolkitCrudConstants.SYS_ADMIN)})
public abstract class ShapeLayout extends Layout<ShapeImportEntity> {

    public static final Logger TAG = Logger.getLogger(ShapeLayout.class.getName());

    @Inject
    private BeanManager beanManager;

    @EntityRef(main = true)
    public ShapeEntity shape = new ShapeEntity();

    public Group detail = new Group(ToolkitLocaleId.SHAPE_DETAIL, this);

    @UIField(enabled = false)
    public LField label = new LField(shape.label, ToolkitLocaleId.SHAPE_IMPORT_LABEL, this);
    @UIField(enabled = false)
    public LField area = new LField(shape.area, ToolkitLocaleId.SHAPE_IMPORT_AREA, this);
    @UIField(enabled = false)
    public LField type = new LField(shape.type, ToolkitLocaleId.SHAPE_IMPORT_TYPE, this);

    @PlaceOnToolbar
    public ActionField showInMap = new ActionField(ToolkitLocaleId.SHAPE_SHOW_ON_MAP, FontAwesome.MAP, this, source -> showInMap());


    public Group nameGroup = new Group("", this, label, area, type).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public ShapeLayout() {
        super(ToolkitLocaleId.SHAPE_IMPORT);
    }

    @Override
    public String getMainSql() {
        return "select * from shape order by label asc";
    }

    private void showInMap() {
        LLayerGroup zoneLayer = new LLayerGroup();
        LMap map = GeoMap.initMap(new LMap());
        map.addLayer(zoneLayer);
        zoneLayer.addComponent(shape.getGeo());
        map.zoomToContent();
        Util.showInWindow(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_SHOW_ON_MAP),map,"95%","95%");
    }


}
