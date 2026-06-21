package com.francombbs.at_ddd.domain.service;

import com.francombbs.at_ddd.domain.user.Usuario;
import com.francombbs.at_ddd.domain.transaction.Transacao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicoAntifraude {

    public void validarTransacao(Usuario usuario, Transacao transacao, List<Transacao> historico) {
        if (!usuario.possuiCartaoValido()) {
            throw new IllegalStateException("cartão não ativo");
        }
        if (usuario.possuiPlanoAtivo() && transacao.ehAssinatura()) {
            throw new IllegalStateException("usuário já possui plano ativo");
        }
        long transacoesRecentes = historico.stream()
                .filter(t -> t.getDataHora().isAfter(LocalDateTime.now().minusMinutes(2)))
                .count();
        if (transacoesRecentes > 3) {
            throw new IllegalStateException("alta-frequência-pequeno-intervalo");
        }
        long duplicadas = historico.stream()
                .filter(t -> t.ehSimilar(transacao))
                .filter(t -> t.getDataHora().isAfter(LocalDateTime.now().minusMinutes(2)))
                .count();
        if (duplicadas >= 2) {
            throw new IllegalStateException("transação duplicada");
        }
    }
}
