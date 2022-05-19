package com.edh.mendelexam.config;

import com.edh.mendelexam.data_access.TransactionNode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class PersistanceConfig {

    @Bean
    public Map<Long, TransactionNode> transactionsMap() {
        return new HashMap<>();
    }

    @Bean
    public Map<String, Set<Long>> transactionsTypesMap() {
        return new HashMap<>();
    }
}
