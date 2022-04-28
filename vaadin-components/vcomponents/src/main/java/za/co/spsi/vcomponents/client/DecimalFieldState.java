package za.co.spsi.vcomponents.client;

import com.vaadin.shared.AbstractComponentState;

/**
 * Created by jaspervdb on 2016/11/14.
 */
public class DecimalFieldState extends AbstractComponentState {

    public String groupSeparator=",",decimalSeparator=".",
            maxValueExceededError = "Max value [_VALUE_] exceeded",
            invalidCharacterError = "Invalid character";

    public double max = 10000d;
    public int decimalPlaces = 2;

}
