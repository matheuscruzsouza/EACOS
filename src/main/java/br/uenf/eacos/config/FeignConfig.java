package br.uenf.eacos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.uenf.eacos.feign")
public class FeignConfig {
    
}
