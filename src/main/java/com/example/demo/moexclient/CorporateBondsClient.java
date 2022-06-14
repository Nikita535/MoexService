package com.example.demo.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="corporate",url="${moex.bonds.corporate.url}")
public interface CorporateBondsClient {
    //гет запрос, который возвращает xml в виде строки
    @GetMapping
    String getBondsFromMoex();
}
