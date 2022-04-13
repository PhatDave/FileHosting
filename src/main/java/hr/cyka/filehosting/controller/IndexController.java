package hr.cyka.filehosting.controller;

import hr.cyka.filehosting.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final FileService fileService;

    @GetMapping
    public String index(Model model) {
        return "index";
    }
}
