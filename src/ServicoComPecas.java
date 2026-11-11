package src;

public class ServicoComPecas extends Servico {
    private double valorPecas;

    public ServicoComPecas (String descricao, double valorBase, double valorPecas){
        super(descricao, valorBase);
        setValorPecas(valorPecas);

    }
public double getValorPecas(){
    return valorPecas;
}

public void setValorPecas(double valorPecas){
    if (valorPecas > 0){
        this.valorPecas = valorPecas;
    }
    else{
        System.out.println("Valor de peça inválido! Digite novamente.");
    }
}

@Override 
public double calcularPrecoFinal (){
    return getValorBase() + valorPecas;
}

@Override
public void exibirInformacoes() {
    System.out.println(getDescricao());
    System.out.printf("     Valor base: R$ %.2f%n", getValorBase());
    System.out.printf("     Peças: R$ %.2f%n", valorPecas);
    System.out.printf("     Preço final: R$ %.2f%n", calcularPrecoFinal());
}

}
