package com.amazingco.hierarchy;

import com.amazingco.hierarchy.config.InjectedConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
@ImportAutoConfiguration(InjectedConfig.class)
public class HierarchyNodesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HierarchyNodesApplication.class, args);
	}

}
