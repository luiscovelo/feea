/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aritmetica;

/**
 *
 * @author COVELO
 */
public class CalcularMediana {
    
    private double newValores;
    private String[] valores;
    private int qtdeValores = 0;
    private double resultMediana = 0.0;
    private String primeiro_passo = "";
    private String segundo_passo = "";
    
    public void EntradaDeDados(String[] valores){
        
        this.valores = valores;
        this.qtdeValores = valores.length;
                
    }
    
    public double getMediana(){
        return this.resultMediana;
    }
    
    public String PrimeiroPasso(){
        
        this.primeiro_passo += "Primeiramente é necessário organizar \r";
        this.primeiro_passo += "de forma crescente.\r\n\n";
        
        double[] vetorValores = new double[this.qtdeValores];
        
        int i = 0;
        for (String valor : this.valores) {
            vetorValores[i] = Double.parseDouble(valor);
            i++;
        }
        
        this.primeiro_passo += "Antigo: \t";
        
        for (int j = 0; j < this.qtdeValores; j++) {
            this.primeiro_passo += ""
                    + vetorValores[j] + "\t";
        }
        
        double aux;
        int chave;
        do{
            chave=0;
        for (int j = 0; j < this.qtdeValores-1; j++) {
                     
            if(vetorValores[j] > vetorValores[j+1]){
                aux = vetorValores[j];
                vetorValores[j] = vetorValores[j+1];
                vetorValores[j+1] = aux;
                chave=1;
            }
            
        }
        }while(chave==1);
        
        
        this.primeiro_passo += "\nNovo: \t";
        
        for (int j = 0; j < this.qtdeValores; j++) {
            this.primeiro_passo += ""
                    + vetorValores[j] + "\t";
        }
        
        return this.primeiro_passo;
    }
    
    public String SegundoPasso(){
                
        double restoDivisao  = 0;
        double resultDivisao = 0;
        double posInf  = 0;
        double posSup  = 0;
        double divisor = 2.0;
        double mediana = 0.0;
        
        restoDivisao  = this.valores.length % 2;
        resultDivisao = this.valores.length / 2;
        
        double[] vetorValores = new double[this.qtdeValores];
        
        int i = 0;
        for (String valor : this.valores) {
            vetorValores[i] = Double.parseDouble(valor);
            i++;
        }
        
        double aux;
        int chave;
        do{
            chave=0;
            for (int j = 0; j < this.qtdeValores-1; j++) {

                if(vetorValores[j] > vetorValores[j+1]){
                    aux = vetorValores[j];
                    vetorValores[j] = vetorValores[j+1];
                    vetorValores[j+1] = aux;
                    chave=1;
                }

            }
        }while(chave==1);
        
        if(restoDivisao == 0){
            
            posInf = resultDivisao-1;
            posSup = resultDivisao;
            
            int x = (int) posInf;
            int y = (int) posSup;
            
            double valorX = (double) vetorValores[x];
            double valorY = (double) vetorValores[y];
            
            this.resultMediana = (valorX + valorY) / 2.0;
            
            this.segundo_passo += "Para acharmos a mediana, precisamos dividir ";
            this.segundo_passo += "a lista dos números ao meio de forma igualitária, ";
            this.segundo_passo += "desta forma conseguimos encontrar a o intervalo necessário.\n\n";
            
            for (int j = 0; j < this.qtdeValores; j++) {
                
                if(x == j){
                    this.segundo_passo += ""
                        + "-| " + vetorValores[x] +"|"+ vetorValores[y] + " |- \t\t";
                   j++;
                }else{
                    this.segundo_passo += vetorValores[j] + "\r\t";
                }
                
            }
            
            this.segundo_passo += "\n\nCom o intervalo encontrado, teremos a seguinte expressão para calcular a mediana.\n\n";
            this.segundo_passo += "Expressão: ("+ valorX + "+" + valorY + ") / 2 = " + this.resultMediana;
            
        }else{
            
            int x = (int) resultDivisao;
            this.resultMediana = vetorValores[x];
            
            this.segundo_passo += "Para acharmos a mediana, precisamos dividir ";
            this.segundo_passo += "a lista dos números ao meio de forma igualitária, ";
            this.segundo_passo += "desta forma conseguimos encontrar a mediana.\n\n";
            
            for (int j = 0; j < this.qtdeValores; j++) {
                if(j == x){
                    this.segundo_passo += "-| " + vetorValores[x] + " |- \t";
               }else{
                    this.segundo_passo += vetorValores[j] + "\t";
                }
                
            }
            
        }

        return this.segundo_passo;
    }
    
}
