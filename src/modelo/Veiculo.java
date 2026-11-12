package modelo;

import java.time.Year;

import excecoes.ValorInvalidoException;

public class Veiculo {
    private String placa;
    private String modelo;
    private int ano;

    public Veiculo(String placa, String modelo, int ano) throws ValorInvalidoException{
        setPlaca(placa);
        setModelo(modelo);
        setAno(ano);
    }

    public String getPlaca(){
        return placa;
    }

    public void setPlaca(String placa) throws ValorInvalidoException{
        if (placa != null && !placa.trim().isEmpty()){
            this.placa = placa.trim().toUpperCase();
        }
        else{
            throw new ValorInvalidoException("Essa placa é inválida!");
        }
    }

    public String getModelo(){
        return modelo;
    }

    public void setModelo(String modelo) throws ValorInvalidoException{
        if (modelo != null && !modelo.trim().isEmpty()){
            this.modelo = modelo;
        }
        else{
            throw new ValorInvalidoException("Esse modelo é inválido!");
        }
    }

    public int getAno(){
        return ano;
    }

    public void setAno(int ano) throws ValorInvalidoException{
        if (ano > 0 && ano <= Year.now().getValue()){
            this.ano = ano;
        }
        else{
            throw new ValorInvalidoException("Esse ano é inválido!");
        }
    }

    public void exibirInformacoes() {
        System.out.println("-- Placa: " + placa + " -- Modelo: " + modelo + " -- Ano: " + ano);
    }

    public int calcularIdade(int anoAtual) {
        return anoAtual - this.ano;
    }
}