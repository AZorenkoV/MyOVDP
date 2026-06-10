package ua.raccoon.myovdp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bond_id", nullable = false)
    private Bond bond;

    public Payment() {}

    public Payment( LocalDate paymentDate, Double amount, String type, String status, Bond bond) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.bond = bond;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bond getBond() {
        return bond;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }
}
