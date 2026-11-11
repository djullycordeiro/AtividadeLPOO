package src;

import java.time.Year;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorOrdens gerenciador = new GerenciadorOrdens();
        int opcao;

        do {
            System.out.print("""

                    === OFICINA BOX 42 ===
                    1 - Criar ordem de serviço
                    2 - Adicionar serviço à ordem
                    3 - Listar ordens
                    4 - Buscar ordens pelo nome do cliente
                    5 - Buscar ordem pela placa
                    6 - Sair

                    """);
            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    criarOrdem(scanner, gerenciador);
                    break;
                case 2:
                    adicionarServicoAOrdem(scanner, gerenciador);
                    break;
                case 3:
                    gerenciador.listarOrdens();
                    break;
                case 4:
                    System.out.print("Digite o início do nome do cliente: ");
                    gerenciador.buscarOrdensPorInicioDoNome(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Digite a placa do veículo: ");
                    gerenciador.buscarOrdemPorPlaca(scanner.nextLine());
                    break;
                case 6:
                    System.out.println("Sistema fechado");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 6);

        scanner.close();
    }

    private static void criarOrdem(Scanner scanner, GerenciadorOrdens gerenciador) {
        System.out.println();
        String nomeCliente = lerTexto(scanner, "Nome do cliente: ");
        String placa = lerTexto(scanner, "Placa do veículo: ");

        if (gerenciador.obterOrdemPorPlaca(placa) != null) {
            System.out.println("Já existe uma ordem para essa placa.");
            return;
        }

        String modelo = lerTexto(scanner, "Modelo: ");
        int ano = lerInteiroNoIntervalo(scanner, "Ano: ", 1921, Year.now().getValue() + 1);

        Veiculo veiculo = new Veiculo(placa, modelo, ano);
        gerenciador.adicionarOrdem(new OrdemServico(nomeCliente, veiculo));
    }

    private static void adicionarServicoAOrdem(Scanner scanner, GerenciadorOrdens gerenciador) {
        System.out.print("\nPlaca do veículo da ordem: ");
        OrdemServico ordem = gerenciador.obterOrdemPorPlaca(scanner.nextLine());

        if (ordem == null) {
            System.out.println("Nenhuma ordem encontrada para essa placa.");
            return;
        }

        Servico servico = criarServico(scanner);
        if (servico == null) {
            return;
        }

        if (ordem.adicionarServico(servico)) {
            System.out.println("\nServiço adicionado com sucesso.");
        }
    }

    private static Servico criarServico(Scanner scanner) {
        System.out.print("""

                === TIPO DE SERVIÇO ===
                1 - Serviço comum
                2 - Serviço com peças
                3 - Serviço por hora
                4 - Diagnóstico

                """);
        int tipo = lerInteiro(scanner, "Escolha o tipo: ");

        if (tipo < 1 || tipo > 4) {
            System.out.println("Tipo inválido.");
            return null;
        }

        String descricao = lerTexto(scanner, "Descrição: ");
        double valorBase = lerDoublePositivo(scanner, "Valor base: R$ ");

        switch (tipo) {
            case 1:
                return new Servico(descricao, valorBase);
            case 2: {
                double valorPecas = lerDoublePositivo(scanner, "Valor das peças: R$ ");
                return new ServicoComPecas(descricao, valorBase, valorPecas);
            }
            case 3: {
                int horas = lerInteiroNoIntervalo(scanner, "Horas trabalhadas: ", 0, Integer.MAX_VALUE);
                double valorHora = lerDoublePositivo(scanner, "Valor da hora: R$ ");
                return new ServicoPorHora(descricao, valorBase, horas, valorHora);
            }
            case 4: {
                boolean possuiReparo = lerSimNao(scanner, "O cliente realizou o reparo? (S/N): ");
                return new ServicoDiagnostico(descricao, valorBase, possuiReparo);
            }
            default:
                return null;
        }
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static int lerInteiroNoIntervalo(Scanner scanner, String mensagem, int minimo, int maximo) {
        while (true) {
            int valor = lerInteiro(scanner, mensagem);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Valor fora do permitido. Tente novamente.");
        }
    }

    private static double lerDoublePositivo(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (valor > 0) {
                    return valor;
                }
                System.out.println("O valor deve ser maior que zero.");
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número (ex: 150.50).");
            }
        }
    }

    private static String lerTexto(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo não pode ficar vazio.");
        }
    }

    private static boolean lerSimNao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.equals("S") || resposta.equals("SIM")) {
                return true;
            }
            if (resposta.equals("N") || resposta.equals("NAO") || resposta.equals("NÃO")) {
                return false;
            }
            System.out.println("Responda com S ou N.");
        }
    }
}