# Design Patterns e Domain-Driven Design (DDD) com Java [AT-DDD]
## Bounded Contexts do Streaming de Música

- Usuários
  - Criação de conta
  - Assinatura de plano
- Transações
  - Autorização de transações
  - Regras antifraude
- Músicas
  - Cadastro de músicas
  - Favoritar músicas
- Playlists
  - Criação de playlists
  - Associação de músicas a playlists vinculadas a usuários
- Pagamentos
  - Processamento de transações
  - Validação de cartão de crédito

---

## Classificação dos Subdomínios
| Subdomínio | Tipo | Justificativa |
|------------|------|---------------|
| Usuários |	Principal	| Núcleo do sistema, gerencia contas e planos de assinatura. |
| Transações |	Principal |	Essencial para autorizar compras e aplicar regras antifraude. |
| Músicas |	Principal	| Base da experiência do usuário, envolve cadastro e favoritos. |
| Playlists |	Principal	| Diferencial competitivo, organiza músicas e personaliza experiência. |
| Pagamentos |	Genérico |	Necessário para todas as transações, mas não é diferencial competitivo. |

---

## Mapa de Contexto (Mermaid)
```mermaid
graph TD

  subgraph Usuarios
    U1[Criação de Conta]
    U2[Assinatura de Plano]
  end

  subgraph Transacoes
    T1[Autorização de Transação]
    T2[Antifraude]
  end

  subgraph Musicas
    M1[Cadastro de Músicas]
    M2[Favoritar Músicas]
  end

  subgraph Playlists
    P1[Criação de Playlist]
    P2[Adicionar Músicas]
  end

  Pagamentos[Pagamentos]

  %% Relacionamentos principais
  U2 --> T1
  T1 --> T2
  T1 --> Pagamentos
  U1 --> U2

  %% Música e playlists
  M1 --> M2
  M2 --> P2
  P1 --> P2
  P1 --> U1
```

---

## Diagrama de Classes (Mermaid)
```mermaid
classDiagram
    class Usuario {
        UUID id
        String nome
        String email
        CartaoCredito cartaoCredito
        PlanoAssinatura planoAtivo
        +possuiPlanoAtivo() boolean
        +possuiCartaoValido() boolean
    }

    class CartaoCredito {
        String numero
        boolean ativo
    }

    class PlanoAssinatura {
        <<enum>>
        GRATUITO
        PREMIUM
        FAMILIA
    }

    class Transacao {
        UUID id
        String comerciante
        double valor
        LocalDateTime dataHora
        boolean assinatura
        UUID usuarioId
        +ehAssinatura() boolean
        +ehSimilar(Transacao outra) boolean
    }

    class Musica {
        UUID id
        String titulo
        String artista
        String album
    }

    class Playlist {
        UUID id
        String nome
        List<Musica> musicas
        Usuario usuario
    }

    class ServicoAntifraude {
        +validarTransacao(Usuario, Transacao, List<Transacao>)
    }

    class ServicoPlaylist {
        +adicionarMusica(Playlist, Musica)
        +removerMusica(Playlist, Musica)
        +listarMusicas(Playlist) List<Musica>
    }

    Usuario "1" --> "1" CartaoCredito
    Usuario "1" --> "0..1" PlanoAssinatura
    Usuario "1" --> "0..*" Playlist
    Playlist "1" --> "0..*" Musica
    Transacao "1" --> "1" Usuario : usuarioId
```

---

## Diagrama de Sequência – Transações (Mermaid)
```mermaid
sequenceDiagram
    participant Cliente
    participant UsuarioController
    participant UsuarioRepository
    participant TransacaoController
    participant ServicoAntifraude
    participant TransacaoRepository

    Cliente->>UsuarioController: POST /usuarios
    UsuarioController->>UsuarioRepository: save(usuario)
    UsuarioRepository-->>Cliente: Usuário criado (UUID)

    Cliente->>TransacaoController: POST /transacoes/{usuarioId}
    TransacaoController->>UsuarioRepository: findById(usuarioId)
    UsuarioRepository-->>TransacaoController: Usuario

    TransacaoController->>TransacaoRepository: findByUsuarioId(usuarioId)
    TransacaoRepository-->>TransacaoController: Histórico de transações

    TransacaoController->>ServicoAntifraude: validarTransacao(usuario, transacao, historico)
    ServicoAntifraude-->>TransacaoController: OK ou Exceção (violação antifraude)

    TransacaoController->>TransacaoRepository: save(transacao)
    TransacaoRepository-->>Cliente: Transação registrada
```

---

## Diagrama de Sequência – Playlists (Mermaid)
```mermaid
sequenceDiagram
    participant Cliente
    participant UsuarioController
    participant UsuarioRepository
    participant PlaylistController
    participant PlaylistRepository
    participant MusicaRepository
    participant ServicoPlaylist

    Cliente->>UsuarioController: POST /usuarios
    UsuarioController->>UsuarioRepository: save(usuario)
    UsuarioRepository-->>Cliente: Usuário criado (UUID)

    Cliente->>PlaylistController: POST /playlists/{usuarioId}
    PlaylistController->>UsuarioRepository: findById(usuarioId)
    UsuarioRepository-->>PlaylistController: Usuario
    PlaylistController->>PlaylistRepository: save(playlist)
    PlaylistRepository-->>Cliente: Playlist criada

    Cliente->>PlaylistController: POST /playlists/{playlistId}/musicas/{musicaId}
    PlaylistController->>MusicaRepository: findById(musicaId)
    MusicaRepository-->>PlaylistController: Musica
    PlaylistController->>ServicoPlaylist: adicionarMusica(playlist, musica)
    ServicoPlaylist-->>PlaylistController: Playlist atualizada
    PlaylistController->>PlaylistRepository: save(playlist)
    PlaylistRepository-->>Cliente: Playlist com música adicionada

    Cliente->>PlaylistController: GET /playlists/{playlistId}/musicas
    PlaylistController->>ServicoPlaylist: listarMusicas(playlist)
    ServicoPlaylist-->>Cliente: Lista de músicas
```

---

## Estratégias de Comunicação
- **Usuários ↔ Transações**
  - Estratégia: comunicação interna via API REST.
  - Benefício: garante que apenas usuários válidos possam realizar transações.

- **Transações ↔ Pagamentos**
  - Estratégia: comunicação síncrona via HTTP/JSON.
  - Benefício: confirmação imediata da transação e aplicação das regras antifraude.

- **Usuários ↔ Playlists**
  - Estratégia: integração via API REST.
  - Benefício: playlists vinculadas diretamente ao usuário, sem recursão infinita.

- **Playlists ↔ Músicas**
  - Estratégia: relacionamento via Many-to-Many no banco de dados.
  - Benefício: flexibilidade para adicionar/remover músicas sem duplicação.
