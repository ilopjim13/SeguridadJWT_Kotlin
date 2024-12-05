package com.es.jwtSecurityKotlin.controller

import com.es.jwtSecurityKotlin.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rutas_protegidas")
class RutaProtegidaController {

    @Autowired
    private lateinit var usuarioService: UsuarioService

    @GetMapping("/recurso1")
    fun getRecursoProtegidoUno () : String {
        return "Este recurso sólo puede ser accedido por usuarios registrados en la BDD \uD83E\uDD75"
    }

    @GetMapping("/recurso2")
    fun getRecursoProtegidoPublico () : String {
        return "Este recurso puede ser accedido por cualquiera \uD83E\uDD75"
    }

    @GetMapping("/recurso/{id}")
    fun getRecursoProtegido (@PathVariable id: String) : String {
        return "Obtener recurso por su id $id \uD83E\uDD75"
    }

    @DeleteMapping("/recurso/{id}")
    fun deleteRecursoProtegido (@PathVariable id: String) : String {
        return "Eliminar recurso por su id $id \uD83E\uDD75"
    }


    @GetMapping("/usuario_autenticado")
    fun saludarUsuarioAutenticado(authentication: Authentication) : String {

        // OBJETIVO ES SALUDAR AL USUARIO AUTENTICADO
        return "Hola ${authentication.name}, qué tal?"
    }

    @DeleteMapping("/eliminar/{nombre}")
    fun eliminarUsuario(
        @PathVariable nombre:String,
        authentication: Authentication
    ):String  {

        if (usuarioService.checkUserOrAdmin(authentication, nombre)) {
            return "Hola $nombre, vas a ser eliminado por ${authentication.name}"
        }

        return "F crack"

    }




}