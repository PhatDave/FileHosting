package hr.cyka.filehosting.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.upload.path}")
    private Path root;

    @Override
    public void init() {
        // todo add exception handling
        Files.createDirectories(root);
    }

    @Override
    public void save(MultipartFile file) {
        
    }

    @Override
    public Stream<Path> getAll() {
        return null;
    }

    @Override
    public Path getByFilename(String filename) {
        return null;
    }

    @Override
    public Resource getByFilenameAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
