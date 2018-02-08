package com.kian.corporatebanking.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.kian.corporatebanking")
public class FeignConfiguration {

}
