package interfaces;

import excecoes.*;

public interface Descontavel {
    double aplicarDesconto(double percentual) throws ValorInvalidoException, OrdemFinalizadaException;
}
