package ua.raccoon.mybudget.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String category; // Напр. Зарплата, ОВДП (купони), Кешбек, Інше

    private String account;  // Куди зараховано (Моно, Приват, Готівка)
    private String description;

    // --- Конструктори ---
    public Income() {
    }

    public Income(LocalDate date, BigDecimal amount, String category, String account, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.account = account;
        this.description = description;
    }

    // --- Геттери та Сеттери ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}