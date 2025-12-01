# 🐞 BUG-001: /api/v1/motorista - CPF não está sendo validado.

**O que está acontecendo:**
O campo CPF não está sendo validado ao ser cadastrado, apenas cpf valido deve ser aceito.

**Onde:**
* **Endpoint:** `POST /api/motorista`
* **URL Afetada:** `http://localhost:4200/motorista`

# 🐞 BUG-002: /api/v1/caminhoes - Lista de Residuos.

**O que está acontecendo:**
Estou pasando a lista de residuos do tipo number [4, 5]
Estou recebendo essa messagem de erro.

{
  "timestamp": "2025-11-29T16:35:21.4081567",
  "status": 400,
  "error": "Bad Request",
  "message": "Um ou mais tipos de resíduo não foram encontrados.",
  "fieldErrors": null
}

**Onde:**
* **Endpoint:** `POST /api/caminhoes`
* **URL Afetada:** `http://localhost:4200/caminhao`

# 🐞 BUG-003: /api/v1/ponto-de-coelta - Cadastro de Ponto de Coleta.

**O que está acontecendo:**
Error
"message": "JSON parse error: Cannot deserialize value of type `java.lang.Long` from Boolean value (token `JsonToken.VALUE_FALSE`)",
DTO estão diferente, data de saída são dois valores, entrada e saída.

export interface PontoColetaRequest{
    bairroId: number,
    nome: string,
    responsavel: string,
    telefone: string,
    email: string,
    endereco: string,
    horaEntrada: string,
    horaSaida: string,
    quantidadeResiduosKg: number,
    tipoResiduoId: number[]
}

**Onde:**
* **Endpoint:** `POST /api/bairro`
* **URL Afetada:** `http://localhost:4200/caminhao`