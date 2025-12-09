package app.controller;

import app.dto.bairro.BairroRequestDTO;
import app.dto.bairro.BairroResponseDTO;
import app.service.BairroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bairros")
public class BairroController {

    @Autowired
    private BairroService bairroService;

    // ======== CONSULTAS ========

    @GetMapping
    public ResponseEntity<List<BairroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(bairroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bairroService.buscarPorId(id));
    }

    // ======== CRUD ========

    @PostMapping
    public ResponseEntity<BairroResponseDTO> criar(@RequestBody @Valid BairroRequestDTO dto) {
        BairroResponseDTO salvo = bairroService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid BairroRequestDTO dto) {

        BairroResponseDTO atualizado = bairroService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bairroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
