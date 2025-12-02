package app.patterns;

import app.entity.Bairro;
import app.entity.Caminhao;
import app.entity.Rota;
import app.entity.RuaConexao;
import app.entity.TrechoRota;
import app.enums.TipoResiduo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class RotaFactory {

    private RotaFactory() {
    }

    public static Rota criarRota(String nome,
                                 TipoResiduo tipoResiduo,
                                 Caminhao caminhao,
                                 Double pesoTotal,
                                 List<List<RuaConexao>> caminhosPorTrecho) {

        double distanciaTotal = 0.0;
        List<TrechoRota> trechos = new ArrayList<>();

        Rota rota = new Rota(
                nome,
                tipoResiduo,
                caminhao,
                LocalDateTime.now(),
                pesoTotal,
                0.0
        );

        for (List<RuaConexao> caminho : caminhosPorTrecho) {
            if (caminho == null || caminho.isEmpty()) {
                continue;
            }

            RuaConexao primeira = caminho.get(0);
            RuaConexao ultima = caminho.get(caminho.size() - 1);

            Bairro origem = primeira.getOrigem();
            Bairro destino = ultima.getDestino();

            TrechoRota trecho = new TrechoRota(rota, origem, destino);
            trecho.setRuas(caminho);
            trechos.add(trecho);

            for (RuaConexao rc : caminho) {
                if (rc.getDistanciaKm() != null) {
                    distanciaTotal += rc.getDistanciaKm();
                }
            }
        }

        rota.setDistanciaTotal(distanciaTotal);
        rota.setTrechos(trechos);

        return rota;
    }
}
