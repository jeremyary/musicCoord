package jary.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author jary
 * @since Nov/06/2014
 */
@Configuration
@ImportResource("classpath:spring.xml")
@ComponentScan("jary")
public class AppConfig {




}
