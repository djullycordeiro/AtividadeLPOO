package interfaces;

import excecoes.*;

public interface Buscavel {
    void buscarPorInicioDoNome(String inicio) throws ValorInvalidoException;
}
