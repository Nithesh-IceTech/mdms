package za.co.spsi.toolkit.crud.locale;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2016/10/20.
 */
@Dependent
public class FormattingLocale {


    @Inject
    @ConfValue("decimal_format")
    private String decimalFormat;

    @Inject
    @ConfValue("decimal_separator")
    private String decimalSeparator;

    @Inject
    @ConfValue("grouping_separator")
    private String groupingSeparator;

    @PostConstruct
    public void init() {
        ToolkitCrudConstants.setDecimalFormat(decimalFormat);
        ToolkitCrudConstants.setDecimalSeparator(decimalSeparator != null ? decimalSeparator.charAt(0)  : '.');
        ToolkitCrudConstants.setGroupingSeparator(groupingSeparator != null ? groupingSeparator.charAt(0) : ',');

    }

}
