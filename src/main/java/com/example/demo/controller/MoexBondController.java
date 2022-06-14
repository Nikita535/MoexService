package com.example.demo.controller;

import com.example.demo.dto.FigiesDto;
import com.example.demo.dto.StocksDto;
import com.example.demo.dto.StocksPricesDto;
import com.example.demo.dto.TickersDto;
import com.example.demo.model.Stock;
import com.example.demo.service.BondService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bonds")
public class MoexBondController {
    private final BondService bondService;


    @PostMapping("/getBondsByTickers")
    public StocksDto getBondsFromMoex(@RequestBody TickersDto tickersDto){
        return bondService.getBondsFromMoex(tickersDto);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPricesByFigies(@RequestBody FigiesDto figiesDto){
        return bondService.getPricesByFigies(figiesDto);
    }
}
