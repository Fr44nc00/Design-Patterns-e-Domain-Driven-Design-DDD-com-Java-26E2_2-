package com.francombbs.at_ddd.interfaces;

import com.francombbs.at_ddd.domain.user.Usuario;
import com.francombbs.at_ddd.domain.user.PlanoAssinatura;
import com.francombbs.at_ddd.infrastructure.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario) {
        usuario.setId(UUID.randomUUID());
        return usuarioRepository.save(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PutMapping("/{id}/plano")
    public Usuario atualizarPlano(@PathVariable UUID id, @RequestParam PlanoAssinatura plano) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setPlanoAtivo(plano);
        return usuarioRepository.save(usuario);
    }
}
