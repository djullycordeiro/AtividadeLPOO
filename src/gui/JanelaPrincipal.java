package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import excecoes.CapacidadeExcedidaException;
import excecoes.OrdemFinalizadaException;
import excecoes.OrdemVaziaException;
import excecoes.ValorInvalidoException;
import gerenciador.GerenciadorOrdens;
import modelo.OrdemServico;
import modelo.Veiculo;
import modelo.servico.Servico;
import modelo.servico.ServicoComPecas;
import modelo.servico.ServicoDiagnostico;
import modelo.servico.ServicoPorHora;

/**
 * Interface gráfica (Swing) da Oficina Box 42.
 * Apenas coleta dados e exibe resultados: todos os cálculos e validações
 * são feitos pelas classes de domínio.
 */
public class JanelaPrincipal extends JFrame {
    private static final int CAPACIDADE_ORDENS = 20;

    private final GerenciadorOrdens gerenciador;
    private final DefaultTableModel modeloTabela;
    private final JTable tabela;
    private final JTextArea areaResumo;

    public JanelaPrincipal(GerenciadorOrdens gerenciador) {
        super("Oficina Box 42 — Ordens de Serviço");
        this.gerenciador = gerenciador;

        String[] colunas = {"Nº", "Cliente", "Placa", "Estado", "Serviços", "Subtotal (R$)", "Desconto (%)", "Total (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarResumoSelecionada();
        });

