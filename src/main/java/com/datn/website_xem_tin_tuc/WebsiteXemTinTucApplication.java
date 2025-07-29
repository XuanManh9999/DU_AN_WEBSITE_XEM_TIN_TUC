package com.datn.website_xem_tin_tuc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// mở lập lịch
@EnableScheduling
public class    WebsiteXemTinTucApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebsiteXemTinTucApplication.class, args);
	}
}
