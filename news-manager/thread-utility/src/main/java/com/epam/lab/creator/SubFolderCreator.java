package com.epam.lab.creator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SubFolderCreator {
    List<Path> create(Path rootPath, int subFoldersCount, int deep) throws IOException;

    List<Path> getCreatedPaths();
}
