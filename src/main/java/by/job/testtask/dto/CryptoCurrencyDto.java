package by.job.testtask.dto;

import by.job.testtask.entity.CryptoCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyDto {

    private String name;
    private double price_usd;

    public static CryptoCurrencyDto convertToDtoWithNameAndPrice(CryptoCurrency cryptoCurrency) {
        return new CryptoCurrencyDto(cryptoCurrency.getName(), cryptoCurrency.getPrice_usd());
    }
}
