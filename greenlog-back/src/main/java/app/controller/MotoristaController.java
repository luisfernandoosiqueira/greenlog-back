package app.controller;

import app.dto.motorista.MotoristaRequestDTO;
import app.dto.motorista.MotoristaResponseDTO;
import app.enums.StatusMotorista;
import app.service.MotoristaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {

    @Autowired
    private MotoristaService motoristaService;

    // ========= CONSULTAS =========

    @GetMapping
    public ResponseEntity<List<MotoristaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(motoristaService.listarTodos());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MotoristaResponseDTO>> listarPorStatus(@PathVariable StatusMotorista status) {
        return ResponseEntity.ok(motoristaService.listarPorStatus(status));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MotoristaResponseDTO>> listarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(motoristaService.listarPorNome(nome));
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<MotoristaResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(motoristaService.buscarPorCpf(cpf));
    }

    // ========= CRUD =========

    @PostMapping
    public ResponseEntity<MotoristaResponseDTO> criar(@RequestBody @Valid MotoristaRequestDTO dto) {
        MotoristaResponseDTO salvo = motoristaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<MotoristaResponseDTO> atualizar(
            @PathVariable String cpf,
            @RequestBody @Valid MotoristaRequestDTO dto) {

        MotoristaResponseDTO atualizado = motoristaService.atualizar(cpf, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        motoristaService.excluir(cpf);
        return ResponseEntity.noContent().build();
    }
}
