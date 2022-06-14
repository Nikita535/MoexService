package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.BondNotFoundException;
import com.example.demo.exception.LimitRequestsException;
import com.example.demo.model.Currency;
import com.example.demo.model.Stock;
import com.example.demo.moexclient.CorporateBondsClient;
import com.example.demo.moexclient.GovermentBondsClient;
import com.example.demo.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BondService {

    //менеджер чтобы удостовериться в правильности работы библы caffeine, которая
    // даёт свою реализациб для кеширования
    private final CacheManager cacheManager;
    private final InjectOutBeansForCache injectOutBeansForCache;

    public StocksDto getBondsFromMoex(TickersDto tickersDto){
        List<BondDto> allBonds =new ArrayList<>();
        allBonds.addAll(injectOutBeansForCache.getGovermentBonds());
        allBonds.addAll(injectOutBeansForCache.getCorporateBonds());

        List<BondDto> resultBonds = allBonds.stream().
                filter(b -> tickersDto.getTickets().contains(b.getTicker())).
                collect(Collectors.toList());
        List<Stock> stocks = resultBonds.stream().
                map(b -> {
                    return Stock.builder().
                            ticker(b.getTicker())
                            .name(b.getName())
                            .figi(b.getTicker())
                            .type("Bond")
                            .currency(Currency.RUB)
                            .source("Moex")
                            .build();
                }).collect(Collectors.toList());
        return new StocksDto(stocks);
    }


    public StocksPricesDto getPricesByFigies(FigiesDto figiesDto) {
        List<String> figies = new ArrayList<>(figiesDto.getFigies());
        List<BondDto> allBonds = new ArrayList<>();
        allBonds.addAll(injectOutBeansForCache.getGovermentBonds());
        allBonds.addAll(injectOutBeansForCache.getCorporateBonds());
        figies.removeAll(allBonds.stream().map(b -> b.getTicker()).collect(Collectors.toList()));
        if(!figies.isEmpty()) {
            throw new BondNotFoundException(String.format("Bonds %s not found.", figies));
        }
        List<StockPrice> prices = allBonds.stream()
                .filter(b -> figiesDto.getFigies().contains(b.getTicker()))
                .map(b -> new StockPrice(b.getTicker(), b.getPrice() * 10))
                .collect(Collectors.toList());
        return new StocksPricesDto(prices);
    }
}
