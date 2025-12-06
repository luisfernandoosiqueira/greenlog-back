package app.repository;

import app.entity.Itinerario;
import app.entity.Rota;
import app.enums.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    // Busca itinerários agendados entre duas datas (inclusive), usando SQL nativo
    @Query(
      value = "SELECT * " +
              "FROM TB_ITINERARIO i " +
              "WHERE i.data_agendamento BETWEEN :inicio AND :fim",
      nativeQuery = true
    )
    List<Itinerario> findByDataAgendamentoBetween(
        @Param("inicio") LocalDate inicio,
        @Param("fim")    LocalDate fim
    );

    // Busca todos os itinerários de um caminhão específico, identificado pela placa, usando SQL nativo
    @Query(
      value = "SELECT i.* " +
              "FROM TB_ITINERARIO i " +
              "  JOIN TB_ROTA r ON i.rota_id = r.id " +
              "WHERE r.caminhao_placa = :placa",
      nativeQuery = true
    )
    List<Itinerario> findByRota_Caminhao_Placa(
        @Param("placa") String placa
    );

    // Busca todos os itinerários de um caminhão em um intervalo de datas, usando SQL nativo
    @Query(
      value = "SELECT i.* " +
              "FROM TB_ITINERARIO i " +
              "  JOIN TB_ROTA r ON i.rota_id = r.id " +
              "WHERE r.caminhao_placa = :placa " +
              "  AND i.data_agendamento BETWEEN :inicio AND :fim",
      nativeQuery = true
    )
    List<Itinerario> findByRota_Caminhao_PlacaAndDataAgendamentoBetween(
        @Param("placa")  String    placa,
        @Param("inicio") LocalDate inicio,
        @Param("fim")    LocalDate fim
    );

    // Verifica se existe itinerário para um caminhão em uma data específica (JPQL)
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
           "FROM Itinerario i " +
           "WHERE i.rota.caminhao.placa = :placa " +
           "  AND FUNCTION('DATE', i.dataAgendamento) = :data")
    boolean existsByCaminhaoAndData(
        @Param("placa") String placa,
        @Param("data")  LocalDate data
    );

    // Filtro por tipo de resíduo da rota associada (JPQL)
    @Query("SELECT i " +
           "FROM Itinerario i " +
           "WHERE i.rota.tipoResiduo = :tipoResiduo")
    List<Itinerario> findByTipoResiduo(
        @Param("tipoResiduo") TipoResiduo tipoResiduo
    );

    // Usado no RotaService.excluir para bloquear exclusão de rota com itinerário vinculado
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
           "FROM Itinerario i " +
           "WHERE i.rota = :rota")
    boolean existsByRota(@Param("rota") Rota rota);

    // impedir mesmo motorista em dois itinerários no mesmo dia
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
           "FROM Itinerario i " +
           "WHERE i.rota.caminhao.motorista.cpf = :cpf " +
           "  AND FUNCTION('DATE', i.dataAgendamento) = :data")
    boolean existsByMotoristaAndData(
        @Param("cpf")  String cpf,
        @Param("data") LocalDate data
    );

    // impedir ponto de coleta em dois itinerários diferentes no mesmo dia
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
           "FROM Itinerario i " +
           "JOIN i.rota.pontosColeta pc " +
           "WHERE pc.id IN :pontosIds " +
           "  AND FUNCTION('DATE', i.dataAgendamento) = :data")
    boolean existsByPontoColetaAndData(
        @Param("pontosIds") List<Long> pontosIds,
        @Param("data")      LocalDate data
    );
}
