package hr.cyka.filehosting.service;

import hr.cyka.filehosting.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {
    void init();
    void save(MultipartFile file);
    List<Path> getAll();
    List<File> getAllFiles();
    Path getByFilename(String filename);
    Resource getByFilenameAsResource(String filename);
    void deleteByFilename(String filename);
}
