package hr.cyka.filehosting.entity;

import lombok.Data;

import java.nio.file.Path;

@Data
public class File {
    private Path path;
    private String name;
}
