package ua.raccon.myovdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.raccon.myovdp.entity.Bond;

@Repository
public interface BondRepository extends JpaRepository<Bond, Long> {
}
