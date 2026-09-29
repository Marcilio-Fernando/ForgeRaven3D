package com.exemplo.forgeraven3d.controller;

import com.exemplo.forgeraven3d.model.Impressao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/impressoes")
public class ImpressaoController {

    private final List<Impressao> impressoes = new ArrayList<>();
    private Long proximoId = 1L;

    public ImpressaoController() {
        impressoes.add(new Impressao(proximoId++, "Suporte de headset", "PLA", "Preto", 85.0, 240, "CONCLUIDA"));
        impressoes.add(new Impressao(proximoId++, "Engrenagem de reposição", "PETG", "Azul", 32.5, 95, "IMPRIMINDO"));
        impressoes.add(new Impressao(proximoId++, "Case para Raspberry Pi", "ABS", "Cinza", 60.0, 180, "NA_FILA"));
        impressoes.add(new Impressao(proximoId++, "Vaso decorativo", "PLA", "Branco", 120.0, 420, "NA_FILA"));
    }

    @GetMapping
    public ResponseEntity<List<Impressao>> listar(
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) Integer tempoMax,
            @RequestParam(required = false) Double pesoMin) {

        List<Impressao> resultado = new ArrayList<>();

        for (Impressao i : impressoes) {
            if (material != null && !i.getMaterial().equalsIgnoreCase(material)) continue;
            if (status != null && !i.getStatus().equalsIgnoreCase(status)) continue;
            if (cor != null && !i.getCor().toLowerCase().contains(cor.toLowerCase())) continue;
            if (tempoMax != null && i.getTempoMinutos() > tempoMax) continue;
            if (pesoMin != null && i.getPesoGramas() < pesoMin) continue;
            resultado.add(i);
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Impressao> buscarPorId(@PathVariable Long id) {
        return buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Impressao> cadastrar(@RequestBody Impressao nova) {
        if (nova.getNome() == null || nova.getNome().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        nova.setId(proximoId++);
        if (nova.getStatus() == null) {
            nova.setStatus("NA_FILA");
        }
        impressoes.add(nova);

        URI location = URI.create("/impressoes/" + nova.getId());
        return ResponseEntity.created(location).body(nova);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Impressao> atualizar(@PathVariable Long id,
                                               @RequestBody Impressao dados) {
        Optional<Impressao> existente = buscar(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (dados.getNome() == null || dados.getNome().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Impressao i = existente.get();
        i.setNome(dados.getNome());
        i.setMaterial(dados.getMaterial());
        i.setCor(dados.getCor());
        i.setPesoGramas(dados.getPesoGramas());
        i.setTempoMinutos(dados.getTempoMinutos());
        i.setStatus(dados.getStatus());

        return ResponseEntity.ok(i);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean removido = impressoes.removeIf(i -> i.getId().equals(id));
        return removido
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private Optional<Impressao> buscar(Long id) {
        return impressoes.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
}