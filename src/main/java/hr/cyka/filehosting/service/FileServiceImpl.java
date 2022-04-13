package hr.cyka.filehosting.service;

import hr.cyka.filehosting.validation.FileValidation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${file.upload.path}")
    private Path root;

    private final FileValidation fileValidation;

    @Override
    @SneakyThrows
    public void init() {
        Files.createDirectories(root);
    }

    @Override
    @SneakyThrows
    public void save(MultipartFile file) {
        fileValidation.validate(file);
        String filename = file.getOriginalFilename();
        fileValidation.validateFilename(filename);
        Path destinationFile = this.root.resolve(Paths.get(filename))
                        .normalize()
                        .toAbsolutePath();
        fileValidation.validateDirectory(destinationFile, root);
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    @SneakyThrows
    public Stream<Path> getAll() {
        return Files.walk(root, 1)
                .filter(path -> !path.equals(root))
                .map(root::relativize);
    }

    @Override
    public Path getByFilename(String filename) {
        return root.resolve(filename);
    }

    @Override
    @SneakyThrows
    public Resource getByFilenameAsResource(String filename) {
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
        Path file = getByFilename(filename);
        Files.delete(file);
    }
}
