# AtividadeLPOO — Oficina Box 42

Sistema de gerenciamento de ordens de serviço (console), em Java.

## Como compilar e executar

Na pasta raiz do projeto (a que contém `src/`):

```bash
javac -encoding UTF-8 -d out -sourcepath src src/app/Main.java
java -cp out app.Main
```

Interface gráfica (Swing):

```bash
javac -encoding UTF-8 -d out -sourcepath src src/gui/JanelaPrincipal.java
java -cp out gui.JanelaPrincipal
```

Requer JDK 14 ou superior.

## Estrutura de pastas (pacotes)

```
src/
├── app/            Main (menu de console, try/catch)
├── gui/            JanelaPrincipal (interface gráfica Swing)
├── modelo/         Veiculo, OrdemServico (implementa Descontavel)
│   └── servico/    Servico (abstrata), ServicoComPecas, ServicoPorHora, ServicoDiagnostico
├── gerenciador/    GerenciadorOrdens (implementa Buscavel)
├── interfaces/     Descontavel, Buscavel
└── excecoes/       ValorInvalidoException, OrdemVaziaException,
                    CapacidadeExcedidaException, OrdemFinalizadaException
```

## Roteiro de demonstração (exemplo do enunciado)

Este roteiro mostra tudo o que a atividade pede, usando o exemplo do enunciado (cliente Ana Silva).

**Como usar:** rode o programa (`java -cp out app.Main`). Sempre que aparecer
`Escolha uma opção:`, siga o próximo passo abaixo. Em cada passo, digite as respostas
**uma por vez, apertando Enter depois de cada uma**, na ordem em que o programa pergunta.

O menu do programa, para referência:

```
1 - Criar ordem de serviço      5 - Buscar ordens por placa
2 - Adicionar serviço à ordem   6 - Aplicar desconto
3 - Listar ordens               7 - Finalizar ordem
4 - Buscar ordens por cliente   8 - Sair
```

### Passo 1 — Criar a ordem da Ana Silva

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `1` (criar ordem) |
| Número da ordem | `101` |
| Nome do cliente | `Ana Silva` |
| Placa | `ABC1D23` |
| Modelo | `Honda Civic` |
| Ano | `2020` |
| Capacidade máxima de serviços | `3` |

✅ Resultado: "Ordem nº 101 criada com sucesso."

### Passo 2 — ERRO 1: tentar finalizar a ordem ainda vazia

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `7` (finalizar ordem) |
| Número da ordem | `101` |

❌ Resultado esperado: erro de **ordem vazia**, porque a ordem ainda não tem nenhum serviço.

### Passo 3 — Adicionar o 1º serviço: troca de pastilhas (com peças)

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `2` (adicionar serviço) |
| Número da ordem | `101` |
| Tipo | `1` (com peças) |
| Descrição | `Troca de pastilhas` |
| Valor base | `150` |
| Valor das peças | `200` |

✅ Resultado: preço calculado R$ 350,00 (150 + 200).

### Passo 4 — Adicionar o 2º serviço: reparo elétrico (por hora)

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `2` (adicionar serviço) |
| Número da ordem | `101` |
| Tipo | `2` (por hora) |
| Descrição | `Reparo eletrico` |
| Valor base | `80` |
| Horas trabalhadas | `2` |
| Valor da hora | `100` |

✅ Resultado: preço calculado R$ 280,00 (80 + 2 × 100).

### Passo 5 — Adicionar o 3º serviço: diagnóstico com reparo

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `2` (adicionar serviço) |
| Número da ordem | `101` |
| Tipo | `3` (diagnóstico) |
| Descrição | `Diagnostico` |
| Valor base | `120` |
| Possui reparo? | `S` |

✅ Resultado: preço calculado R$ 60,00 (metade de 120).

### Passo 6 — ERRO 2: tentar adicionar um 4º serviço

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `2` (adicionar serviço) |
| Número da ordem | `101` |
| Tipo | `1` |
| Descrição | `Extra` |
| Valor base | `10` |
| Valor das peças | `10` |

