package by.job.testtask.service;

import by.job.testtask.entity.CryptoCurrency;
import by.job.testtask.entity.CurrencyPriceStash;
import by.job.testtask.repository.CryptoCurrencyRepository;
import by.job.testtask.repository.CurrencyPriceStashRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PriceChangeNotifyService {

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private CurrencyPriceStashRepository currencyPriceStashRepository;

    public void notifyIfPriceChange() {
        Map<String, CryptoCurrency> currencies = getCryptoCurrencies();
        List<CurrencyPriceStash> registeredUserList = currencyPriceStashRepository.findAll();
        registeredUserList.forEach(user -> user.getSavedCurrencies().forEach((symbol, savedPrice) -> {
            CryptoCurrency cryptoCurrency = currencies.get(symbol);
            if (priceHasChanged(savedPrice, cryptoCurrency.getPrice_usd())) {
                logEvent(cryptoCurrency, savedPrice, user.getUser().getUsername());
            }
        }));
    }

    private Map<String, CryptoCurrency> getCryptoCurrencies() {
        List<CryptoCurrency> cryptoCurrencyList = cryptoCurrencyRepository.findAll();
        Map<String, CryptoCurrency> savedCurrencies = new HashMap<>();
        cryptoCurrencyList
                .forEach(cryptoCurrency -> savedCurrencies.put(cryptoCurrency.getSymbol(), cryptoCurrency));
        return savedCurrencies;
    }

    private boolean priceHasChanged(double savedCurrencyPrice, double currentCurrencyPrice) {
        final double onePercent = savedCurrencyPrice / 100;
        double positivePriceChange = savedCurrencyPrice + onePercent;
        double negativePriceChange = savedCurrencyPrice - onePercent;
        return (currentCurrencyPrice > positivePriceChange) || (currentCurrencyPrice < negativePriceChange);
    }

    private void logEvent(CryptoCurrency cryptoCurrency, Double savedCurrencyPrice, String username) {
        double currentCurrencyPrice = cryptoCurrency.getPrice_usd();
        String percentDifference = new DecimalFormat("#0.00")
                .format(((currentCurrencyPrice - savedCurrencyPrice) * 100) / savedCurrencyPrice);
        log.warn(username + ": price of " + cryptoCurrency.getSymbol() + " has changed by " + percentDifference + "%");
    }
}
