package app.controller;

import app.enums.TipoResiduo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tipos-residuo")
public class TipoResiduoController {

    // GET /api/tipos-residuo
    @GetMapping
    public ResponseEntity<List<TipoResiduo>> listarTipos() {
        List<TipoResiduo> tipos = Arrays.asList(TipoResiduo.values());
        return ResponseEntity.ok(tipos);
    }
}
