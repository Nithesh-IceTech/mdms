package za.co.spsi.toolkit.io;


import lombok.Data;

import java.io.File;
import java.io.IOException;

import static za.co.spsi.toolkit.io.IOUtil.deleteRecursively;

@Data
public class ClosableFile implements AutoCloseable {

    private File file;

    public ClosableFile(File file) throws IOException {
        this.file = file;
    }

    public void close() {
        deleteRecursively(file);
    }


}
