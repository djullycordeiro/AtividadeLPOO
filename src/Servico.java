package src;

public abstract class Servico {
    //n é permitido instanciar diretamente a classe Servico, pois ela é abstrata. Ela serve como base para as subclasses.
    private String descricao;
    private double valorBase;

    public Servico(String descricao, double valorBase) {
        setDescricao(descricao);
        setValorBase(valorBase);
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        if (descricao != null && !descricao.trim().isEmpty()){
            this.descricao = descricao;
        }
        else {
            System.out.println("Digite uma descrição válida");
        }
    }

    public double getValorBase(){
        return valorBase;
    }

    public void setValorBase(double valorBase){
        if (valorBase > 0){
            this.valorBase = valorBase;
        }
        else{
            System.out.println("Digite um valor válido");
        }
    }


    public abstract double calcularPrecoFinal();

    public abstract void exibirInformacoes();
}