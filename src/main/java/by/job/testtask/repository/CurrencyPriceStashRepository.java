package by.job.testtask.repository;

import by.job.testtask.entity.CurrencyPriceStash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyPriceStashRepository extends JpaRepository<CurrencyPriceStash, Double> {
}
