package modelo;

import excecoes.*;
import interfaces.Descontavel;
import modelo.servico.Servico;

public class OrdemServico implements Descontavel {
    public enum Estado { ABERTA, FINALIZADA }
    //atributos
    private int numero;
    private String nomeCliente;
    private Veiculo veiculo;
    private Servico[] servicos;
    private int quantidadeServicos;
    private double percentualDesconto;
    private Estado estado;

    //construtor
    public OrdemServico(int numero, String nomeCliente, Veiculo veiculo, int capacidadeMaxima)
            throws ValorInvalidoException {
        setNumero(numero);
        setNomeCliente(nomeCliente);
        setVeiculo(veiculo);
        if (capacidadeMaxima <= 0) {
            throw new ValorInvalidoException("Capacidade de serviços deve ser maior que zero!");
        }
        this.servicos = new Servico[capacidadeMaxima];
        this.quantidadeServicos = 0;
        this.percentualDesconto = 0;
        this.estado = Estado.ABERTA;
    }

    public int getNumero() {
        return numero;
    }

    private void setNumero(int numero) throws ValorInvalidoException {
        if (numero <= 0) throw new ValorInvalidoException("O número da ordem deve ser positivo!");
        this.numero = numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) throws ValorInvalidoException {
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new ValorInvalidoException("Nome do cliente não pode ser vazio!");
        }
        this.nomeCliente = nomeCliente.trim();
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) throws ValorInvalidoException {
        if (veiculo == null) throw new ValorInvalidoException("Veículo não pode ser nulo!");
        this.veiculo = veiculo;
    }

    public int getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public Estado getEstado() {
        return estado;
    }

    public void adicionarServico(Servico servico)
            throws ValorInvalidoException, CapacidadeExcedidaException, OrdemFinalizadaException {
        if (servico == null) {
            throw new ValorInvalidoException("Serviço não pode ser nulo!");
        }
        if (estado == Estado.FINALIZADA) {
            throw new OrdemFinalizadaException("A ordem " + numero + " está FINALIZADA e não aceita novos serviços.");
        }
        if (quantidadeServicos >= servicos.length) {
            throw new CapacidadeExcedidaException("Capacidade máxima de serviços (" + servicos.length
                    + ") da ordem " + numero + " atingida.");
        }
        servicos[quantidadeServicos] = servico;
        quantidadeServicos++;
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < quantidadeServicos; i++) {
            subtotal += servicos[i].calcularPrecoFinal(); // polimorfismo
        }
        return subtotal;
    }

    public double calcularTotal() {
        return calcularSubtotal() * (1 - percentualDesconto / 100);
    }

    @Override
    public double aplicarDesconto(double percentual) throws ValorInvalidoException, OrdemFinalizadaException {
        if (estado == Estado.FINALIZADA) {
            throw new OrdemFinalizadaException("A ordem " + numero + " está FINALIZADA; o desconto não pode ser alterado.");
        }
        if (percentual < 0 || percentual > 100) {
            throw new ValorInvalidoException("Desconto deve estar entre 0 e 100%.");
        }
        this.percentualDesconto = percentual;
        return calcularTotal();
    }

    public void finalizar() throws OrdemVaziaException, OrdemFinalizadaException {
        if (estado == Estado.FINALIZADA) {
            throw new OrdemFinalizadaException("A ordem " + numero + " já está FINALIZADA.");
        }
        if (quantidadeServicos == 0) {
            throw new OrdemVaziaException("Não é possível finalizar a ordem " + numero + " sem serviços.");
        }
        estado = Estado.FINALIZADA;
    }

    public void exibirResumo() {
        System.out.println("------ ORDEM DE SERVIÇO Nº " + numero + " ------");
        System.out.println("Cliente: " + nomeCliente);
        System.out.print("Veículo ");
        veiculo.exibirInformacoes();
        System.out.println("Estado: " + estado);
        System.out.println("Serviços:");
        if (quantidadeServicos == 0) {
            System.out.println("  (nenhum serviço adicionado)");
        }
        for (int i = 0; i < quantidadeServicos; i++) {
            System.out.print("  " + (i + 1) + ". ");
            servicos[i].exibirInformacoes();
        }
        System.out.printf("Subtotal: R$ %.2f%n", calcularSubtotal());
        System.out.printf("Desconto: %.2f%%%n", percentualDesconto);
        System.out.printf("TOTAL: R$ %.2f%n", calcularTotal());
    }
}
