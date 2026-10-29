package br.com.fiap.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extra (opcional): quando o Bean Validation barra um JSON inválido,
 * devolvemos 400 Bad Request com a mensagem de cada campo que deu erro,
 * em vez da resposta genérica do Spring. Ajuda muito no Insomnia e no Front.
 */
@RestControllerAdvice
public class ValidacaoHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarErrosDeValidacao(MethodArgumentNotValidException e) {
        Map<String, String> erros = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }
}
