package za.co.spsi.toolkit.crud.gui;

/**
 * Created by jaspervdb on 2014/05/15.
 */


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import za.co.spsi.toolkit.util.MaskId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/12/03
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicFieldBehaviour implements ValueChangeListener {

    private LField myField;
    private LField fields[] = new LField[]{};
    private Group groups[] = new Group[]{};
    // actions on compare values
    private String compareValues[];
    private boolean testEquals;
    private List<Action> actions = new ArrayList<Action>();
    private List<LField> defaultsMap = new ArrayList<>();
    private boolean executeOnConstruction = true;
    private MaskId maskId = null;

    enum CMD {
        NOT_SET, SET_MANDATORY, SET_VISIBLE, SET_DISAPPEARED, SET_ENABLED, SET_CLEARED, CALLBACK;
    }

    public DynamicFieldBehaviour(LField field) {
        this.myField = field;
    }

    public static class Action {
        public CMD cmd;
        public boolean value = true;
        private Runnable callback;

        public Action(CMD cmd, boolean value) {
            this.cmd = cmd;
            this.value = value;
        }

        public Action(Runnable callback) {
            cmd = CMD.CALLBACK;
            this.callback = callback;
        }
    }

    public DynamicFieldBehaviour setFields(LField... fields) {
        this.fields = fields;
        return this;
    }

    public DynamicFieldBehaviour setDefaultValueFields(LField... fields) {
        for (LField field : fields) {
            defaultsMap.add(field);
        }
        return this;
    }

    public DynamicFieldBehaviour setGroups(Group... groups) {
        this.groups = groups;
        return this;
    }

    public DynamicFieldBehaviour setMandatory(boolean value) {
        actions.add(new Action(CMD.SET_MANDATORY, value));
        return this;
    }

    public DynamicFieldBehaviour setMandatory() {
        return setMandatory(true);
    }

    public DynamicFieldBehaviour setVisible(boolean visible) {
        actions.add(new Action(CMD.SET_VISIBLE, visible));
        return this;
    }

    public DynamicFieldBehaviour callback(Runnable action) {
        actions.add(new Action(action));
        return this;
    }

    public DynamicFieldBehaviour setDisappeared(boolean visible) {
        actions.add(new Action(CMD.SET_DISAPPEARED, visible));
        return this;
    }

    public DynamicFieldBehaviour setVisible() {
        return setVisible(true);
    }

    public DynamicFieldBehaviour setEnabled() {
        actions.add(new Action(CMD.SET_ENABLED, true));
        return this;
    }

    public DynamicFieldBehaviour setDisabled() {
        actions.add(new Action(CMD.SET_ENABLED, false));
        return this;
    }

    public DynamicFieldBehaviour clear() {
        actions.add(new Action(CMD.SET_CLEARED, true));
        return this;
    }

    public boolean isExecuteOnConstruction() {
        return executeOnConstruction;
    }

    public DynamicFieldBehaviour setExecuteOnConstruction(boolean executeOnConstruction) {
        this.executeOnConstruction = executeOnConstruction;
        return this;
    }

    public DynamicFieldBehaviour addCompare(String... compareValues) {
        return addCompare(true, compareValues);
    }

    public DynamicFieldBehaviour addCompare(boolean testEquals, String... compareValues) {
        this.testEquals = testEquals;
        this.compareValues = compareValues;
        return this;
    }

    public MaskId getMaskId() {
        return maskId;
    }

    public void setMaskId(MaskId maskId) {
        this.maskId = maskId;
    }

    private void applyVisibility(boolean visible) {
        for (LField field : fields) {
            if (field.getComponent() != null) {
                field.getProperties().setVisible(visible);
            }
        }
        for (Group group : groups) {
            if (group.getComponent() != null) {
                group.getComponent().setVisible(visible);
            }
        }
    }

    private void applyMask() {

        if (maskId == null) {
            return;
        }
        for (LField field : fields) {
            if (field.getComponent() != null) {
                field.getProperties().setMaskId(maskId);
                field.applyProperties();
            }
        }
    }

    private void validate(Action action) {
        if (action.cmd == CMD.SET_VISIBLE) {
            Assert.notNull(compareValues, String.format("CMD Visible requires compare values to be set on field %s", myField.getCaption()));
        }
        if (action.cmd == CMD.SET_DISAPPEARED) {
            Assert.notNull(compareValues, String.format("CMD Disappeared requires compare values to be set on field %s", myField.getCaption()));
        }
        if (action.cmd == CMD.SET_MANDATORY) {
            Assert.notNull(fields, String.format("Fields must be set if mandatory is enabled dynamic behaviour for %s", myField.getCaption()));
        }
        if (action.cmd == CMD.SET_ENABLED) {
            Assert.notNull(compareValues, String.format("CMD Enabled requires compare values to be set on field %s", myField.getCaption()));
        }
        if (action.cmd == CMD.SET_CLEARED) {
            Assert.notNull(compareValues, String.format("CMD Cleared requires compare values to be set on field %s", myField.getCaption()));
        }
    }

    @Override
    public void valueChanged(LField srcField,com.vaadin.ui.Field field, boolean inConstruction,boolean valueIsNull) {

        if (!inConstruction || executeOnConstruction) {
            myField.intoBindingsWithNoValidation();

            boolean match = !testEquals;

            if (compareValues == null) {
                match = !StringUtils.isEmpty(myField.getField().getAsString());
            } else {
                for (String value : compareValues) {

                    if (testEquals) {
                        match = match || value.equals(myField.getField().getAsString());
                    } else {
                        match = match && !value.equals(myField.getField().getAsString());
                    }
                }
            }

            if (match) {
                applyMask();
            }

            for (Action action : actions) {
                validate(action);
                if (action.cmd == CMD.SET_VISIBLE) {
                    applyVisibility(match ? (action.value ) : !action.value);
                }
                if (action.cmd == CMD.SET_DISAPPEARED) {
                    applyVisibility(match ? (!action.value ) : action.value);
                }
                if (action.callback != null && match) {
                    action.callback.run();
                }
            }

            for (LField item : defaultsMap) {

                if (StringUtils.isEmpty(item.getField().getSerial())) {
                    item.getField().set(item.getProperties().getDefaultValue());
                    item.getField().reset();
                    item.intoControl();
                }
            }

            applyFieldCmd(fields, match);

            for (Group group : groups) {
                applyFieldCmd(group.getFields().toArray(new LField[group.getFields().size()]), match);
            }
        }
    }

    private void applyFieldCmd(LField fields[], boolean match) {

        for (LField field : fields) {
            // when clearing fields, no change events validation messages should be displayed
            boolean inConstruction = field.getLayout().isBusyConstructing();
            field.getLayout().setBusyConstructing(true);
            try {
                field.markState();
                for (Action action : actions) {
                    if (action.cmd == CMD.SET_MANDATORY) {
                        field.getProperties().setMandatory(match ? action.value : !action.value);
                    }
                    if (action.cmd == CMD.SET_ENABLED) {
                        field.getProperties().setReadOnly(!(match ? action.value : !action.value));
                    }
                    if (action.cmd == CMD.SET_CLEARED && match) {
                        field.getField().set(null);
                        field.intoControl();
                    }
                }
                field.getProperties().applyProperties(field.getVaadinField(), field);
            } finally {
                field.resetState();
                field.getLayout().setBusyConstructing(inConstruction);
            }
        }
    }

    public void removeAction(CMD... list) {
        for (CMD cmd : list) {
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).cmd == cmd) {
                    actions.remove(i--);
                }
            }
        }
    }
}
