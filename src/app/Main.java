package app;

import excecoes.*;
import gerenciador.GerenciadorOrdens;
import modelo.OrdemServico;
import modelo.Veiculo;
import modelo.servico.*;

import java.util.Scanner;

public class Main {
    private static final int CAPACIDADE_ORDENS = 20;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorOrdens gerenciador;
        try {
            gerenciador = new GerenciadorOrdens(CAPACIDADE_ORDENS);
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao iniciar: " + e.getMessage());
            return;
        }

        int opcao;
        do {
            System.out.println();
            System.out.println("=== OFICINA BOX 42 ===");
            System.out.println("1 - Criar ordem de serviço");
            System.out.println("2 - Adicionar serviço à ordem");
            System.out.println("3 - Listar ordens");
            System.out.println("4 - Buscar ordens por cliente");
            System.out.println("5 - Buscar ordens por placa");
            System.out.println("6 - Aplicar desconto");
            System.out.println("7 - Finalizar ordem");
            System.out.println("8 - Sair");
            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> criarOrdem(scanner, gerenciador);
                    case 2 -> adicionarServico(scanner, gerenciador);
                    case 3 -> gerenciador.listarOrdens();
                    case 4 -> gerenciador.buscarPorInicioDoNome(lerLinha(scanner, "Início do nome do cliente: "));
                    case 5 -> gerenciador.buscarPorPlaca(lerLinha(scanner, "Placa do veículo: "));
                    case 6 -> aplicarDesconto(scanner, gerenciador);
                    case 7 -> finalizarOrdem(scanner, gerenciador);
                    case 8 -> System.out.println("Sistema encerrado.");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (ValorInvalidoException e) {
                System.out.println("Erro de validação: " + e.getMessage());
            } catch (CapacidadeExcedidaException e) {
                System.out.println("Capacidade excedida: " + e.getMessage());
            } catch (OrdemFinalizadaException e) {
                System.out.println("Ordem finalizada: " + e.getMessage());
            } catch (OrdemVaziaException e) {
                System.out.println("Ordem vazia: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        } while (opcao != 8);

        scanner.close();
    }

    private static void criarOrdem(Scanner scanner, GerenciadorOrdens gerenciador)
            throws ValorInvalidoException, CapacidadeExcedidaException {
        int numero = lerInteiro(scanner, "Número da ordem: ");
        String nome = lerLinha(scanner, "Nome do cliente: ");
        String placa = lerLinha(scanner, "Placa: ");
        String modelo = lerLinha(scanner, "Modelo: ");
        int ano = lerInteiro(scanner, "Ano: ");
        int capacidade = lerInteiro(scanner, "Capacidade máxima de serviços: ");

        Veiculo veiculo = new Veiculo(placa, modelo, ano);
        OrdemServico ordem = new OrdemServico(numero, nome, veiculo, capacidade);
        gerenciador.adicionarOrdem(ordem);
        System.out.println("Ordem nº " + numero + " criada com sucesso.");
    }

    private static OrdemServico selecionarOrdem(Scanner scanner, GerenciadorOrdens gerenciador) {
        int numero = lerInteiro(scanner, "Número da ordem: ");
        OrdemServico ordem = gerenciador.buscarPorNumero(numero);
        if (ordem == null) {
            System.out.println("Ordem nº " + numero + " não encontrada.");
        }
        return ordem;
    }

    private static void adicionarServico(Scanner scanner, GerenciadorOrdens gerenciador)
            throws ValorInvalidoException, CapacidadeExcedidaException, OrdemFinalizadaException {
        OrdemServico ordem = selecionarOrdem(scanner, gerenciador);
        if (ordem == null) return;

        System.out.println("Tipo de serviço: 1 - Com peças | 2 - Por hora | 3 - Diagnóstico");
        int tipo = lerInteiro(scanner, "Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo de serviço inválido.");
            return;
        }
        String descricao = lerLinha(scanner, "Descrição: ");
        double valorBase = lerDouble(scanner, "Valor base: R$ ");

        Servico servico;
        if (tipo == 1) {
            double pecas = lerDouble(scanner, "Valor das peças: R$ ");
            servico = new ServicoComPecas(descricao, valorBase, pecas);
        } else if (tipo == 2) {
            double horas = lerDouble(scanner, "Horas trabalhadas: ");
            double valorHora = lerDouble(scanner, "Valor da hora: R$ ");
            servico = new ServicoPorHora(descricao, valorBase, horas, valorHora);
        } else {
            boolean reparo = lerSimNao(scanner, "Possui reparo? (S/N): ");
            servico = new ServicoDiagnostico(descricao, valorBase, reparo);
        }
        ordem.adicionarServico(servico);
        System.out.printf("Serviço adicionado. Preço calculado: R$ %.2f%n", servico.calcularPrecoFinal());
    }

    private static void aplicarDesconto(Scanner scanner, GerenciadorOrdens gerenciador)
            throws ValorInvalidoException, OrdemFinalizadaException {
        OrdemServico ordem = selecionarOrdem(scanner, gerenciador);
        if (ordem == null) return;
        double percentual = lerDouble(scanner, "Percentual de desconto (0-100): ");
        double novoTotal = ordem.aplicarDesconto(percentual);
        System.out.printf("Desconto aplicado. Novo total: R$ %.2f%n", novoTotal);
    }

    private static void finalizarOrdem(Scanner scanner, GerenciadorOrdens gerenciador)
            throws OrdemVaziaException, OrdemFinalizadaException {
        OrdemServico ordem = selecionarOrdem(scanner, gerenciador);
        if (ordem == null) return;
        ordem.finalizar();
        System.out.println("Ordem nº " + ordem.getNumero() + " FINALIZADA com sucesso.");
    }

    // ---------- leitura do console (trata erros de digitação permitindo nova tentativa) ----------

    private static String lerLinha(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        if (!scanner.hasNextLine()) {
            System.exit(0);
        }
        return scanner.nextLine();
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerLinha(scanner, mensagem).trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerLinha(scanner, mensagem).trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número (ex: 150.50).");
            }
        }
    }

    private static boolean lerSimNao(Scanner scanner, String mensagem) {
        while (true) {
            String r = lerLinha(scanner, mensagem).trim().toUpperCase();
            if (r.equals("S") || r.equals("SIM")) return true;
            if (r.equals("N") || r.equals("NAO") || r.equals("NÃO")) return false;
            System.out.println("Responda com S ou N.");
        }
    }
}
