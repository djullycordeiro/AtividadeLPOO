package src;

public class ServicoPorHora extends Servico {
    private double horasTrabalhadas;
    private double valorHora;

    public ServicoPorHora(String descricao, double valorBase, double horasTrabalhadas, double valorHora){
        super(descricao, valorBase);
        setHorasTrabalhadas(horasTrabalhadas);
        setValorHora(valorHora);

    }
public double getHorasTrabalhadas(){
    return horasTrabalhadas;
    }
public void setHorasTrabalhadas(double horasTrabalhadas){
    if (horasTrabalhadas >= 0){
        this.horasTrabalhadas = horasTrabalhadas;
    }
    else{
        System.out.println("Número de horas inválido! Digite novamente");
    }
}
public double getValorHora(){
    return valorHora;
}

public void setValorHora(double valorHora){
    if (valorHora > 0){
        this.valorHora = valorHora;
    }
    else{
        System.out.println("Valor por hora inválido! Digite novamente.");
    }
}

@Override
public double calcularPrecoFinal(){
    return getValorBase() + (horasTrabalhadas * valorHora);
}

@Override
public void exibirInformacoes() {
    System.out.println(getDescricao());
    System.out.printf("     Valor base: R$ %.2f%n", getValorBase());
    System.out.println("     Horas trabalhadas: " + horasTrabalhadas);
    System.out.printf("     Valor/hora: R$ %.2f%n", valorHora);
    System.out.printf("     Preço final: R$ %.2f%n", calcularPrecoFinal());
}
}