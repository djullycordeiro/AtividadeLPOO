package src;

public class OrdemServico {
    private static final int MAX_SERVICOS = 10;

    private String nomeCliente;
    private Veiculo veiculo;
    private Servico[] servicos;
    private int quantidadeServicos;

    public OrdemServico(String nomeCliente, Veiculo veiculo) {
        this.nomeCliente = nomeCliente;
        this.veiculo = veiculo;
        this.servicos = new Servico[MAX_SERVICOS];
        this.quantidadeServicos = 0;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public boolean adicionarServico(Servico servico) {
        if (quantidadeServicos >= servicos.length) {
            System.out.println("Limite de serviços desta ordem atingido, não é possível adicionar mais.");
            return false;
        }
        servicos[quantidadeServicos] = servico;
        quantidadeServicos++;
        return true;
    }

    public void exibirServicos() {
        System.out.println("Serviços:");
        if (quantidadeServicos == 0) {
            System.out.println("  (nenhum serviço adicionado)");
            return;
        }
        for (int i = 0; i < quantidadeServicos; i++) {
            System.out.print("  " + (i + 1) + ". ");
            servicos[i].exibirInformacoes();
        }
    }

    public double calcularValorTotal() {
        double total = 0;
        for (int i = 0; i < quantidadeServicos; i++) {
            total += servicos[i].calcularPrecoFinal(); // polimorfismo
        }
        return total;
    }

    public void exibirResumo() {
        System.out.printf("""
                ------ ORDEM DE SERVIÇO ------
                Cliente: %s
                Veículo ->\s""", nomeCliente);

        veiculo.exibirInformacoes();
        exibirServicos();

        System.out.printf("TOTAL DA ORDEM: R$ %.2f%n", calcularValorTotal());
    }
}