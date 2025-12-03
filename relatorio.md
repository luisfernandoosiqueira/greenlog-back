# 🐞 BUG-001: http://localhost:4200/caminhao - Erro ao tentar atualizar o caminhão.

**O que está acontecendo:**
O campo CPF não está sendo validado ao ser cadastrado, apenas cpf valido deve ser aceito.

**Onde:**
* **Endpoint:** `POST /api/motorista`
* **URL Afetada:** `http://localhost:4200/motorista`