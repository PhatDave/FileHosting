package hr.cyka.filehosting.validation;

import hr.cyka.filehosting.exception.StorageException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Component
public class FileValidation {
    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }
    }

    public void validateDirectory(Path root, Path directory) {
        if (!directory.getParent().equals(root.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory.");
        }
    }

    public void validateFilename(String filename) {
        if (filename == null) {
            throw new StorageException("Filename is null.");
        }
    }
}
