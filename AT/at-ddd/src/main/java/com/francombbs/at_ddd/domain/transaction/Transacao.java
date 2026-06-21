package com.francombbs.at_ddd.domain.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
public class Transacao {

    @Id
    private UUID id;

    private String comerciante;
    private double valor;
    private LocalDateTime dataHora;

    private boolean assinatura;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    public boolean ehAssinatura() {
        return assinatura;
    }

    public boolean ehSimilar(Transacao outra) {
        return this.valor == outra.valor && this.comerciante.equals(outra.comerciante);
    }
}
