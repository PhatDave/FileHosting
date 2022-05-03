package hr.cyka.filehosting.controller;

import hr.cyka.filehosting.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final FileService fileService;

    @GetMapping("/files")
    public String listUploadedFiles(Model model) {
        return "index";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.getByFilenameAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") List<MultipartFile> file) {
        for (MultipartFile f : file) {
            fileService.save(f);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, RedirectAttributes redirectAttributes) {
        fileService.deleteByFilename(filename);
        redirectAttributes.addFlashAttribute("message",
                "You successfully deleted " + filename + "!");
        return "redirect:/files";
    }
}
