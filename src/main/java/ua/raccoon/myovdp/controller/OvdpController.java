package ua.raccoon.myovdp.controller;

import org.springframework.web.bind.annotation.*;
import ua.raccoon.myovdp.entity.Bond;
import ua.raccoon.myovdp.entity.Payment;
import ua.raccoon.myovdp.service.OvdpService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OvdpController {

    private final OvdpService ovdpService;

    // Весна (Spring) автоматично підтягне сюди наш сервіс із бізнес-логікою
    public OvdpController(OvdpService ovdpService) {
        this.ovdpService = ovdpService;
    }

    // 1. Отримати всі куплені облігації
    // Буде доступно за адресою: GET http://localhost:8080/api/bonds
    @GetMapping("/bonds")
    public List<Bond> getAllBonds() {
        return ovdpService.getAllBonds();
    }

    // 2. Отримати весь календар виплат (впорядкований за датами)
    // Буде доступно за адресою: GET http://localhost:8080/api/calendar
    @GetMapping("/calendar")
    public List<Payment> getCalendar() {
        return ovdpService.getCalendar();
    }

    // 3. Додати нову облігацію та автоматично згенерувати для неї календар виплат
    // Буде доступно за адресою: POST http://localhost:8080/api/bonds
    @PostMapping("/bonds")
    public Bond createBond(@RequestBody Bond bond) {
        return ovdpService.saveBondWithCalendar(bond);
    }
}