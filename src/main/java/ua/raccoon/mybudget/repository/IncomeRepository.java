package ua.raccoon.mybudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccoon.mybudget.entity.Income;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    // Автоматично шукає доходи за вказаний проміжок часу
    List<Income> findByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);
}