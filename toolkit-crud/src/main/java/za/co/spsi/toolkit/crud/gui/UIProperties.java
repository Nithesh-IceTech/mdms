package za.co.spsi.toolkit.crud.gui;


import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.util.StringUtils;

/**
 * Created by jaspervdb on 14/11/19.
 */
public class UIProperties {

    private String caption,description,mask;
    private int columns = 0;
    boolean leftToRight = false;
    private int rows = 1;
    private String width = "100%";
    private String height = "";

    // disable this component
    private boolean enabled = true;

    public UIProperties() {
    }

    public UIProperties(UI ui) {
        init(ui);
    }

    public void init(UI ui) {
        this.caption = !StringUtils.isEmpty(ui.captionId())?ui.captionId():caption;
        this.columns = ui.columns()>0?ui.columns():columns;
        this.leftToRight = ui.leftToRight()?ui.leftToRight():leftToRight;
        this.rows = ui.rows()!=1?ui.rows():rows;
        this.width = !ui.width().equals("100%")?ui.width():width;
        this.height = ui.height();
        this.enabled = ui.enabled();
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isLeftToRight() {
        return leftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

