package com.francombbs.at_ddd.domain.music;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class Musica {

    @Id
    private UUID id;

    private String titulo;
    private String artista;
    private String album;
}
