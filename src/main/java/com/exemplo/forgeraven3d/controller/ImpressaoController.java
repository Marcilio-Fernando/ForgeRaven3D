package com.exemplo.forgeraven3d.controller;

import com.exemplo.forgeraven3d.model.Impressao;
import com.exemplo.forgeraven3d.repository.ImpressaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/impressoes")
public class ImpressaoController {

    private final ImpressaoRepository repository;

    public ImpressaoController(ImpressaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Impressao>> listar(
            @RequestParam(defaultValue = "") String material,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String cor,
            @RequestParam(defaultValue = "2147483647") Integer tempoMax,
            @RequestParam(defaultValue = "0") Double pesoMin) {

        return ResponseEntity.ok(repository.filtrar(material, status, cor, tempoMax, pesoMin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Impressao> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Impressao> cadastrar(@RequestBody Impressao nova) {
        if (nova.getNome() == null || nova.getNome().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        nova.setId(null);
        if (nova.getStatus() == null) {
            nova.setStatus("NA_FILA");
        }

        Impressao salva = repository.save(nova);

        URI location = URI.create("/impressoes/" + salva.getId());
        return ResponseEntity.created(location).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Impressao> atualizar(@PathVariable Long id,
                                               @RequestBody Impressao dados) {
        Optional<Impressao> existente = repository.findById(id);
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

        return ResponseEntity.ok(repository.save(i));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Optional<Impressao> existente = repository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repository.delete(existente.get());
        return ResponseEntity.noContent().build();
    }
}