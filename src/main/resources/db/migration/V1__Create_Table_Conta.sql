CREATE TABLE Conta (
    Id SERIAL PRIMARY KEY,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor NUMERIC(10, 2) NOT NULL,
    descricao TEXT NOT NULL,
    situacao VARCHAR(20) NOT NULL,
    CHECK (situacao IN ('PENDENTE', 'PAGO', 'CANCELADO'))
);
