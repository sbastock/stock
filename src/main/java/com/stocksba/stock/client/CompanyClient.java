package com.stocksba.stock.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "stocksba-company")
public interface CompanyClient {
	
	@RequestMapping(value = "/company/api/v1/company/query", method = RequestMethod.GET)
	ResponseEntity<Object> getCompany(@RequestParam("code") String code);

}
