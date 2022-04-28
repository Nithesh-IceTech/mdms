package za.co.spsi.pjtk.io;


import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static za.co.spsi.pjtk.io.IOUtil.deleteRecursively;


@Data
public class ClosableFile implements AutoCloseable {

    private Optional<File> file;

    public ClosableFile(File file) throws IOException {
        this.file = Optional.of(file);
    }

    public ClosableFile(Optional<File> file) throws IOException {
        this.file = file;
    }

    public void close() {
        if (file.isPresent()) {
            deleteRecursively(file.get());
        }
    }

    public File get() {
        return file.get();
    }

    public boolean isPresent() {
        return file.isPresent();
    }
}
