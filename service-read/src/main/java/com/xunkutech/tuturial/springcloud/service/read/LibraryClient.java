package com.xunkutech.tuturial.springcloud.service.read;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("service-library")
public interface LibraryClient {
	@GetMapping("/libraries")
    Map<String, String> getLibraryList();
}
