package ua.raccoon.mybudget.service;

import org.springframework.stereotype.Service;
import ua.raccoon.mybudget.entity.Expense;
import ua.raccoon.mybudget.entity.Income;
import ua.raccoon.mybudget.repository.ExpenseRepository;
import ua.raccoon.mybudget.repository.IncomeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    // Оновлений конструктор для двох репозиторіїв
    public BudgetService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    // --- ВИТРАТИ (залишаються без змін) ---
    public Expense saveExpense(Expense expense) { return expenseRepository.save(expense); }
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .sorted((e1, e2) -> e2.getDate().compareTo(e1.getDate())).toList();
    }
    public BigDecimal getTotalExpensesSum() {
        return expenseRepository.findAll().stream()
                .map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void deleteExpense(Long id) { expenseRepository.deleteById(id); }

    // --- НАДХОДЖЕННЯ (нові методи) ---
    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll().stream()
                .sorted((i1, i2) -> i2.getDate().compareTo(i1.getDate()))
                .toList();
    }

    public BigDecimal getTotalIncomesSum() {
        return incomeRepository.findAll().stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    // Потужна штука для загального балансу (Доходи - Витрати)
    public BigDecimal getNetBalance() {
        return getTotalIncomesSum().subtract(getTotalExpensesSum());
    }

    // Групування витрат за категоріями (Категорія -> Сума)
    public Map<String, BigDecimal> getExpensesByCategory() {
        return expenseRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.mapping(
                                Expense::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

    // Групування надходжень за категоріями (Категорія -> Сума)
    public Map<String, BigDecimal> getIncomesByCategory() {
        return incomeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Income::getCategory,
                        Collectors.mapping(
                                Income::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }
}