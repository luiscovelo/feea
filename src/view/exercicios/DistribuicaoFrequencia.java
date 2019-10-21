/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.exercicios;

import aritmetica.CalcularDistribuicao;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import view.exercicios.grafico.Grafico;

/**
 *
 * @author COVELO
 */
public class DistribuicaoFrequencia extends javax.swing.JFrame {

    /** Creates new form DistribuicaoFrequencia */
    public DistribuicaoFrequencia() {
        initComponents();
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/view/dashboard/imagens/logo-feea.png"));  
        setIconImage(icon.getImage());
    }
    
    private String[] intervalos;
    private String[] frequencia;
    private Object[] dadosDist;
    
    private double[] intervalo1 = new double[1000];
    private double[] intervalo2 = new double[1000];
    private double[] qtdeFrequencia = new double[1000];
    
    private String[] graficoIntervalo = new String[1000];
    private double[] graficoFiPercent = new double[1000];
    
    private String result_fi_percent = "";
    private String result_Fi = "";
    private String result_Fi_percent = "";
    private String result_xqmed = "";
    
    public void EntradaDeDados(String[] intervalos, String[] frequencia){
        
        this.intervalos = intervalos;
        this.frequencia = frequencia;
        MontarTabela();
        
    }
    
    public void MontarTabela(){
        
        CalcularDistribuicao calcDist = new CalcularDistribuicao();
        
        calcDist.Calcular(this.intervalos, this.frequencia);

        this.intervalo1 = calcDist.getIntervalo1();
        this.intervalo2 = calcDist.getIntervalo2();
        this.qtdeFrequencia = calcDist.getNumIntervalos();
        
        String colunas[] = {
            "Intervalo", 
            "Media do Intervalo",
            "fi",
            "fi(%)",
            "Fi",
            "Fi(%)",
            "Xmed*Q"
        };
        
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        
        String calcIntervalo;
        double mediaIntervalo;
        double totalItens = 0;
        double fi_perc = 0;
        double total_fi_perc = 0;
        double total_media_intervalos = 0;
        double[] Fi = new double[1000];
        double totalFi = 0;
        double Fi_perc = 0;
        double Xmedq = 0;
        
        for(int i = 0; i< this.qtdeFrequencia.length; i++){
            totalItens += this.qtdeFrequencia[i];
        }
        
        for(int i = 0; i< this.qtdeFrequencia.length; i++){
            
            calcIntervalo = this.intervalo1[i] + " - " + this.intervalo2[i];
            mediaIntervalo = (this.intervalo1[i] + this.intervalo2[i])/2;
            fi_perc = (this.qtdeFrequencia[i]*100)/totalItens;
            total_fi_perc += fi_perc;
            totalFi += this.qtdeFrequencia[i];
            Fi[i] = totalFi;
            
            total_media_intervalos += mediaIntervalo * this.qtdeFrequencia[i];
            
            Fi_perc = (Fi[i]*100)/totalItens;
            
            Xmedq = mediaIntervalo*this.qtdeFrequencia[i];
            
            modelo.addRow(new String[]{
                calcIntervalo,
                String.format("%.2f", mediaIntervalo),
                String.format("%.0f", this.qtdeFrequencia[i]),
                String.format("%.2f", fi_perc),
                String.format("%.2f", Fi[i]),
                String.format("%.2f", Fi_perc),
                String.format("%.2f", Xmedq)
            });
            
            this.graficoIntervalo[i] = calcIntervalo;
            this.graficoFiPercent[i] = Fi_perc;
            
            this.result_fi_percent += (i+1) + ". ("+this.qtdeFrequencia[i] +" * 100) / " + totalItens;
            this.result_fi_percent += " = " + String.format("%.2f", fi_perc) + "%\n\n";
            
            this.result_Fi_percent += (i+1) + ". ("+Fi[i] +" * 100) / " + totalItens;
            this.result_Fi_percent += " = " + String.format("%.2f", Fi_perc) + "%\n\n";
            
            this.result_xqmed += (i+1) + ". ("+mediaIntervalo+" * "+this.qtdeFrequencia[i]+")";
            this.result_xqmed += " = " + String.format("%.2f", Xmedq) + "\n\n";
            
            if(i==0){
                
                this.result_Fi += (i+1) + ". (" + this.qtdeFrequencia[i] + " + 0) = " + String.format("%.2f", Fi[i]) + "\n\n";
                
            }else if(i < this.qtdeFrequencia.length){
                
                this.result_Fi += (i+1) + ". (" + this.qtdeFrequencia[i] + " + "+ Fi[i-1] +") = " + String.format("%.2f", Fi[i]) + "\n\n";
                
            }
                        
        }
        
        this.txt_fi_percent.append(this.result_fi_percent);
        this.txt_Fi.append(this.result_Fi);
        this.txt_Fi_percent.append(this.result_Fi_percent);
        this.txt_xmed.append(this.result_xqmed);
        
        modelo.addRow(new String[]{
            "Média",
            String.format("%.2f", total_media_intervalos/totalItens),
            String.format("%.0f", totalItens),
            String.format("%.2f", total_fi_perc),
            "",
            "",
            ""
        });
        
        this.tabelaFrequencia.setModel(modelo);
        //this.criarGrafico();
    }
    
