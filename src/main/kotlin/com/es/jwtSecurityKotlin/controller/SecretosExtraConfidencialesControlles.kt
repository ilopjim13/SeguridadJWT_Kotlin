package com.es.jwtSecurityKotlin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/secretos_extra_confidenciales")
class SecretosExtraConfidencialesControlles {

    @GetMapping("/ficha1")
    fun getSecretos():String {
        return "HEHE"
    }

    @GetMapping("/ficha2")
    fun getSecretos2():String {
        return "HEHE PUBLICO"
    }

}