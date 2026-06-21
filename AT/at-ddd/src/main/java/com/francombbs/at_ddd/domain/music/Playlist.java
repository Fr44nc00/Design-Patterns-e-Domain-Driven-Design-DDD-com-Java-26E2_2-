package com.francombbs.at_ddd.domain.music;

import com.francombbs.at_ddd.domain.user.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
public class Playlist {

    @Id
    private UUID id;

    private String nome;

    @ManyToMany
    private List<Musica> musicas;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"planoAtivo", "cartaoCredito"})
    private Usuario usuario;
}
