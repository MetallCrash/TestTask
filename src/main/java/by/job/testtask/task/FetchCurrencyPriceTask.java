package by.job.testtask.task;

import by.job.testtask.entity.AvailableCurrency;
import by.job.testtask.entity.CryptoCurrency;
import by.job.testtask.repository.CryptoCurrencyRepository;
import by.job.testtask.service.PriceChangeNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@EnableScheduling
@Service
public class FetchCurrencyPriceTask {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    private PriceChangeNotifyService priceChangeNotifyService;

    private final String currencyResourceUrl = "https://api.coinlore.net/api/ticker/?id=";


    @Scheduled(fixedRate = 60000)
    private void saveCurrencyPrice() {
        AvailableCurrency.stream()
                .forEach(availableCurrency -> {
                    CryptoCurrency[] cryptoCurrencies = restTemplate
                            .getForObject(currencyResourceUrl + availableCurrency.getId(), CryptoCurrency[].class);
                    if (cryptoCurrencies != null) {
                        cryptoCurrencyRepository.saveAll(Arrays.asList(cryptoCurrencies));
                    }
                });
        priceChangeNotifyService.notifyIfPriceChange();
    }
}
