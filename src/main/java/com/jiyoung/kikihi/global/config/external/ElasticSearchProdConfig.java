package com.jiyoung.kikihi.global.config.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@Profile("prod")
@EnableElasticsearchRepositories(basePackages = "com.jiyoung.kikihi.platform.adapter.out.elasticSearch")
public class ElasticSearchProdConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String host;

    @Value("${elasticsearch.user_name}")
    private String username;

    @Value("${elasticsearch.user_password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host)
                .withBasicAuth(username, password)
                .build();
    }
}
