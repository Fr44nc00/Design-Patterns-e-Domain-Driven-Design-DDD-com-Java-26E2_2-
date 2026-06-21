package com.francombbs.at_ddd.infrastructure;

import com.francombbs.at_ddd.domain.music.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
}
