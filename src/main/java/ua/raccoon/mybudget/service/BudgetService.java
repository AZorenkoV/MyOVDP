package ua.raccoon.mybudget.service;

import org.springframework.stereotype.Service;
import ua.raccoon.mybudget.entity.Expense;
import ua.raccoon.mybudget.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    private final ExpenseRepository expenseRepository;

    // Впровадження залежності через конструктор
    public BudgetService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Зберегти нову витрату
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Отримати всі витрати (відсортовані від нових до старих)
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate()))
                .toList();
    }

    // Порахувати загальну суму всіх витрат
    public BigDecimal getTotalExpensesSum() {
        return expenseRepository.findAll().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Видалити витрату за ID
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}