package com.lbass.kotlinhttp

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.kotlin.core.publisher.toMono

@SpringBootTest
@AutoConfigureWebTestClient
class SyncWebClientRouterTest {

    @Autowired
    private lateinit var testClient: WebTestClient

    @Test
    fun get() {
        for (i in 1..10) {
            println("$i 번째 시도 =====")
            testClient.get().uri("/api/hello2")
                    .exchange()
                    .returnResult<String>()
                    .responseBody
                    .toMono().subscribe { println(it) }
        }
    }
}