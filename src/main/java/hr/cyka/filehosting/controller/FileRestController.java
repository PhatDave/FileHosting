package hr.cyka.filehosting.controller;

import hr.cyka.filehosting.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files/")
public class FileRestController {
	private final Logger logger = LoggerFactory.getLogger(FileRestController.class);
	private final FileService fileService;

	@GetMapping
	private ResponseEntity<?> getAll() {
		logger.info("getAll()");
		return ResponseEntity.ok(fileService.getAllFiles());
	}

	@DeleteMapping("/{fileName}")
	private ResponseEntity<?> delete(@PathVariable String fileName) {
		logger.info("delete({})", fileName);
		fileService.deleteByFilename(fileName);
		return ResponseEntity.ok().build();
	}
}
