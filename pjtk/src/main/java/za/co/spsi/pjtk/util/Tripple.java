package za.co.spsi.pjtk.util;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class Tripple<S, T, U> implements Serializable {

    @NonNull
    private final S first;
    @NonNull
    private final T second;
    @NonNull
    private final U third;

}
