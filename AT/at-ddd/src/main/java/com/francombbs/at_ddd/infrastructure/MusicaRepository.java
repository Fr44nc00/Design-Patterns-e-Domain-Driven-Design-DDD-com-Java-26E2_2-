package com.francombbs.at_ddd.infrastructure;

import com.francombbs.at_ddd.domain.music.Musica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MusicaRepository extends JpaRepository<Musica, UUID> {
}
