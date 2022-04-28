package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.collections.CollectionUtils;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/10/25
 * Time: 11:34 AM
 * Field to contains a one to multiple relationship represented in a in a twin select column
 */
public class FieldTwinColSelect<T> extends LField {

    private Service service;
    private TwinColSelect twinColumn;
    private Map<Object, String> available;
    protected ListSelect listSelect;

    public FieldTwinColSelect(String fieldName, Service service, Layout layout) {
        super((Field) null, fieldName, layout);
        this.service = service;
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        listSelect = new ListSelect(getCaption());
        util.applyUI(listSelect, this.getUI());

        TwinColSelect twinColumn = new TwinColSelect(getCaption());
        available = service.findAll();
        for (Object key : available.keySet()) {
            twinColumn.addItem(key);
            twinColumn.setItemCaption(key, available.get(key));
        }
        util.applyUI(twinColumn, this.getUI());
        return twinColumn;
    }


    @Override
    public void intoControl() {
        super.intoControl();
        if (getProperties().isReadOnly()) {
            listSelect.removeAllItems();
            // get the string values of the codes
            List<String> descriptions = new ArrayList<>();
            for (Object description : ((Set) get()).toArray()) {
                descriptions.add(description.toString());
            }

            listSelect.addItems(service.getDescriptionForDynamicLookups(descriptions));
        }
    }

    @Override
    public void saveEvent(Connection connection) {
        Set value = (Set) twinColumn.getValue();

        Collection retained = CollectionUtils.intersection(available.keySet(), value);
        Collection added = CollectionUtils.disjunction(value, retained);
        Collection removed = CollectionUtils.disjunction(available.keySet(), retained);

        service.persistChanges(value, added, removed);
    }

    public static interface Service<T> {
        /**
         * @return all available options for the user
         */
        Map<Object, String> findAll();

        List<String> getDescriptionForDynamicLookups(List<Object> code);

        /**
         * persist all the changes
         *
         * @param removed
         */
        void persistChanges(Collection all, Collection added, Collection removed);
    }
}
