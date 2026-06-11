package ua.raccoon.myovdp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.raccoon.myovdp.service.OvdpService;

@Controller
@RequestMapping("/ovdp") // Тепер усі адреси цього контролера починатимуться з /ovdp
public class ViewController {

    private final OvdpService ovdpService;

    public ViewController(OvdpService ovdpService) {
        this.ovdpService = ovdpService;
    }

    // Цей метод тепер буде слухати адресу: http://localhost:8080/ovdp
    @GetMapping
    public String index(Model model) {
        model.addAttribute("bonds", ovdpService.getAllBonds());
        model.addAttribute("calendar", ovdpService.getCalendar());
        return "ovdp";
    }
}