package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;

/**
 * Created by jaspervdb on 14/10/22.
 */
public class MultiColumnFormLayout extends GridLayout {
    private final int rows;
    private int componentCount;
    private boolean leftToRight;

    /**
     * A layout that uses FormLayout in multiple columns.
     * @param columns number of columns (with nested components) to display.
     * @param componentCount the pending number of nested components (or input fields).
     */
    public MultiColumnFormLayout(int columns, int componentCount, boolean leftToRight) {
        super(columns, 1);    // will make as many FormLayouts as columns were defined
        this.leftToRight = leftToRight;

        final int additionalRow = (componentCount % columns == 0) ? 0 : 1;
        this.rows = (componentCount / columns) + additionalRow;

        for (int i = 0; i < columns; i++)    {
            final FormLayout formLayout = new FormLayout();
            formLayout.addStyleName("caption-width-300");

            // use super.addComponent() because this.addComponent() is overridden to throw UnsupportedOperationException
            super.addComponent(formLayout, i, 0, i, 0);
        }
    }

    @Override
    public void addComponent(Component component) {
        int column = leftToRight?(componentCount % getColumns()):(componentCount / rows);
        componentCount++;
        final FormLayout formLayout = (FormLayout) getComponent(column, 0);
        formLayout.addComponent(component);
    }

    @Override
    public void addComponent(Component component, int column1, int row1, int column2, int row2) throws OverlapsException, OutOfBoundsException {
        throw new UnsupportedOperationException("Sorry, this is a FormLayout delegate!");
    }
}