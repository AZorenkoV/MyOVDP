package ua.raccoon.mybudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccoon.mybudget.entity.Expense;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Автоматично шукає витрати за вказаний проміжок часу
    List<Expense> findByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);
}