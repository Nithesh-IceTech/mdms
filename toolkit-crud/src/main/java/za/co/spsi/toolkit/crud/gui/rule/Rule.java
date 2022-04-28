package za.co.spsi.toolkit.crud.gui.rule;


import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.ObjectUtils;

import java.lang.reflect.Method;


/**
 * Created by jaspervdb on 4/6/16.
 * Must have a annotated @ValidateRule method
 */
public abstract class Rule<E extends Layout> {

    protected E entity;
    protected String code;
    protected LField focusField;
    protected Field field[];

    public Rule(E entity, String code, LField focusField, Field... field) {
        this.entity = entity;
        this.code = code;
        this.focusField = focusField;
        this.field = field;
        entity.addBusinessRule(this);
    }

    public void setFocusField(LField focusField) {
        this.focusField = focusField;
    }

    public Layout getLayout() {
        return entity;
    }

    public String getMessage() {
        return entity.getLookupServiceHelper().executeLookupCodeRequest(
                "MESSAGE",code, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId()).getDescription();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public final String validateRule() {
        try {
            for (Method m : ObjectUtils.findMethodsByAnnotation(getClass(), ValidateRule.class)) {
                Assert.isTrue(m.getParameterTypes().length == 0, "@ValidateRule method may not contain any arguments");
                String msg = (String) m.invoke(this, (Object[]) null);
                if (msg != null) {
                    if (focusField != null) {
                        focusField.setFocus();
                    }
                    return msg;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
