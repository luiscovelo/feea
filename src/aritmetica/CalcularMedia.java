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
public class CalcularMedia {
    
    private String[] valores;
    private double resultadoMedia;
    private double somatorioTotal = 0.0;
    private double resultadoSoma  = 0.0;
    private int qtdeValores = 0;
    
    private String primeiro_passo = "";
    private String segundo_passo  = "";
    
    public double CalcularMedia(String[] valores){
        
        this.valores = valores;
        this.qtdeValores = valores.length;
        
        try {
            
            for (String valor : valores) {
                this.somatorioTotal += Double.parseDouble(valor);
            }

            this.resultadoMedia = this.somatorioTotal / this.qtdeValores;
            
        }catch(NumberFormatException e){
            System.out.println(e);
        }
        
        return this.resultadoMedia;
        
    }
    
    public String PrimeiroPasso(){
        
        this.primeiro_passo += "Primeiro é necessário somar os valores:\n\n";
        
        for (String valor : this.valores) {
            this.primeiro_passo += valor + " + ";
            this.resultadoSoma += Double.parseDouble(valor);
        }
        
        if (this.primeiro_passo.length() > 0) {
            this.primeiro_passo = this.primeiro_passo.substring (0, this.primeiro_passo.length() - 2);
        }
        
        this.primeiro_passo += " = " + this.resultadoSoma;
        
        return this.primeiro_passo;
    }
    
    public String SegundoPasso(){
        
        this.segundo_passo += "Com o resultado do 1º passo, efetue a divisão de '"+this.resultadoSoma+"' pela ";
        this.segundo_passo += "quantidade de números informada,\n no caso "+this.qtdeValores+":\n\n";
        this.segundo_passo += this.resultadoSoma+" / " + this.qtdeValores + " = " + this.resultadoMedia;
        
        return this.segundo_passo;
    }
    
}
