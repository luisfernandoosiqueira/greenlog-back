package app.controller;

import app.dto.pontocoleta.PontoColetaRequestDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/pontos-coleta")
public class PontoColetaController {

    @Autowired
    private PontoColetaService pontoColetaService;

    // ========= CONSULTAS =========

    @GetMapping
    public ResponseEntity<List<PontoColetaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pontoColetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pontoColetaService.buscarPorId(id));
    }

    @GetMapping("/bairro/{bairroId}")
    public ResponseEntity<List<PontoColetaResponseDTO>> listarPorBairro(@PathVariable Long bairroId) {
        return ResponseEntity.ok(pontoColetaService.listarPorBairro(bairroId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PontoColetaResponseDTO>> listarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(pontoColetaService.listarPorNome(nome));
    }

    // ========= CRUD =========

    @PostMapping
    public ResponseEntity<PontoColetaResponseDTO> criar(@RequestBody @Valid PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO salvo = pontoColetaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PontoColetaRequestDTO dto) {

        PontoColetaResponseDTO atualizado = pontoColetaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pontoColetaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
