package ua.raccoon.mybudget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.raccoon.mybudget.entity.Expense;
import ua.raccoon.mybudget.entity.Income;
import ua.raccoon.mybudget.service.BudgetService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public String showBudgetCabinet(Model model) {
        // 1. Визначаємо межі поточного місяця (липень 2026 року)
        LocalDate today = LocalDate.now();
        java.time.YearMonth currentMonth = java.time.YearMonth.from(today);
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        // 2. Отримуємо відфільтровані списки за поточний місяць
        List<Expense> currentExpenses = budgetService.getExpensesForPeriod(start, end);
        List<Income> currentIncomes = budgetService.getIncomesForPeriod(start, end);

        model.addAttribute("expenses", currentExpenses);
        model.addAttribute("incomes", currentIncomes);

        // 3. Рахуємо суми для табло (тільки за поточний місяць)
        java.math.BigDecimal totalExpenses = currentExpenses.stream()
                .map(Expense::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalIncomes = currentIncomes.stream()
                .map(Income::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal netBalance = totalIncomes.subtract(totalExpenses);

        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalIncomes", totalIncomes);
        model.addAttribute("netBalance", netBalance);

        // 4. Передаємо підсумки за категоріями (групуємо відфільтровані дані)
        java.util.Map<String, java.math.BigDecimal> expensesByCategory = currentExpenses.stream()
                .collect(java.util.stream.Collectors.groupingBy(Expense::getCategory,
                        java.util.stream.Collectors.mapping(Expense::getAmount,
                                java.util.stream.Collectors.reducing(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))));

        java.util.Map<String, java.math.BigDecimal> incomesByCategory = currentIncomes.stream()
                .collect(java.util.stream.Collectors.groupingBy(Income::getCategory,
                        java.util.stream.Collectors.mapping(Income::getAmount,
                                java.util.stream.Collectors.reducing(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))));

        model.addAttribute("expensesByCategory", expensesByCategory);
        model.addAttribute("incomesByCategory", incomesByCategory);

        // 5. Дефолтні об'єкти з поточною датою для форм додавання (залишаються як були)
        Expense defaultExpense = new Expense();
        defaultExpense.setDate(today);
        model.addAttribute("newExpense", defaultExpense);

        Income defaultIncome = new Income();
        defaultIncome.setDate(today);
        model.addAttribute("newIncome", defaultIncome);

        return "budget";
    }

    // --- ВИТРАТИ ---
    @PostMapping("/expense/add")
    public String addExpense(@ModelAttribute("newExpense") Expense expense) {
        budgetService.saveExpense(expense);
        return "redirect:/budget";
    }

    @PostMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        budgetService.deleteExpense(id);
        return "redirect:/budget";
    }

    // --- НАДХОДЖЕННЯ ---
    @PostMapping("/income/add")
    public String addIncome(@ModelAttribute("newIncome") Income income) {
        budgetService.saveIncome(income);
        return "redirect:/budget";
    }

    @PostMapping("/income/delete/{id}")
    public String deleteIncome(@PathVariable("id") Long id) {
        budgetService.deleteIncome(id);
        return "redirect:/budget";
    }

    @GetMapping("/history")
    public String showHistoryPage(@RequestParam(required = false) String month, Model model) {
        YearMonth selectedMonth;

        // Якщо місяць передано в параметрах (наприклад, "2026-05"), парсимо його
        if (month != null && !month.isEmpty()) {
            selectedMonth = YearMonth.parse(month);
        } else {
            // Якщо параметр порожній — беремо поточний місяць
            selectedMonth = YearMonth.now();
        }

        LocalDate start = selectedMonth.atDay(1);
        LocalDate end = selectedMonth.atEndOfMonth();

        // Витягуємо дані з бази за обраний період
        List<Expense> expenses = budgetService.getExpensesForPeriod(start, end);
        List<Income> incomes = budgetService.getIncomesForPeriod(start, end);

        // Рахуємо суми
        BigDecimal totalExpenses = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncomes = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Передаємо в Thymeleaf
        model.addAttribute("expenses", expenses);
        model.addAttribute("incomes", incomes);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalIncomes", totalIncomes);
        model.addAttribute("selectedMonth", selectedMonth.toString()); // Передаємо назад для інпуту "2026-07"

        return "history";
    }
}