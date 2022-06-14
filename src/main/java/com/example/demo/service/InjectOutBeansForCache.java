package com.example.demo.service;

import com.example.demo.dto.BondDto;
import com.example.demo.exception.LimitRequestsException;
import com.example.demo.moexclient.CorporateBondsClient;
import com.example.demo.moexclient.GovermentBondsClient;
import com.example.demo.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class InjectOutBeansForCache {
    //Создаем 2 клиента для поочередного запроса на разные xml
    private final CorporateBondsClient corporateBondsClient;
    private final GovermentBondsClient govermentBondsClient;
    //Парсер есть парсер че бубнить
    private final Parser parser;


    @Cacheable(value = "corporation")
    public List<BondDto> getCorporateBonds() {
        String xmlFromMoex = corporateBondsClient.getBondsFromMoex();
        List<BondDto> bondDto = parser.parse(xmlFromMoex);
        if(bondDto.isEmpty()){
            throw new LimitRequestsException("Limit exception");
        }
        return bondDto;
    }

    @Cacheable(value = "goverment")
    public List<BondDto> getGovermentBonds() {
        String xmlFromMoex = govermentBondsClient.getBondsFromMoex();
        List<BondDto> bondDto = parser.parse(xmlFromMoex);
        if(bondDto.isEmpty()){
            throw new LimitRequestsException("Limit exception");
        }
        return bondDto;
    }
}
