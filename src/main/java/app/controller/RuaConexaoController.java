package app.controller;

import app.dto.ruaconexao.RuaConexaoRequestDTO;
import app.dto.ruaconexao.RuaConexaoResponseDTO;
import app.service.RuaConexaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ruas-conexoes")
public class RuaConexaoController {

    @Autowired
    private RuaConexaoService ruaConexaoService;

    // ========= CONSULTAS =========

    @GetMapping
    public ResponseEntity<List<RuaConexaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ruaConexaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuaConexaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ruaConexaoService.buscarPorId(id));
    }

    @GetMapping("/origem/{origemId}")
    public ResponseEntity<List<RuaConexaoResponseDTO>> listarPorOrigem(@PathVariable Long origemId) {
        return ResponseEntity.ok(ruaConexaoService.listarPorOrigem(origemId));
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<RuaConexaoResponseDTO>> listarPorDestino(@PathVariable Long destinoId) {
        return ResponseEntity.ok(ruaConexaoService.listarPorDestino(destinoId));
    }

    // ========= CRUD =========

    @PostMapping
    public ResponseEntity<RuaConexaoResponseDTO> criar(@RequestBody @Valid RuaConexaoRequestDTO dto) {
        RuaConexaoResponseDTO salvo = ruaConexaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuaConexaoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid RuaConexaoRequestDTO dto) {

        RuaConexaoResponseDTO atualizado = ruaConexaoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        ruaConexaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
