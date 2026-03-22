package springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class XssReflectedController {

    // УЯЗВИМЫЙ reflected XSS
    @GetMapping("/xss/reflected")
    public String reflectedVulnerable(@RequestParam(name = "q", required = false) String q,
                                      Model model) {
        model.addAttribute("query", q); // без экранирования
        return "xss/reflected";        // шаблон xss/reflected.html
    }

    // ЗАЩИЩЁННЫЙ вариант
    @GetMapping("/xss/reflected-safe")
    public String reflectedSafe(@RequestParam(name = "q", required = false) String q,
                                Model model) {
        String safe = q == null ? "" : q
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
        model.addAttribute("query", safe);
        return "xss/reflected_safe";   // шаблон xss/reflected_safe.html
    }
}
