package br.uenf.eacos.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "br.uenf.eacos.listener",
    "br.uenf.eacos.util"
})
public class AppConfig {
    
}