        areaResumo = new JTextArea();
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JSplitPane divisao = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabela), new JScrollPane(areaResumo));
        divisao.setResizeWeight(0.45);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 6, 6));
        botoes.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        botoes.add(botao("Nova ordem", this::criarOrdem));
        botoes.add(botao("Adicionar serviço", this::adicionarServico));
        botoes.add(botao("Aplicar desconto", this::aplicarDesconto));
        botoes.add(botao("Finalizar ordem", this::finalizarOrdem));
        botoes.add(botao("Buscar por cliente", this::buscarPorCliente));
        botoes.add(botao("Buscar por placa", this::buscarPorPlaca));
        botoes.add(botao("Listar todas", this::listarTodas));

        JPanel lateral = new JPanel(new BorderLayout());
        lateral.add(botoes, BorderLayout.NORTH);

        setLayout(new BorderLayout());
        add(divisao, BorderLayout.CENTER);
        add(lateral, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setLocationRelativeTo(null);
    }

    // ---------------- ações ----------------

    private void criarOrdem() {
        JTextField numero = new JTextField();
        JTextField cliente = new JTextField();
        JTextField placa = new JTextField();
        JTextField modelo = new JTextField();
        JTextField ano = new JTextField();
        JTextField capacidade = new JTextField("5");

        if (!formulario("Nova ordem de serviço",
                "Número:", numero, "Cliente:", cliente, "Placa:", placa,
                "Modelo:", modelo, "Ano:", ano, "Capacidade de serviços:", capacidade)) {
            return;
        }
        try {
            Veiculo veiculo = new Veiculo(placa.getText(), modelo.getText(), lerInt(ano, "Ano"));
            OrdemServico ordem = new OrdemServico(lerInt(numero, "Número"), cliente.getText(),
                    veiculo, lerInt(capacidade, "Capacidade"));
            gerenciador.adicionarOrdem(ordem);
            atualizarTabela();
            selecionar(ordem);
            sucesso("Ordem nº " + ordem.getNumero() + " criada.");
        } catch (ValorInvalidoException | CapacidadeExcedidaException e) {
            erro(e);
        }
    }

    private void adicionarServico() {
        OrdemServico ordem = ordemSelecionada();
        if (ordem == null) return;

        String[] tipos = {"Com peças", "Por hora", "Diagnóstico"};
        int tipo = JOptionPane.showOptionDialog(this, "Tipo de serviço:", "Adicionar serviço",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo < 0) return;

        JTextField descricao = new JTextField();
        JTextField valorBase = new JTextField();
        JTextField campo1 = new JTextField();
        JTextField campo2 = new JTextField();
        JCheckBox reparo = new JCheckBox("Possui reparo (50% de desconto no valor base)");

        boolean ok = switch (tipo) {
            case 0 -> formulario("Serviço com peças", "Descrição:", descricao,
                    "Valor base (R$):", valorBase, "Valor das peças (R$):", campo1);
            case 1 -> formulario("Serviço por hora", "Descrição:", descricao,
                    "Valor base (R$):", valorBase, "Horas trabalhadas:", campo1, "Valor da hora (R$):", campo2);
            default -> formulario("Diagnóstico", "Descrição:", descricao,
                    "Valor base (R$):", valorBase, "", reparo);
        };
        if (!ok) return;

        try {
            double base = lerDouble(valorBase, "Valor base");
            Servico servico = switch (tipo) {
                case 0 -> new ServicoComPecas(descricao.getText(), base, lerDouble(campo1, "Valor das peças"));
                case 1 -> new ServicoPorHora(descricao.getText(), base,
                        lerDouble(campo1, "Horas trabalhadas"), lerDouble(campo2, "Valor da hora"));
                default -> new ServicoDiagnostico(descricao.getText(), base, reparo.isSelected());
            };
            ordem.adicionarServico(servico);
            atualizarTabela();
            selecionar(ordem);
            sucesso(String.format("Serviço adicionado. Preço calculado: R$ %.2f", servico.calcularPrecoFinal()));
        } catch (ValorInvalidoException | CapacidadeExcedidaException | OrdemFinalizadaException e) {
            erro(e);
        }
    }

    private void aplicarDesconto() {
        OrdemServico ordem = ordemSelecionada();
        if (ordem == null) return;
        String texto = JOptionPane.showInputDialog(this, "Percentual de desconto (0 a 100):",
                ordem.getPercentualDesconto());
        if (texto == null) return;
        try {
            double novoTotal = ordem.aplicarDesconto(converterDouble(texto, "Desconto"));
            atualizarTabela();
            selecionar(ordem);
            sucesso(String.format("Desconto aplicado. Novo total: R$ %.2f", novoTotal));
        } catch (ValorInvalidoException | OrdemFinalizadaException e) {
            erro(e);
        }
    }

    private void finalizarOrdem() {
        OrdemServico ordem = ordemSelecionada();
        if (ordem == null) return;
        try {
            ordem.finalizar();
            atualizarTabela();
            selecionar(ordem);
            sucesso("Ordem nº " + ordem.getNumero() + " FINALIZADA.");
        } catch (OrdemVaziaException | OrdemFinalizadaException e) {
            erro(e);
        }
    }

    private void buscarPorCliente() {
        String texto = JOptionPane.showInputDialog(this, "Início do nome do cliente:");
        if (texto == null) return;
        try {
            areaResumo.setText(capturarSaida(() -> gerenciador.buscarPorInicioDoNome(texto)));
            tabela.clearSelection();
        } catch (ValorInvalidoException e) {
            erro(e);
        }
    }

    private void buscarPorPlaca() {
        String texto = JOptionPane.showInputDialog(this, "Placa do veículo:");
        if (texto == null) return;
        try {
            areaResumo.setText(capturarSaida(() -> gerenciador.buscarPorPlaca(texto)));
            tabela.clearSelection();
        } catch (ValorInvalidoException e) {
            erro(e);
        }
    }

    private void listarTodas() {
        try {
            areaResumo.setText(capturarSaida(gerenciador::listarOrdens));
            tabela.clearSelection();
        } catch (ValorInvalidoException e) {
            erro(e); // listarOrdens não lança, mas a assinatura genérica exige o catch
        }
    }

    // ---------------- tabela e resumo ----------------

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (int i = 0; i < gerenciador.getQuantidadeOrdens(); i++) {
            OrdemServico o = gerenciador.getOrdem(i);
            modeloTabela.addRow(new Object[]{
                    o.getNumero(), o.getNomeCliente(), o.getVeiculo().getPlaca(), o.getEstado(),
                    o.getQuantidadeServicos(),
                    String.format("%.2f", o.calcularSubtotal()),
                    String.format("%.2f", o.getPercentualDesconto()),
                    String.format("%.2f", o.calcularTotal())
            });
        }
    }

    private void selecionar(OrdemServico ordem) {
        for (int i = 0; i < gerenciador.getQuantidadeOrdens(); i++) {
            if (gerenciador.getOrdem(i) == ordem) {
                tabela.setRowSelectionInterval(i, i);
                mostrarResumoSelecionada();
                return;
            }
        }
    }

    private void mostrarResumoSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        OrdemServico ordem = gerenciador.getOrdem(linha);
        try {
            areaResumo.setText(capturarSaida(ordem::exibirResumo));
            areaResumo.setCaretPosition(0);
        } catch (ValorInvalidoException e) {
            erro(e);
        }
    }

    private OrdemServico ordemSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma ordem na tabela primeiro.",
                    "Nenhuma ordem selecionada", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return gerenciador.getOrdem(linha);
    }

    // ---------------- utilitários ----------------

    /** Ação que imprime no console e pode lançar ValorInvalidoException. */
    private interface AcaoComSaida {
        void executar() throws ValorInvalidoException;
    }

    /**
     * Reaproveita os métodos de exibição do domínio (que usam System.out),
     * redirecionando temporariamente a saída para um texto mostrado na janela.
     */
    private static String capturarSaida(AcaoComSaida acao) throws ValorInvalidoException {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            acao.executar();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /** Monta um formulário com pares rótulo/campo. Retorna true se o usuário clicou em OK. */
    private boolean formulario(String titulo, Object... rotulosECampos) {
        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));
        for (int i = 0; i < rotulosECampos.length; i += 2) {
            painel.add(new JLabel((String) rotulosECampos[i]));
            painel.add((JComponent) rotulosECampos[i + 1]);
        }
        return JOptionPane.showConfirmDialog(this, painel, titulo, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    private static int lerInt(JTextField campo, String nome) throws ValorInvalidoException {
        try {
            return Integer.parseInt(campo.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException(nome + " deve ser um número inteiro.");
        }
    }

    private static double lerDouble(JTextField campo, String nome) throws ValorInvalidoException {
        return converterDouble(campo.getText(), nome);
    }

    private static double converterDouble(String texto, String nome) throws ValorInvalidoException {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException(nome + " deve ser um número (ex: 150.50).");
        }
    }

    private JButton botao(String texto, Runnable acao) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> acao.run());
        return b;
    }

    private void sucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void erro(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new JanelaPrincipal(new GerenciadorOrdens(CAPACIDADE_ORDENS)).setVisible(true);
            } catch (ValorInvalidoException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
