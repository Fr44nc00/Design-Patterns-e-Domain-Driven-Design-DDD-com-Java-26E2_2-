package com.francombbs.at_ddd.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class CartaoCredito {
    private String numero;
    private boolean ativo;
}
