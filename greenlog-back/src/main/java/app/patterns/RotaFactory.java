package app.patterns;

import app.entity.Bairro;
import app.entity.Caminhao;
import app.entity.Rota;
import app.entity.RuaConexao;
import app.entity.TrechoRota;
import app.entity.TipoResiduoModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Padrão Factory Method aplicado à criação de rotas e trechos.
 * Centraliza a lógica de montagem da entidade Rota a partir
 * dos caminhos (listas de RuaConexao) retornados pelo cálculo
 * de menor caminho.
 */
public final class RotaFactory {

    private RotaFactory() {
    }

    /**
     * Cria uma rota completa com seus trechos, a partir dos caminhos
     * já calculados entre os bairros consecutivos.
     *
     * @param nome              nome da rota
     * @param tipoResiduo       tipo de resíduo transportado
     * @param caminhao          caminhão associado à rota
     * @param pesoTotal         peso total estimado (kg)
     * @param caminhosPorTrecho lista de caminhos; cada posição representa
     *                          um trecho e contém a lista ordenada de
     *                          RuaConexao entre origem e destino desse trecho
     * @return Rota pronta para ser persistida
     */
    public static Rota criarRota(String nome,
                                 TipoResiduoModel tipoResiduo,
                                 Caminhao caminhao,
                                 Double pesoTotal,
                                 List<List<RuaConexao>> caminhosPorTrecho) {

        double distanciaTotal = 0.0;
        List<TrechoRota> trechos = new ArrayList<>();

        // data de criação definida no momento da fábrica
        Rota rota = new Rota(
                nome,
                tipoResiduo,
                caminhao,
                LocalDateTime.now(),
                pesoTotal,
                0.0 // será atualizado após somar as distâncias
        );

        for (List<RuaConexao> caminho : caminhosPorTrecho) {
            if (caminho == null || caminho.isEmpty()) {
                continue;
            }

            // origem e destino do trecho vêm da primeira e última conexão
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
