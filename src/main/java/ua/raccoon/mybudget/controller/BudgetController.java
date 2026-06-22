package ua.raccoon.mybudget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.raccoon.mybudget.entity.Expense;
import ua.raccoon.mybudget.entity.Income;
import ua.raccoon.mybudget.service.BudgetService;

import java.time.LocalDate;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // Головна панель (Dashboard) — Витрати та Надходження разом
    @GetMapping
    public String showBudgetCabinet(Model model) {
        // Списки
        model.addAttribute("expenses", budgetService.getAllExpenses());
        model.addAttribute("incomes", budgetService.getAllIncomes());

        // Суми для табло
        model.addAttribute("totalExpenses", budgetService.getTotalExpensesSum());
        model.addAttribute("totalIncomes", budgetService.getTotalIncomesSum());
        model.addAttribute("netBalance", budgetService.getNetBalance());

        // Дефолтні об'єкти з поточною датою для форм додавання
        Expense defaultExpense = new Expense();
        defaultExpense.setDate(LocalDate.now());
        model.addAttribute("newExpense", defaultExpense);

        Income defaultIncome = new Income();
        defaultIncome.setDate(LocalDate.now());
        model.addAttribute("newIncome", defaultIncome);

        // Передаємо підсумки за категоріями
        model.addAttribute("expensesByCategory", budgetService.getExpensesByCategory());
        model.addAttribute("incomesByCategory", budgetService.getIncomesByCategory());


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
}