CREATE TABLE servico
(
    id                     BIGSERIAL PRIMARY KEY,
    descricao              VARCHAR(255)   NOT NULL UNIQUE,
    preco_padrao           NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    tempo_estimado_minutos INTEGER        NOT NULL DEFAULT 0,
    ativo                  BOOLEAN        NOT NULL DEFAULT TRUE,
    data_criacao           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_servico_descricao ON servico (descricao);