package com.lbass.kotlinhttp

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient


@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient? {
        /**
         * 기본 생성자도 제공한다.
         */
        // WebClient.create();
        // WebClient.create(String baseUrl);

        val exchangeStrategies = ExchangeStrategies.builder()
                .codecs { configurer: ClientCodecConfigurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50) }
                .build()
        exchangeStrategies
                .messageWriters().stream()
                .filter { obj: HttpMessageWriter<*>? -> LoggingCodecSupport::class.java.isInstance(obj) }
                .forEach { writer: HttpMessageWriter<*> -> (writer as LoggingCodecSupport).isEnableLoggingRequestDetails = true }
        return WebClient.builder()
                .clientConnector(
                        ReactorClientHttpConnector(
                                HttpClient
                                        .create()
                                        .tcpConfiguration { client ->
                                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000)
                                                    .doOnConnected { conn ->
                                                        conn.addHandlerLast(ReadTimeoutHandler(180))
                                                                .addHandlerLast(WriteTimeoutHandler(180))
                                                    }
                                        }
                        )
                )
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
                .build()
    }
}