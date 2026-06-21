package com.francombbs.at_ddd.infrastructure;

import com.francombbs.at_ddd.domain.transaction.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findByUsuarioId(UUID usuarioId);
}
