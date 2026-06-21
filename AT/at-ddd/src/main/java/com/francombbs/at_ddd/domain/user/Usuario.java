package com.francombbs.at_ddd.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class Usuario {

    @Id
    private UUID id;

    private String nome;
    private String email;

    @Embedded
    private CartaoCredito cartaoCredito;

    @Enumerated(EnumType.STRING)
    private PlanoAssinatura planoAtivo;

    public boolean possuiPlanoAtivo() {
        return planoAtivo != null;
    }

    public boolean possuiCartaoValido() {
        return cartaoCredito != null && cartaoCredito.isAtivo();
    }
}
