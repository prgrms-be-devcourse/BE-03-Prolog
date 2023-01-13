package com.prgrms.prolog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db/db.properties") // env(db).properties 파일 소스 등록
public class DatabaseConfig {

}
