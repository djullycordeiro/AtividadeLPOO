package gerenciador;

import excecoes.*;
import interfaces.Buscavel;
import modelo.OrdemServico;

public class GerenciadorOrdens implements Buscavel {
    private OrdemServico[] ordens;
    private int quantidadeOrdens;

    public GerenciadorOrdens(int capacidade) throws ValorInvalidoException {
        if (capacidade <= 0) {
            throw new ValorInvalidoException("Capacidade do gerenciador deve ser positiva!");
        }
        this.ordens = new OrdemServico[capacidade];
        this.quantidadeOrdens = 0;
    }

    public int getQuantidadeOrdens() {
        return quantidadeOrdens;
    }

    /** Retorna a ordem na posição informada (0 até getQuantidadeOrdens() - 1). */
    public OrdemServico getOrdem(int indice) {
        if (indice < 0 || indice >= quantidadeOrdens) {
            throw new IndexOutOfBoundsException("Índice de ordem inválido: " + indice);
        }
        return ordens[indice];
    }

    public void adicionarOrdem(OrdemServico ordem) throws ValorInvalidoException, CapacidadeExcedidaException {
        if (ordem == null) {
            throw new ValorInvalidoException("Ordem não pode ser nula!");
        }
        if (buscarPorNumero(ordem.getNumero()) != null) {
            throw new ValorInvalidoException("Já existe uma ordem com o número " + ordem.getNumero() + ".");
        }
        if (quantidadeOrdens >= ordens.length) {
            throw new CapacidadeExcedidaException("Limite de " + ordens.length + " ordens atingido.");
        }
        ordens[quantidadeOrdens] = ordem;
        quantidadeOrdens++;
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

    /** Retorna a ordem com o número informado ou null se não existir. */
    public OrdemServico buscarPorNumero(int numero) {
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getNumero() == numero) {
                return ordens[i];
            }
        }
        return null;
    }

    @Override
    public void buscarPorInicioDoNome(String inicio) throws ValorInvalidoException {
        if (inicio == null || inicio.trim().isEmpty()) {
            throw new ValorInvalidoException("Texto de busca não pode ser vazio!");
        }
        String busca = inicio.trim().toLowerCase();
        boolean encontrou = false;
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getNomeCliente().toLowerCase().startsWith(busca)) {
                ordens[i].exibirResumo();
                System.out.println();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma ordem encontrada para clientes começando com \"" + inicio.trim() + "\".");
        }
    }

    public void buscarPorPlaca(String placa) throws ValorInvalidoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new ValorInvalidoException("Placa de busca não pode ser vazia!");
        }
        boolean encontrou = false;
        for (int i = 0; i < quantidadeOrdens; i++) {
            if (ordens[i].getVeiculo().getPlaca().equalsIgnoreCase(placa.trim())) {
                ordens[i].exibirResumo();
                System.out.println();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma ordem encontrada para a placa " + placa.trim().toUpperCase() + ".");
        }
    }
}
