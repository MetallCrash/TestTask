package by.job.testtask.service;

import by.job.testtask.dto.CryptoCurrencyDto;
import by.job.testtask.entity.AvailableCurrency;
import by.job.testtask.entity.CryptoCurrency;
import by.job.testtask.entity.CurrencyPriceStash;
import by.job.testtask.entity.User;
import by.job.testtask.repository.CryptoCurrencyRepository;
import by.job.testtask.repository.CurrencyPriceStashRepository;
import by.job.testtask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
@EnableScheduling
public class CryptoCurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyPriceStashRepository currencyPriceStashRepository;

    private final String currencyResourceUrl = "https://api.coinlore.net/api/ticker/?id=";

    public List<CryptoCurrency> getCurrencyList() {
        List<CryptoCurrency> cryptoCurrencyList = new ArrayList<>();
        for (AvailableCurrency availableCurrency : AvailableCurrency.values()) {
            CryptoCurrency[] arrayOfCryptoCurrencyData =
                    restTemplate.getForObject(currencyResourceUrl + availableCurrency.getId(), CryptoCurrency[].class);
            if (arrayOfCryptoCurrencyData != null) {
                cryptoCurrencyList.addAll(Arrays.asList(arrayOfCryptoCurrencyData));
            }
        }
        return cryptoCurrencyList;
    }

    public CryptoCurrencyDto getCurrency(Long id) {
        return cryptoCurrencyRepository.findById(id)
                .map(CryptoCurrencyDto::convertToDtoWithNameAndPrice)
                .orElse(null);
    }

    public void registerUserAndCryptoCurrencyPrice(String username, String symbol) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            CryptoCurrency currency = cryptoCurrencyRepository.findBySymbol(symbol.toUpperCase(Locale.ROOT));
            CurrencyPriceStash price = new CurrencyPriceStash();
            price.setUser(user.get());
            price.getSavedCurrencies().put(currency.getSymbol(), currency.getPrice_usd());
            currencyPriceStashRepository.save(price);
        }
    }

}
