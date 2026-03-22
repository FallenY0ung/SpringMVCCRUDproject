package springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/xss/comments")
public class XssStoredController {

    // Память процесса — только для учебной демонстрации
    private final List<String> vulnerableComments = new ArrayList<>();
    private final List<String> safeComments = new ArrayList<>();

    // Страница с уязвимыми комментариями
    @GetMapping("/vulnerable")
    public String listVulnerable(Model model) {
        model.addAttribute("comments", vulnerableComments);
        return "xss/comments_vulnerable";
    }

    // Добавление уязвимого комментария (stored XSS)
    @PostMapping("/vulnerable/add")
    public String addVulnerable(@RequestParam("text") String text) {
        vulnerableComments.add(text); // сохраняем как есть
        return "redirect:/xss/comments/vulnerable";
    }

    // Страница с защищёнными комментариями
    @GetMapping("/safe")
    public String listSafe(Model model) {
        model.addAttribute("comments", safeComments);
        return "xss/comments_safe";
    }

    // Добавление защищённого комментария
    @PostMapping("/safe/add")
    public String addSafe(@RequestParam("text") String text) {
        String safe = text == null ? "" : text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
        safeComments.add(safe);
        return "redirect:/xss/comments/safe";
    }
}
