package com.francombbs.at_ddd.interfaces;

import com.francombbs.at_ddd.domain.music.Musica;
import com.francombbs.at_ddd.domain.music.Playlist;
import com.francombbs.at_ddd.domain.service.ServicoPlaylist;
import com.francombbs.at_ddd.domain.user.Usuario;
import com.francombbs.at_ddd.infrastructure.UsuarioRepository;
import com.francombbs.at_ddd.infrastructure.MusicaRepository;
import com.francombbs.at_ddd.infrastructure.PlaylistRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final MusicaRepository musicaRepository;
    private final ServicoPlaylist servicoPlaylist;
    private final UsuarioRepository usuarioRepository;

    public PlaylistController(PlaylistRepository playlistRepository,
                              MusicaRepository musicaRepository,
                              ServicoPlaylist servicoPlaylist,
                              UsuarioRepository usuarioRepository) {
        this.playlistRepository = playlistRepository;
        this.musicaRepository = musicaRepository;
        this.servicoPlaylist = servicoPlaylist;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/{usuarioId}")
    public Playlist criarPlaylist(@PathVariable UUID usuarioId, @RequestBody Playlist playlist) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        playlist.setId(UUID.randomUUID());
        playlist.setUsuario(usuario);
        return playlistRepository.save(playlist);
    }

    @PostMapping("/{playlistId}/musicas/{musicaId}")
    public Playlist adicionarMusica(@PathVariable UUID playlistId, @PathVariable UUID musicaId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist não encontrada"));
        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() -> new RuntimeException("Música não encontrada"));

        servicoPlaylist.adicionarMusica(playlist, musica);
        return playlistRepository.save(playlist);
    }

    @GetMapping("/{playlistId}/musicas")
    public List<Musica> listarMusicas(@PathVariable UUID playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist não encontrada"));
        return servicoPlaylist.listarMusicas(playlist);
    }
}
