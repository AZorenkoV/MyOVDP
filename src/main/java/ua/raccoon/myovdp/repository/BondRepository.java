package ua.raccoon.myovdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccoon.myovdp.entity.Bond;

@Repository
public interface BondRepository extends JpaRepository<Bond, Long> {
}
