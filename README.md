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

1. Opção 1: ordem 101, Ana Silva, ABC1D23, Honda Civic, 2020, capacidade 3.
2. Opção 7 na ordem 101 → erro **ordem vazia**.
3. Opção 2 três vezes: com peças (150 + 200), por hora (80, 2 h × 100), diagnóstico (120, reparo S).
4. Opção 2 de novo → erro **capacidade excedida**.
5. Opção 6 com 150 → erro **desconto inválido**; com 10 → total R$ 621,00.
6. Opções 4 ("an") e 5 ("abc1d23") → buscas.
7. Opção 7 → FINALIZADA; depois opção 6 ou 2 → erro **ordem finalizada**.

## Interface gráfica e uso de IA

- **Ferramenta:** Claude Code (Anthropic), modelo Claude Opus 5.5.
- **Prompt utilizado:**

> Crie uma interface gráfica em Java Swing para o sistema Oficina Box 42, em um pacote separado `gui`, reutilizando as classes de domínio existentes (GerenciadorOrdens, OrdemServico, Veiculo, ServicoComPecas, ServicoPorHora, ServicoDiagnostico). A interface não deve recalcular preços nem duplicar validações: todo valor exibido vem dos métodos do domínio (calcularSubtotal, calcularTotal, calcularPrecoFinal, exibirResumo) e toda validação acontece pelas exceções que o domínio lança. A janela deve ter: uma tabela com as ordens (número, cliente, placa, estado, nº de serviços, subtotal, desconto, total); uma área com o resumo da ordem selecionada; botões para criar ordem, adicionar serviço (com peças, por hora ou diagnóstico), aplicar desconto, finalizar ordem, buscar por início do nome do cliente e buscar por placa. Trate as exceções personalizadas (ValorInvalidoException, CapacidadeExcedidaException, OrdemFinalizadaException, OrdemVaziaException) e as entradas numéricas inválidas mostrando mensagens em JOptionPane, sem fechar o programa. A aplicação de console deve continuar funcionando sem alterações.

- **Como a interface se integra ao domínio:**
  - Os valores da tabela vêm de `calcularSubtotal()`, `calcularTotal()` e `getPercentualDesconto()`; a GUI não faz nenhuma conta.
  - Os objetos são criados com os construtores do domínio, que validam e lançam `ValorInvalidoException`; a GUI só exibe `e.getMessage()` num `JOptionPane`.
  - O resumo e as buscas reutilizam `exibirResumo()`, `buscarPorInicioDoNome()` e `buscarPorPlaca()`: a saída deles (`System.out`) é redirecionada temporariamente para o texto da janela (método `capturarSaida`).
  - Única alteração no domínio: `GerenciadorOrdens.getOrdem(int)`, para a tabela conseguir percorrer as ordens (o array continua privado).
