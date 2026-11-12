package modelo.servico;

import excecoes.ValorInvalidoException;

public abstract class Servico {
    //n é permitido instanciar diretamente a classe Servico, pois ela é abstrata. Ela serve como base para as subclasses.
    private String descricao;
    private double valorBase;

    public Servico(String descricao, double valorBase) throws ValorInvalidoException {
        setDescricao(descricao);
        setValorBase(valorBase);
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao) throws ValorInvalidoException{
        if (descricao != null && !descricao.trim().isEmpty()){
            this.descricao = descricao;
        }
        else {
            throw new ValorInvalidoException("Digite uma descrição válida");
        }
    }

    public double getValorBase(){
        return valorBase;
    }

    public void setValorBase(double valorBase) throws ValorInvalidoException{
        if (valorBase > 0){
            this.valorBase = valorBase;
        }
        else{
            throw new ValorInvalidoException ("Digite um valor válido");
        }
    }


    public abstract double calcularPrecoFinal();

    public abstract void exibirInformacoes();
}