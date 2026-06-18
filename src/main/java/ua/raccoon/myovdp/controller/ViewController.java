package ua.raccoon.myovdp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.raccoon.myovdp.entity.Bond;
import ua.raccoon.myovdp.entity.Payment;
import ua.raccoon.myovdp.service.OvdpService;

import java.util.List;

@Controller
@RequestMapping("/ovdp")
public class ViewController {

    private final OvdpService ovdpService;

    public ViewController(OvdpService ovdpService) {
        this.ovdpService = ovdpService;
    }

    @GetMapping
    public String index(
            @RequestParam(value = "isin", required = false) String isin,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status, // Новий параметр
            Model model) {

        model.addAttribute("bonds", ovdpService.getAllBonds());

        // Передаємо всі три фільтри в сервіс
        List<Payment> filteredCalendar = ovdpService.getCalendarFiltered(isin, type, status);
        model.addAttribute("calendar", filteredCalendar);

        // Рахуємо суму для відфільтрованих записів
        double totalAmount = filteredCalendar.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        model.addAttribute("totalAmount", totalAmount);

        // Запам'ятовуємо обрані значення для трьох селекторів
        model.addAttribute("selectedIsin", isin != null ? isin : "ALL");
        model.addAttribute("selectedType", type != null ? type : "ALL");
        model.addAttribute("selectedStatus", status != null ? status : "ALL"); // Запам'ятовуємо статус

        return "ovdp";
    }

    @PostMapping("/add")
    public String addBond(@ModelAttribute Bond bond) {
        ovdpService.saveBondWithCalendar(bond);
        return "redirect:/ovdp";
    }

    @GetMapping("/delete/{id}")
    public String deleteBond(@PathVariable Long id) {
        ovdpService.deleteBond(id);
        return "redirect:/ovdp";
    }

    @GetMapping("/payment/edit/{id}")
    public String editPaymentForm(@PathVariable Long id, Model model) {
        model.addAttribute("payment", ovdpService.getPaymentById(id));
        return "edit-payment";
    }

    @PostMapping("/payment/save")
    public String saveEditedPayment(@ModelAttribute Payment payment) {
        ovdpService.savePayment(payment);
        return "redirect:/ovdp";
    }

    // Швидке підтвердження отримання виплати в один клік
    @GetMapping("/payment/pay/{id}")
    public String markAsPaid(@PathVariable Long id) {
        Payment payment = ovdpService.getPaymentById(id);
        payment.setStatus("PAID");
        ovdpService.savePayment(payment);

        // Повертаємося назад на сторінку з тими ж фільтрами, які були
        return "redirect:/ovdp";
    }
}