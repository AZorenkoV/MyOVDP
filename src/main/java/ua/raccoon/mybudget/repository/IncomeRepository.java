package ua.raccoon.mybudget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccoon.mybudget.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
}