package br.jus.trf1.sipe.comum.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                "arquivosPorNome" // cache para bytes de arquivos (logos, templates)
        );
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)           // ajuste conforme memória
                .expireAfterWrite(Duration.ofHours(6)) // TTL razoável p/ assets
                .recordStats());
        return manager;
    }
}
