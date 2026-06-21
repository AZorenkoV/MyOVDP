package ua.raccoon.mybudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccoon.mybudget.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Тут ми зможемо додавати кастомні методи пошуку (наприклад, за категоріями чи датами)
}