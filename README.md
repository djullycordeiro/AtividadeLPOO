# AtividadeLPOO — Oficina Box 42

Sistema de gerenciamento de ordens de serviço (console), em Java.

## Como compilar e executar

Na pasta raiz do projeto (a que contém `src/`):

```bash
javac -encoding UTF-8 -d out -sourcepath src src/app/Main.java
java -cp out app.Main
```

Requer JDK 14 ou superior.

## Estrutura de pastas (pacotes)

```
src/
├── app/            Main (menu de console, try/catch)
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
