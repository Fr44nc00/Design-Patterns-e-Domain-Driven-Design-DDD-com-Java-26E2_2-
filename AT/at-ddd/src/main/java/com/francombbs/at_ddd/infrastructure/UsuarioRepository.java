package com.francombbs.at_ddd.infrastructure;

import com.francombbs.at_ddd.domain.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}
