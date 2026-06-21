package com.francombbs.at_ddd.interfaces;

import com.francombbs.at_ddd.domain.transaction.Transacao;
import com.francombbs.at_ddd.domain.user.Usuario;
import com.francombbs.at_ddd.domain.service.ServicoAntifraude;
import com.francombbs.at_ddd.infrastructure.TransacaoRepository;
import com.francombbs.at_ddd.infrastructure.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoAntifraude servicoAntifraude;

    public TransacaoController(TransacaoRepository transacaoRepository,
                               UsuarioRepository usuarioRepository,
                               ServicoAntifraude servicoAntifraude) {
        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoAntifraude = servicoAntifraude;
    }

    @PostMapping("/{usuarioId}")
    public Transacao criarTransacao(@PathVariable UUID usuarioId, @RequestBody Transacao transacao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Transacao> historico = transacaoRepository.findByUsuarioId(usuarioId);

        servicoAntifraude.validarTransacao(usuario, transacao, historico);

        transacao.setId(UUID.randomUUID());
        transacao.setUsuarioId(usuarioId);
        return transacaoRepository.save(transacao);
    }

    @GetMapping
    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }
}
