package com.epam.lab.writer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface JsonStringWriter {
    void write(Path path, String fileName, List<String> strings) throws IOException;
    void stopWrite();
}
