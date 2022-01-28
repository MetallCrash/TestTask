package by.job.testtask.controller;

import by.job.testtask.dto.CryptoCurrencyDto;
import by.job.testtask.entity.CryptoCurrency;
import by.job.testtask.service.CryptoCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CryptoCurrencyController {

    @Autowired
    private CryptoCurrencyService cryptoCurrencyService;

    @GetMapping
    public List<CryptoCurrency> getCurrencyList() {
        return cryptoCurrencyService.getCurrencyList();
    }


    @GetMapping("/{id}")
    public CryptoCurrencyDto getCurrency(@PathVariable("id") Long id) {
        return cryptoCurrencyService.getCurrency(id);
    }

    @GetMapping("/notify")
    public void notify(@RequestParam("username") String username, @RequestParam("symbol") String symbol) {
        cryptoCurrencyService.registerUserAndCryptoCurrencyPrice(username, symbol);
    }
}
