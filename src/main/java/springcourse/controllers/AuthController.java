package springcourse.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springcourse.dao.AuthDAO;

@Controller
public class AuthController {

    public static final String ADMIN_AUTH = "ADMIN_AUTH";

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @Autowired
    private AuthDAO authDAO;

    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpSession session,
                          Model model) {

        if (authDAO.authenticateVulnerable(username, password)) {
            session.setAttribute(ADMIN_AUTH, true);
            return "redirect:/people";
        }

        model.addAttribute("error", "Incorrect login or password");
        return "auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}