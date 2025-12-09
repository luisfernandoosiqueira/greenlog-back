package app.controller;

import app.dto.caminhao.CaminhaoRequestDTO;
import app.dto.caminhao.CaminhaoResponseDTO;
import app.enums.StatusCaminhao;
import app.service.CaminhaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/caminhoes")
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    public CaminhaoController(CaminhaoService caminhaoService) {
        this.caminhaoService = caminhaoService;
    }

    @GetMapping
    public ResponseEntity<List<CaminhaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(caminhaoService.listarTodos());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CaminhaoResponseDTO>> listarPorStatus(@PathVariable StatusCaminhao status) {
        return ResponseEntity.ok(caminhaoService.listarPorStatus(status));
    }

    @GetMapping("/{placa}")
    public ResponseEntity<CaminhaoResponseDTO> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(caminhaoService.buscarPorPlaca(placa));
    }

    @PostMapping
    public ResponseEntity<CaminhaoResponseDTO> criar(@RequestBody @Valid CaminhaoRequestDTO dto) {
        CaminhaoResponseDTO salvo = caminhaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{placa}")
    public ResponseEntity<CaminhaoResponseDTO> atualizar(
            @PathVariable String placa,
            @RequestBody @Valid CaminhaoRequestDTO dto) {

        CaminhaoResponseDTO atualizado = caminhaoService.atualizar(placa, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> excluir(@PathVariable String placa) {
        caminhaoService.excluir(placa);
        return ResponseEntity.noContent().build();
    }
}
