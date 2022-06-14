package com.example.demo.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="goverment",url="${moex.bonds.goverment.url}")
public interface GovermentBondsClient {
    @GetMapping
    String getBondsFromMoex();
}
