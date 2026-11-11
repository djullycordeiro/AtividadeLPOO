package src;

public class GerenciadorOrdens {
    private static final int MAX_ORDENS = 100;

    private OrdemServico[] ordens;
    private int quantidadeOrdens;

    public GerenciadorOrdens() {
        this.ordens = new OrdemServico[MAX_ORDENS];
        this.quantidadeOrdens = 0;
    }

    public void adicionarOrdem(OrdemServico ordem) {
        if (quantidadeOrdens >= ordens.length) {
            System.out.println("Limite de ordens cadastradas atingido.");
            return;
        }
        ordens[quantidadeOrdens] = ordem;
        quantidadeOrdens++;
        System.out.println("\nOrdem cadastrada com sucesso.\n");
    }

    public void listarOrdens() {
        if (quantidadeOrdens == 0) {
            System.out.println("Nenhuma ordem cadastrada ainda.");
            return;
        }
        for (int i = 0; i < quantidadeOrdens; i++) {
            ordens[i].exibirResumo();
            System.out.println();
        }
    }

    public void buscarOrdensPorInicioDoNome(String inicio) {
        boolean encontrou = false;
        String buscaMin = inicio.trim().toLowerCase();
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getNomeCliente().toLowerCase().startsWith(buscaMin)) {
                ordens[i].exibirResumo();
                System.out.println();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma ordem encontrada para esse nome.");
        }
    }

    public OrdemServico obterOrdemPorPlaca(String placa) {
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getVeiculo().getPlaca().equalsIgnoreCase(placa.trim())) {
                return ordens[i];
            }
        }
        return null;
    }

    public void buscarOrdemPorPlaca(String placa) {
        OrdemServico ordem = obterOrdemPorPlaca(placa);
        if (ordem == null) {
            System.out.println("Nenhuma ordem encontrada para essa placa.");
            return;
        }
        ordem.exibirResumo();
    }
}