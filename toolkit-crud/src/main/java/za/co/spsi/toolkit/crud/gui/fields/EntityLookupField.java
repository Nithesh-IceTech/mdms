package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.LFieldList;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/03/15.
 */
public class EntityLookupField<E> extends LookupField<E> {

    public EntityLookupField(Field field, String captionId, Class<? extends Layout> refLayout, Layout model) {
        super(field, captionId, null, model);
        setSql(getSql(refLayout));
    }

    private String getSql(Class<? extends Layout> refLayout) {
        try {
            Layout layout = refLayout.newInstance();
            LFieldList nameFields = layout.getGroups().getNameGroup().getFields();
            StringList fsql = nameFields.stream().filter(f -> f.getField() != null && f.getField().getEntity() != null).
                    map(f ->
                            "NVL(" + (f instanceof MLCSLookupField?((MLCSLookupField)f).getLookupColName() : EntityDB.getFullColumnName(f.getField())) + ",'') "
                    ).collect(Collectors.toCollection(StringList::new));

            FormattedSql formattedSql = new FormattedSql(layout.getMainSql());
            formattedSql.setSelect(EntityDB.getFullColumnName(layout.getMainEntity().getSingleId())+ "," + fsql.toString(" || '-' || "));
            formattedSql.setFrom(formattedSql.getFrom() +
                    nameFields.getFieldsOfType(MLCSLookupField.class).stream().map(f -> ((MLCSLookupField) f).getLeftJoinSql())
                            .collect(Collectors.toCollection(StringList::new)).toString("\n"));
            return formattedSql.toString().replace("_AGENCY_ID_", ToolkitCrudConstants.getChildAgencyId().toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
