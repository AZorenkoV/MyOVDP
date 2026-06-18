package ua.raccoon.myovdp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.raccoon.myovdp.entity.Bond;
import ua.raccoon.myovdp.entity.Payment;
import ua.raccoon.myovdp.repository.BondRepository;
import ua.raccoon.myovdp.repository.PaymentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class OvdpService {

    private final BondRepository bondRepository;
    private final PaymentRepository paymentRepository;

    public OvdpService(BondRepository bondRepository, PaymentRepository paymentRepository) {
        this.bondRepository = bondRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Bond saveBondWithCalendar(Bond bond) {
        // 1. Зберігаємо ОВДП в базу, щоб отримати її ID
        Bond savedBond = bondRepository.save(bond);

        // Рахуємо суми виплат для всього об'єму куплених облігацій
        double totalCouponAmount = savedBond.getCouponAmount() * savedBond.getQuantity();
        double totalNominal = savedBond.getQuantity() * 1000.0;

        // 2. ЦИКЛ ГЕНЕРАЦІЇ КУПОНІВ
        LocalDate nextPaymentDate = savedBond.getFirstPaymentDate();

        while (nextPaymentDate.isBefore(savedBond.getMaturityDate())) {
            // Перевірка: якщо до погашення залишилося МЕНШЕ ніж 2 місяці,
            // то окремий купон створювати не треба, він виплатиться вже при погашенні.
            if (nextPaymentDate.plusMonths(2).isAfter(savedBond.getMaturityDate())) {
                break;
            }

            Payment coupon = new Payment(nextPaymentDate, totalCouponAmount, "COUPON", "PENDING", savedBond);
            paymentRepository.save(coupon);

            nextPaymentDate = nextPaymentDate.plusMonths(6);
        }

        // 3. ФІНАЛЬНІ ВИПЛАТИ (в день погашення облігації)
        // Цей купон і погашення номіналу будуть завжди
        Payment finalCoupon = new Payment(savedBond.getMaturityDate(), totalCouponAmount, "COUPON", "PENDING", savedBond);
        paymentRepository.save(finalCoupon);

        Payment maturity = new Payment(savedBond.getMaturityDate(), totalNominal, "MATURITY", "PENDING", savedBond);
        paymentRepository.save(maturity);

        return savedBond;
    }

    public void deleteBond(Long id) {
        bondRepository.deleteById(id);
    }

    // Знайти виплату за ID для відображення на формі редагування
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Виплату з ID " + id + " не знайдено"));
    }

    // Зберегти відредаговану виплату
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    // Додатковий метод: отримати всі ОВДП із бази
    public List<Bond> getAllBonds() {
        return bondRepository.findAll();
    }

    // Додатковий метод: отримати весь календар виплат (відсортований за датою)
    public List<Payment> getCalendar() {
        return paymentRepository.findAllByOrderByPaymentDateAsc();
    }

    // Повна фільтрація: за ISIN, типом виплати та статусом одночасно
    public List<Payment> getCalendarFiltered(String isin, String type, String status) {
        return paymentRepository.findAll().stream()
                // 1. Фільтр за облігацією
                .filter(p -> isin == null || isin.isEmpty() || isin.equals("ALL") || p.getBond().getIsin().equals(isin))
                // 2. Фільтр за типом виплати (КУПОН/ПОГАШЕННЯ)
                .filter(p -> type == null || type.isEmpty() || type.equals("ALL") || p.getType().equals(type))
                // 3. Фільтр за статусом виплати (PENDING/PAID)
                .filter(p -> status == null || status.isEmpty() || status.equals("ALL") || p.getStatus().equals(status))
                // Сортування за датою
                .sorted((p1, p2) -> p1.getPaymentDate().compareTo(p2.getPaymentDate()))
                .toList();
    }
}