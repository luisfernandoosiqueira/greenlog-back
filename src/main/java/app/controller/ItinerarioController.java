package app.controller;

import app.dto.itinerario.ItinerarioRequestDTO;
import app.dto.itinerario.ItinerarioResponseDTO;
import app.service.ItinerarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerarios")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    public ItinerarioController(ItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    // GET /api/itinerarios  ou  /api/itinerarios?data=yyyy-MM-dd
    @GetMapping
    public ResponseEntity<List<ItinerarioResponseDTO>> listar(
            @RequestParam(name = "data", required = false) String data
    ) {
        if (data != null && !data.isBlank()) {
            return ResponseEntity.ok(itinerarioService.listarPorData(data));
        }
        return ResponseEntity.ok(itinerarioService.listarTodos());
    }

    // GET /api/itinerarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ItinerarioResponseDTO> buscarPorId(@PathVariable Long id) {
        ItinerarioResponseDTO dto = itinerarioService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // POST /api/itinerarios
    @PostMapping
    public ResponseEntity<ItinerarioResponseDTO> criar(
            @RequestBody @Valid ItinerarioRequestDTO dto
    ) {
        ItinerarioResponseDTO criado = itinerarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // PUT /api/itinerarios/{id}/cancelar
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ItinerarioResponseDTO> cancelar(@PathVariable Long id) {
        ItinerarioResponseDTO dto = itinerarioService.cancelar(id);
        return ResponseEntity.ok(dto);
    }
}