    //criar o dataset
    public CategoryDataset createDataSet(String[] intervalos, double[] Fi_percent){
                
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        
        for(int i = 0; i<intervalos.length; i++){
            
            if(intervalos[i]!=null && Fi_percent[i] != 0.0){
                dataSet.addValue(
                    Fi_percent[i], 
                    "", 
                    intervalos[i]
                );
            }
 
        }
                
        return dataSet;
        
    }
    
    //criar o grafico barras
    public JFreeChart createLineChart(CategoryDataset dataset){
    
        JFreeChart graficoBarras = ChartFactory.createLineChart(
            "Gráfico de Ogiva", 
            "Intervalo", "F'i(%)", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        return graficoBarras;
        
    }
    
    //criar o grafico completo
    public ChartPanel criarGrafico(){
        
        CategoryDataset dataSet = this.createDataSet(this.graficoIntervalo,this.graficoFiPercent);
        JFreeChart grafico = this.createLineChart(dataSet);
        ChartPanel painelDoGrafico = new ChartPanel(grafico);
        
        painelDoGrafico.setPreferredSize(new Dimension(800,800));
        
        return painelDoGrafico;
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaFrequencia1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        pnSide = new javax.swing.JPanel();
        pnResolucao = new javax.swing.JPanel();
        spResolucao = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtPrimeiroPasso = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSegundoPasso = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtSegundoPasso1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtSegundoPasso2 = new javax.swing.JTextArea();
        pnBase = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaFrequencia = new javax.swing.JTable();
        jp_xmed = new javax.swing.JScrollPane();
        txt_xmed = new javax.swing.JTextArea();
        jp_fi_percent = new javax.swing.JScrollPane();
        txt_fi_percent = new javax.swing.JTextArea();
        jp_Fi = new javax.swing.JScrollPane();
        txt_Fi = new javax.swing.JTextArea();
        jp_Fi_percent = new javax.swing.JScrollPane();
        txt_Fi_percent = new javax.swing.JTextArea();
        lb_fi = new javax.swing.JLabel();
        lb_Fi = new javax.swing.JLabel();
        lb_Fi_perc = new javax.swing.JLabel();
        lb_xmed = new javax.swing.JLabel();
        btnGrafico = new javax.swing.JButton();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabelaFrequencia1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Frequência", "Quantidade", "X", "fi%", "FI", "FI%", "Xmed*Q"
            }
        ));
        jScrollPane2.setViewportView(tabelaFrequencia1);

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Resolução do Exercício");

        pnSide.setBackground(new java.awt.Color(255, 255, 255));
        pnSide.setMaximumSize(new java.awt.Dimension(400, 600));
        pnSide.setMinimumSize(new java.awt.Dimension(400, 600));

        javax.swing.GroupLayout pnSideLayout = new javax.swing.GroupLayout(pnSide);
        pnSide.setLayout(pnSideLayout);
        pnSideLayout.setHorizontalGroup(
            pnSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        pnSideLayout.setVerticalGroup(
            pnSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        spResolucao.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel1.setText("1º Passo");

        txtPrimeiroPasso.setEditable(false);
        txtPrimeiroPasso.setColumns(20);
        txtPrimeiroPasso.setRows(5);
        jScrollPane3.setViewportView(txtPrimeiroPasso);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel2.setText("2º Passo");

        txtSegundoPasso.setEditable(false);
        txtSegundoPasso.setColumns(20);
        txtSegundoPasso.setRows(5);
        jScrollPane4.setViewportView(txtSegundoPasso);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel3.setText("3º Passo");

        txtSegundoPasso1.setEditable(false);
        txtSegundoPasso1.setColumns(20);
        txtSegundoPasso1.setRows(5);
        jScrollPane5.setViewportView(txtSegundoPasso1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel4.setText("4º Passo");

        txtSegundoPasso2.setEditable(false);
        txtSegundoPasso2.setColumns(20);
        txtSegundoPasso2.setRows(5);
        jScrollPane6.setViewportView(txtSegundoPasso2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        spResolucao.setViewportView(jPanel1);

        javax.swing.GroupLayout pnResolucaoLayout = new javax.swing.GroupLayout(pnResolucao);
        pnResolucao.setLayout(pnResolucaoLayout);
        pnResolucaoLayout.setHorizontalGroup(
            pnResolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnResolucaoLayout.createSequentialGroup()
                .addComponent(spResolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnResolucaoLayout.setVerticalGroup(
            pnResolucaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spResolucao)
        );

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(610, Short.MAX_VALUE))
            .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jFrame1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(pnResolucao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(57, 57, 57)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(290, Short.MAX_VALUE))
            .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jFrame1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pnResolucao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnSide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Distribuição de Frequência");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(1034, 600));
        setMinimumSize(new java.awt.Dimension(1034, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1034, 600));

        pnBase.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        tabelaFrequencia.setFont(new java.awt.Font("Berlin Sans FB", 0, 16)); // NOI18N
        tabelaFrequencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabelaFrequencia.setEnabled(false);
        tabelaFrequencia.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tabelaFrequencia);

        txt_xmed.setEditable(false);
        txt_xmed.setColumns(20);
        txt_xmed.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        txt_xmed.setRows(5);
        jp_xmed.setViewportView(txt_xmed);

        txt_fi_percent.setEditable(false);
        txt_fi_percent.setColumns(20);
        txt_fi_percent.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        txt_fi_percent.setRows(5);
        txt_fi_percent.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt_fi_percent.setEnabled(false);
        jp_fi_percent.setViewportView(txt_fi_percent);

        txt_Fi.setEditable(false);
        txt_Fi.setColumns(20);
        txt_Fi.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        txt_Fi.setRows(5);
        txt_Fi.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jp_Fi.setViewportView(txt_Fi);

        txt_Fi_percent.setEditable(false);
        txt_Fi_percent.setColumns(20);
        txt_Fi_percent.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        txt_Fi_percent.setRows(5);
        jp_Fi_percent.setViewportView(txt_Fi_percent);

        lb_fi.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        lb_fi.setText("fi (%)");
        lb_fi.setToolTipText("Frequência Relativa");

        lb_Fi.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        lb_Fi.setText("Fi");
        lb_Fi.setToolTipText("Frequência Absoluta Acumulada");

        lb_Fi_perc.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        lb_Fi_perc.setText("Fi (%)");
        lb_Fi_perc.setToolTipText("Frequência Relativa Acumulada");

        lb_xmed.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        lb_xmed.setText("Xmed*Q");
        lb_xmed.setToolTipText("Resultado da média do intervalo multiplicado pela Frequência Absoluta.");

        btnGrafico.setBackground(new java.awt.Color(93, 188, 210));
        btnGrafico.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnGrafico.setForeground(new java.awt.Color(255, 255, 255));
        btnGrafico.setText("Visualizar o gráfico de Ogiva");
        btnGrafico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGraficoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnBaseLayout = new javax.swing.GroupLayout(pnBase);
        pnBase.setLayout(pnBaseLayout);
        pnBaseLayout.setHorizontalGroup(
            pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBaseLayout.createSequentialGroup()
                .addGroup(pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnBaseLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnBaseLayout.createSequentialGroup()
                                .addComponent(jp_fi_percent, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jp_Fi, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jp_Fi_percent, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jp_xmed, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnBaseLayout.createSequentialGroup()
                                .addComponent(lb_fi)
                                .addGap(211, 211, 211)
                                .addComponent(lb_Fi)
                                .addGap(234, 234, 234)
                                .addComponent(lb_Fi_perc)
                                .addGap(209, 209, 209)
                                .addComponent(lb_xmed))
                            .addComponent(btnGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnBaseLayout.setVerticalGroup(
            pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBaseLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_fi)
                    .addComponent(lb_Fi)
                    .addComponent(lb_Fi_perc)
                    .addComponent(lb_xmed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jp_xmed, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jp_Fi_percent, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jp_Fi, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jp_fi_percent, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(btnGrafico)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 393, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGraficoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGraficoMouseClicked
        
        Grafico viewGrafico = new Grafico();
        
        viewGrafico.montarGrafico( criarGrafico() );
        
        viewGrafico.setLocationRelativeTo(null);
        viewGrafico.setVisible(true);
        
    }//GEN-LAST:event_btnGraficoMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DistribuicaoFrequencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DistribuicaoFrequencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DistribuicaoFrequencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DistribuicaoFrequencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DistribuicaoFrequencia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGrafico;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jp_Fi;
    private javax.swing.JScrollPane jp_Fi_percent;
    private javax.swing.JScrollPane jp_fi_percent;
    private javax.swing.JScrollPane jp_xmed;
    private javax.swing.JLabel lb_Fi;
    private javax.swing.JLabel lb_Fi_perc;
    private javax.swing.JLabel lb_fi;
    private javax.swing.JLabel lb_xmed;
    private javax.swing.JPanel pnBase;
    private javax.swing.JPanel pnResolucao;
    private javax.swing.JPanel pnSide;
    private javax.swing.JScrollPane spResolucao;
    private javax.swing.JTable tabelaFrequencia;
    private javax.swing.JTable tabelaFrequencia1;
    private javax.swing.JTextArea txtPrimeiroPasso;
    private javax.swing.JTextArea txtSegundoPasso;
    private javax.swing.JTextArea txtSegundoPasso1;
    private javax.swing.JTextArea txtSegundoPasso2;
    private javax.swing.JTextArea txt_Fi;
    private javax.swing.JTextArea txt_Fi_percent;
    private javax.swing.JTextArea txt_fi_percent;
    private javax.swing.JTextArea txt_xmed;
    // End of variables declaration//GEN-END:variables

}
