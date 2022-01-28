package by.job.testtask.repository;

import by.job.testtask.entity.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {
    CryptoCurrency findBySymbol(String symbol);
}
