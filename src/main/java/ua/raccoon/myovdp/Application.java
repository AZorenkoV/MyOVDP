package ua.raccoon.myovdp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ua.raccoon.myovdp.entity.Bond;
import ua.raccoon.myovdp.entity.Payment;
import ua.raccoon.myovdp.service.OvdpService;

import java.time.LocalDate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner testDatabase(OvdpService ovdpService) {
        return args -> {
            System.out.println("--- СТАРТ ТЕСТОВОГО ЗАПИСУ ОВДП ---");

            // Створюємо тестову облігацію за вашими полями:
            // ISIN, Брокер, Назва, Ціна за 1 шт, Кількість, Вартість купона, Дата першої виплати, Дата погашення
            Bond testBond = new Bond(
                    "UA4000239016",
                    "ICU",
                    "Військові ОВДП (UA4000239016)",
                    985.50,
                    10, // Купили 10 штук
                    78.20, // Купон на 1 шт
                    LocalDate.of(2026, 11, 15), // Найближчий купон (через 5 місяців від сьогодні)
                    LocalDate.of(2027, 11, 17)  // Дата погашення (на 1.5 роки)
            );

            // Запускаємо наш сервіс
            ovdpService.saveBondWithCalendar(testBond);

            System.out.println("--- ОВДП ТА КАЛЕНДАР УСПІШНО ЗБЕРЕЖЕНО НА ДИСК! ---");

            // Виведемо в консоль те, що збереглося в календарі виплат
            System.out.println("Ваш майбутній календар виплат:");
            for (Payment p : ovdpService.getCalendar()) {
                System.out.println("- Дата: " + p.getPaymentDate()
                        + " | Тип: " + p.getType()
                        + " | Сума: " + p.getAmount() + " грн");
            }
        };
    }
}