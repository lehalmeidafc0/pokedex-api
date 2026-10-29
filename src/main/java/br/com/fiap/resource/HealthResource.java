package br.com.fiap.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// A plataforma do deploy chama o /health para saber se a API está no ar.
// Não acessa o banco, só responde OK.
@RestController
public class HealthResource {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
