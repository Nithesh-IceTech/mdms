package za.co.spsi.pjtk.io;

import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

@NoArgsConstructor
public class Files extends ArrayList<File> {

    public Files filter(String name) {
        return stream().filter(f -> f.getName().equals(name)).collect(Collectors.toCollection(Files::new));
    }
}
