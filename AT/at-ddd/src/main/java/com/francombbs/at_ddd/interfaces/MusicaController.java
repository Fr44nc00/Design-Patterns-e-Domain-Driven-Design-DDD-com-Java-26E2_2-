package com.francombbs.at_ddd.interfaces;

import com.francombbs.at_ddd.domain.music.Musica;
import com.francombbs.at_ddd.infrastructure.MusicaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/musicas")
public class MusicaController {

    private final MusicaRepository musicaRepository;

    public MusicaController(MusicaRepository musicaRepository) {
        this.musicaRepository = musicaRepository;
    }

    @PostMapping
    public Musica criarMusica(@RequestBody Musica musica) {
        musica.setId(UUID.randomUUID());
        return musicaRepository.save(musica);
    }

    @GetMapping
    public List<Musica> listarMusicas() {
        return musicaRepository.findAll();
    }
}
