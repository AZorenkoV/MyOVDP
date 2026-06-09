package ua.raccon.myovdp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bonds")
public class Bond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private String broker;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price; // Ціна купівлі однієї штуки

    @Column(nullable = false)
    private int quantity; // Кількість (залишаємо, щоб знати загальну суму виплат)

    @Column(nullable = false)
    private double couponAmount; // Вартість купона за 1 шт (наприклад, 75.50)

    @Column(nullable = false)
    private LocalDate firstPaymentDate; // Дата першої виплати

    @Column(nullable = false)
    private LocalDate maturityDate; // Дата погашення

    @OneToMany(mappedBy = "bond", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    // Порожній конструктор для Hibernate
    public Bond() {}

    // Конструктор для зручного створення об'єктів
    public Bond(String isin, String broker, String name, double price, int quantity,
                double couponAmount, LocalDate firstPaymentDate, LocalDate maturityDate) {
        this.isin = isin;
        this.broker = broker;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.couponAmount = couponAmount;
        this.firstPaymentDate = firstPaymentDate;
        this.maturityDate = maturityDate;
    }

    // Рядок нижче потрібен, щоб згенерувати геттери і сеттери для кожного поля
    // (В IntelliJ IDEA це можна зробити через Alt+Insert -> Getter and Setter)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public String getBroker() { return broker; }
    public void setBroker(String broker) { this.broker = broker; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getCouponAmount() { return couponAmount; }
    public void setCouponAmount(double couponAmount) { this.couponAmount = couponAmount; }

    public LocalDate getFirstPaymentDate() { return firstPaymentDate; }
    public void setFirstPaymentDate(LocalDate firstPaymentDate) { this.firstPaymentDate = firstPaymentDate; }

    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}