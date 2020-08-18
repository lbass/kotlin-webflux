package com.lbass.kotlinhttp.api.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.EntityResponse.fromObject
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class DemoRouter {
    @Bean
    fun routerFunction() = nest(path("/api"),
            router {
                listOf(
                        GET("/hello") {
                            Thread.sleep(3000)
                            fromObject("Hello World!!").build()
                        },
                        GET("/hello2") {
                            Thread.sleep(3000)
                            ServerResponse.ok().bodyValue("Hello World2!!")
                        }
                )
            }
    )
}