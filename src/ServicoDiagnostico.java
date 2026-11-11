package src;

public class ServicoDiagnostico extends Servico {
    private boolean possuiReparo;

    public ServicoDiagnostico(String descricao, double valorBase, boolean possuiReparo){
        super(descricao, valorBase);
        setPossuiReparo(possuiReparo);
    }

    public boolean getPossuiReparo(){
        return possuiReparo;
    }

    public void setPossuiReparo(boolean possuiReparo){
        this.possuiReparo = possuiReparo;
    }

    @Override 
    public double calcularPrecoFinal(){
        if (possuiReparo){
            return getValorBase()*(0.5);
        }
        return getValorBase();
    }

    @Override
    public void exibirInformacoes() {
        System.out.println(getDescricao());
        System.out.printf("     Valor base: R$ %.2f%n", getValorBase());
        System.out.println("     Reparo realizado: " + (possuiReparo ? "Sim" : "Não"));
        System.out.printf("     Preço final: R$ %.2f%n", calcularPrecoFinal());
    }
}