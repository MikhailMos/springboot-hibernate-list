package ru.example.springboot.hibernate.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringbootHibernateListApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHibernateListApplication.class, args);
	}

}
