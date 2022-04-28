package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.UIProperties;
import za.co.spsi.toolkit.crud.gui.LField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2014/10/08.
 */
public class FormPanel extends VerticalLayout {

    protected int cols = 2, cnt = 0;
    protected String caption, description;
    protected VerticalLayout formLayout = new VerticalLayout();

    private static final int margin = 4;

    public FormPanel() {
        init();
    }

    private void init() {
        setMargin(false);
        setSpacing(false);
        setHeightUndefined();
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public static void addFormHeader(VerticalLayout layout, String captionStr, String descriptionStr) {
        Label caption = new Label(captionStr.toUpperCase());
        caption.addStyleName("header");
        Label description = new Label(descriptionStr);
        description.addStyleName("description");
        HorizontalLayout header = new HorizontalLayout(caption, description);
        header.setSpacing(true);
        layout.addComponent(header);
    }

    public void initForm() {
        addStyleName("form-panel");
        setSpacing(false);

        if (!StringUtils.isEmpty(caption)) {
            addFormHeader(this,caption,description);
        }

        formLayout.addStyleName("form");
        addComponent(formLayout);
    }

    public void setFields(Group group, LField... fields) {
        UIProperties ui = group.getUI();
        // remap the columns
        cols = ui != null ? ui.getColumns() : cols;
        MultiColumnFormLayout form = new MultiColumnFormLayout(
                cols > 0 ? cols : 1, fields.length,ui.isLeftToRight());
        if (cols == -1) {
            form.setWidth("50%");
        } else {
            form.setWidth("100%");
        }
        form.setSpacing(true);
        form.setMargin(new MarginInfo(false, false, false, false));
        for (LField field : fields) {
            if (field instanceof LField.Spacer) {
                form.addComponent(new Label(""));
            } else {
                Component component = field.getComponent();
                form.addComponent(component);
            }
        }
        formLayout.setSpacing(false);
        formLayout.addComponent(form);
    }

}
