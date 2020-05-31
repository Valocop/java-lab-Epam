package com.epam.lab.creator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
public class SubFolderCreatorImpl implements SubFolderCreator {
    private static final Logger LOG = LogManager.getLogger(SubFolderCreatorImpl.class);
    private List<Path> createdPaths = new ArrayList<>();

    @Autowired
    @Override
    public List<Path> create(@Value("${PATH}") Path rootPath,
                             @Value("${SUBORDERS_COUNT}") int subFoldersCount, @Value("3") int deep) throws IOException {
        int foldersCount = subFoldersCount - deep;
        clearPath(rootPath);
        List<Path> subFolderTree = createSubFolderTree(rootPath, deep);
        LOG.info("SubFolderCreator creates folders " + subFolderTree.toString());
        List<Path> randomSubFolders = createRandomSubFolders(subFolderTree, foldersCount);
        createdPaths.addAll(randomSubFolders);
        return randomSubFolders;
    }

    @Override
    public List<Path> getCreatedPaths() {
        return createdPaths;
    }

    private void clearPath(Path path) throws IOException {
        LOG.info("SubFolderCreator clears root path " + path.toString());
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private List<Path> createRandomSubFolders(List<Path> pathList, int foldersCount) throws IOException {
        List<Path> paths = new ArrayList<>(pathList);

        for (int i = 0; i < foldersCount; i++) {
            int index = new Random().nextInt(pathList.size());
            Path path = pathList.get(index).resolve("sub" + i);
            Files.createDirectories(path);
            paths.add(path);
        }

        return paths;
    }

    private List<Path> createSubFolderTree(Path rootPath, int deep) throws IOException {
        List<Path> paths = new ArrayList<>();
        Path subFoldersPath = rootPath;
        paths.add(subFoldersPath);

        for (int i = 0; i < deep; i++) {
            subFoldersPath = subFoldersPath.resolve("subRoot" + i);
            Files.createDirectories(subFoldersPath);
            paths.add(subFoldersPath);
        }

        return paths;
    }
}
