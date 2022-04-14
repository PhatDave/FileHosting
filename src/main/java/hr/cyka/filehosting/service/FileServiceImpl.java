package hr.cyka.filehosting.service;

import hr.cyka.filehosting.validation.FileValidation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    // todo move this to properties somehoqw
    private final Path root = Path.of("media");
    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileValidation fileValidation;

    @Override
    @SneakyThrows
    public void init() {
        logger.info("Initializing file storage");
        Files.createDirectories(root);
    }

    @Autowired
    public FileServiceImpl(FileValidation fileValidation) {
        this.fileValidation = fileValidation;
        init();
    }

    @Override
    @SneakyThrows
    public void save(MultipartFile file) {
        logger.info("Saving file: {}", file.getOriginalFilename());
        fileValidation.validate(file);
        String filename = file.getOriginalFilename();
        fileValidation.validateFilename(filename);
        Path destinationFile = this.root.resolve(Paths.get(filename))
                        .normalize()
                        .toAbsolutePath();
        // todo figure out what's wrong with this
//        fileValidation.validateDirectory(destinationFile, root);
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    @SneakyThrows
    public Stream<Path> getAll() {
        logger.info("Getting all files");
        return Files.walk(root, 1)
                .filter(path -> !path.equals(root))
                .map(root::relativize);
    }

    @Override
    public Path getByFilename(String filename) {
        logger.info("Getting file: {}", filename);
        return root.resolve(filename);
    }

    @Override
    @SneakyThrows
    public Resource getByFilenameAsResource(String filename) {
        logger.info("Getting file: {} as resource", filename);
        Path file = getByFilename(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Could not read file: " + filename);
        }
    }

    @Override
    @SneakyThrows
    public void deleteByFilename(String filename) {
        logger.info("Deleting file: {}", filename);
        Path file = getByFilename(filename);
        Files.delete(file);
    }
}
