package modelo.servico;

import excecoes.ValorInvalidoException;

public class ServicoPorHora extends Servico {
    private double horasTrabalhadas;
    private double valorHora;

    public ServicoPorHora(String descricao, double valorBase, double horasTrabalhadas, double valorHora) throws ValorInvalidoException{
        super(descricao, valorBase);
        setHorasTrabalhadas(horasTrabalhadas);
        setValorHora(valorHora);

    }
public double getHorasTrabalhadas(){
    return horasTrabalhadas;
    }
public void setHorasTrabalhadas(double horasTrabalhadas) throws ValorInvalidoException{
    if (horasTrabalhadas > 0){
        this.horasTrabalhadas = horasTrabalhadas;
    }
    else{
        throw new ValorInvalidoException("Número de horas inválido! Digite novamente");
    }
}
public double getValorHora(){
    return valorHora;
}

public void setValorHora(double valorHora) throws ValorInvalidoException{
    if (valorHora > 0){
        this.valorHora = valorHora;
    }
    else{
        throw new ValorInvalidoException("Valor por hora inválido! Digite novamente.");
    }
}

@Override
public double calcularPrecoFinal(){
    return getValorBase() + (horasTrabalhadas * valorHora);
}

@Override
public void exibirInformacoes() {
    System.out.println(getDescricao());
    System.out.println("     Tipo: por hora");
    System.out.printf("     Valor base: R$ %.2f%n", getValorBase());
    System.out.println("     Horas trabalhadas: " + horasTrabalhadas);
    System.out.printf("     Valor/hora: R$ %.2f%n", valorHora);
    System.out.printf("     Preço final: R$ %.2f%n", calcularPrecoFinal());
}
}