❌ Resultado esperado: erro de **capacidade excedida**, porque a ordem só aceita 3 serviços.

### Passo 7 — ERRO 3: dar um desconto inválido

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `6` (aplicar desconto) |
| Número da ordem | `101` |
| Percentual de desconto | `150` |

❌ Resultado esperado: erro de **desconto inválido**, porque o desconto tem que estar entre 0 e 100.

### Passo 8 — Dar um desconto válido de 10%

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `6` (aplicar desconto) |
| Número da ordem | `101` |
| Percentual de desconto | `10` |

✅ Resultado: novo total **R$ 621,00** (subtotal R$ 690,00 − 10%).

### Passo 9 — Buscar a ordem pelo nome do cliente

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `4` (buscar por cliente) |
| Início do nome do cliente | `an` |

✅ Resultado: aparece a ordem da Ana Silva (a busca ignora maiúsculas e minúsculas).

### Passo 10 — Buscar a ordem pela placa

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `5` (buscar por placa) |
| Placa do veículo | `abc1d23` |

✅ Resultado: aparece a mesma ordem.

### Passo 11 — Finalizar a ordem

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `7` (finalizar ordem) |
| Número da ordem | `101` |

✅ Resultado: "Ordem nº 101 FINALIZADA com sucesso."

### Passo 12 — ERRO 4: tentar mudar uma ordem já finalizada

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `6` (aplicar desconto) |
| Número da ordem | `101` |
| Percentual de desconto | `5` |

❌ Resultado esperado: erro de **ordem finalizada**, porque uma ordem finalizada não pode mais ser alterada.

### Passo 13 — Sair

| O programa pergunta | Digite |
|---|---|
| Escolha uma opção | `8` (sair) |

**Pronto!** Você mostrou a criação da ordem, os 3 tipos de serviço, o cálculo do preço,
o desconto, as duas buscas, a finalização e os 4 erros tratados (o programa avisa e não fecha).

## Interface gráfica e uso de IA

- **Ferramenta:** Claude Code (Anthropic), modelo Claude Opus 5.5.
- **Prompt utilizado:**

> Crie uma interface gráfica em Java Swing para o sistema Oficina Box 42, em um pacote separado `gui`, reutilizando as classes de domínio existentes (GerenciadorOrdens, OrdemServico, Veiculo, ServicoComPecas, ServicoPorHora, ServicoDiagnostico). A interface não deve recalcular preços nem duplicar validações: todo valor exibido vem dos métodos do domínio (calcularSubtotal, calcularTotal, calcularPrecoFinal, exibirResumo) e toda validação acontece pelas exceções que o domínio lança. A janela deve ter: uma tabela com as ordens (número, cliente, placa, estado, nº de serviços, subtotal, desconto, total); uma área com o resumo da ordem selecionada; botões para criar ordem, adicionar serviço (com peças, por hora ou diagnóstico), aplicar desconto, finalizar ordem, buscar por início do nome do cliente e buscar por placa. Trate as exceções personalizadas (ValorInvalidoException, CapacidadeExcedidaException, OrdemFinalizadaException, OrdemVaziaException) e as entradas numéricas inválidas mostrando mensagens em JOptionPane, sem fechar o programa. A aplicação de console deve continuar funcionando sem alterações.

- **Como a interface se integra ao domínio:**
  - Os valores da tabela vêm de `calcularSubtotal()`, `calcularTotal()` e `getPercentualDesconto()`; a GUI não faz nenhuma conta.
  - Os objetos são criados com os construtores do domínio, que validam e lançam `ValorInvalidoException`; a GUI só exibe `e.getMessage()` num `JOptionPane`.
  - O resumo e as buscas reutilizam `exibirResumo()`, `buscarPorInicioDoNome()` e `buscarPorPlaca()`: a saída deles (`System.out`) é redirecionada temporariamente para o texto da janela (método `capturarSaida`).
  - Única alteração no domínio: `GerenciadorOrdens.getOrdem(int)`, para a tabela conseguir percorrer as ordens (o array continua privado).
