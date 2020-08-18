package com.lbass.kotlinhttp

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.web.reactive.function.client.WebClient

@SpringJUnitConfig(WebClientConfig::class)
@ExtendWith(SpringExtension::class)
@WebFluxTest
class AsyncWebClientRouterTest(@Autowired val client: WebClient) {
    @Test
    fun get() {
        for (i in 1..100) {
            println("$i 번째 시도 =====")
            client
                    .get()
                    .uri("http://localhost:8080/api/hello2")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .subscribe { println(it) }
        }
        Thread.sleep(10000)
    }
}