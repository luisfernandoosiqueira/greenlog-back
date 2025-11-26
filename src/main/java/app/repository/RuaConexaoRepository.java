package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.Bairro;
import app.entity.RuaConexao;

@Repository
public interface RuaConexaoRepository extends JpaRepository<RuaConexao, Long> {

    List<RuaConexao> findByOrigem(Bairro origem);

    List<RuaConexao> findByDestino(Bairro destino);

    @Query("""
           select rc 
           from RuaConexao rc
           join fetch rc.origem
           join fetch rc.destino
           """)
    List<RuaConexao> findAllComBairros();
}
