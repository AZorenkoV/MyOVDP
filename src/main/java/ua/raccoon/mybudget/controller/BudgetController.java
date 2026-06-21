package ua.raccoon.mybudget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.raccoon.mybudget.entity.Expense;
import ua.raccoon.mybudget.service.BudgetService;

import java.time.LocalDate;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // Головна сторінка бюджету (Перегляд усіх витрат та форми додавання)
    @GetMapping
    public String showBudgetCabinet(Model model) {
        model.addAttribute("expenses", budgetService.getAllExpenses());
        model.addAttribute("totalExpenses", budgetService.getTotalExpensesSum());

        // Створюємо порожній об'єкт із поточною датою для форми додавання
        Expense defaultExpense = new Expense();
        defaultExpense.setDate(LocalDate.now());
        model.addAttribute("newExpense", defaultExpense);

        return "budget"; // назва HTML-файлу в templates
    }

    // Додавання нової витрати
    @PostMapping("/expense/add")
    public String addExpense(@ModelAttribute("newExpense") Expense expense) {
        budgetService.saveExpense(expense);
        return "redirect:/budget"; // перезавантажуємо сторінку, щоб оновити список
    }

    // Видалення витрати
    @PostMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        budgetService.deleteExpense(id);
        return "redirect:/budget";
    }
}