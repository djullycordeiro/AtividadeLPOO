package modelo.servico;

import excecoes.ValorInvalidoException;

public class ServicoComPecas extends Servico {
    private double valorPecas;

    public ServicoComPecas (String descricao, double valorBase, double valorPecas) throws ValorInvalidoException{
        super(descricao, valorBase);
        setValorPecas(valorPecas);

    }
public double getValorPecas(){
    return valorPecas;
}

public void setValorPecas(double valorPecas) throws ValorInvalidoException{
    if (valorPecas >= 0){
        this.valorPecas = valorPecas;
    }
    else{
        throw new ValorInvalidoException("Valor de peça inválido! Digite novamente.");
    }
}

@Override 
public double calcularPrecoFinal (){
    return getValorBase() + valorPecas;
}

@Override
public void exibirInformacoes() {
    System.out.println(getDescricao());
    System.out.println("     Tipo: Com peças");
    System.out.printf("     Valor base: R$ %.2f%n", getValorBase());
    System.out.printf("     Peças: R$ %.2f%n", valorPecas);
    System.out.printf("     Preço final: R$ %.2f%n", calcularPrecoFinal());
}

}
