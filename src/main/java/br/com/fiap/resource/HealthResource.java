package br.com.fiap.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint simples usado pela plataforma de Deploy para saber se a API está no ar
 * (health check). Responde 200 OK sem acessar o banco, então é sempre rápido.
 */
@RestController
public class HealthResource {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
