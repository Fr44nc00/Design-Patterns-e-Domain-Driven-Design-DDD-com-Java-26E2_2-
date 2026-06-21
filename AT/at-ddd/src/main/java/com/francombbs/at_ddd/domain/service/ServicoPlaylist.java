package com.francombbs.at_ddd.domain.service;

import com.francombbs.at_ddd.domain.music.Musica;
import com.francombbs.at_ddd.domain.music.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoPlaylist {

    public void adicionarMusica(Playlist playlist, Musica musica) {
        playlist.getMusicas().add(musica);
    }

    public void removerMusica(Playlist playlist, Musica musica) {
        playlist.getMusicas().remove(musica);
    }

    public List<Musica> listarMusicas(Playlist playlist) {
        return playlist.getMusicas();
    }
}
