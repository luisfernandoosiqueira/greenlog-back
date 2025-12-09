package app.controller;

import app.dto.rota.RotaRequestDTO;
import app.dto.rota.RotaResponseDTO;
import app.service.RotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    @Autowired
    private RotaService rotaService;

    // ========= CONSULTAS =========

    @GetMapping
    public ResponseEntity<List<RotaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(rotaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rotaService.buscarPorId(id));
    }

    // ========= CRIAÇÃO =========

    @PostMapping
    public ResponseEntity<RotaResponseDTO> criar(@RequestBody @Valid RotaRequestDTO dto) {
        RotaResponseDTO salva = rotaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // ========= ATUALIZAÇÃO =========

    @PutMapping("/{id}")
    public ResponseEntity<RotaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid RotaRequestDTO dto) {

        RotaResponseDTO atualizada = rotaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    // ========= EXCLUSÃO =========

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        rotaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
