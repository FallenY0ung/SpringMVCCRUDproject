package springcourse.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springcourse.service.ReplicationService;

@Controller
@RequestMapping("/admin/replication")
public class ReplicationController {

    private final ReplicationService replicationService;

    public ReplicationController(ReplicationService replicationService) {
        this.replicationService = replicationService;
    }

    @PostMapping("/run")
    public String runReplication(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isAdmin = (Boolean) session.getAttribute(AuthController.ADMIN_AUTH);

        if (!Boolean.TRUE.equals(isAdmin)) {
            return "redirect:/login";
        }

        try {
            replicationService.replicateAll();
            redirectAttributes.addFlashAttribute("message", "Репликация успешно выполнена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Ошибка репликации: " + e.getMessage());
        }

        return "redirect:/people";
    }
}