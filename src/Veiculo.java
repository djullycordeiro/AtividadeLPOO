package src;

public class Veiculo {
    private String placa;
    private String modelo;
    private int ano;

    public Veiculo(String placa, String modelo, int ano) {
        setPlaca(placa);
        setModelo(modelo);
        setAno(ano);
    }

    public String getPlaca(){
        return placa;
    }

    public void setPlaca(String placa){
        if (placa != null && !placa.trim().isEmpty()){
            this.placa = placa.trim().toUpperCase();
        }
        else{
            System.out.println("Placa inválida! Digite novamente");
        }
    }

    public String getModelo(){
        return modelo;
    }

    public void setModelo(String modelo){
        if (modelo != null && !modelo.trim().isEmpty()){
            this.modelo = modelo;
        }
        else{
            System.out.println("Modelo inválido! Digite novamente.");
        }
    }

    public int getAno(){
        return ano;
    }

    public void setAno(int ano){
        if (ano > 0 && ano <= 2026){
            this.ano = ano;
        }
        else{
            System.out.println("Ano inválido! Digite novamente.");
        }
    }

    public void exibirInformacoes() {
        System.out.println("-- Placa: " + placa + " -- Modelo: " + modelo + " -- Ano: " + ano);
    }

    public int calcularIdade(int anoAtual) {
        return anoAtual - this.ano;
    }
}