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
public class CalcularDistribuicao {
    
    private String[] intervalos;
    private String[] frequencia;
    
    private double[] intervalo1 = new double[1000];
    private double[] intervalo2 = new double[1000];
    private double[] qtdeFrequencia = new double[1000];
    
    public void Calcular(String[] intervalos, String[] frequencia){
        
        this.intervalos = intervalos;
        this.frequencia = frequencia;
        
        FormatarDados();
        
    }
    
    public void FormatarDados(){
        
        double[] int1;
        double[] int2;
        double[] numIntervalos;
        
        int1 = new double[this.intervalos.length];
        int2 = new double[this.intervalos.length];
        numIntervalos = new double[this.frequencia.length];
        
        String[] aux;
        
        int i = 0;
        for (String valor: this.intervalos) {
            
            try{
                aux = valor.split("-");
                int1[i] = Double.parseDouble(aux[0]);
                int2[i] = Double.parseDouble(aux[1]);
                
            }catch(Exception e){
                System.out.println(e);
            }
                        
            i++;
        }
        
        i = 0;
        for (String valor: this.frequencia) {
            numIntervalos[i] = Double.parseDouble(valor);
            i++;
        }
        
        this.intervalo1 = int1;
        this.intervalo2 = int2;
        this.qtdeFrequencia = numIntervalos;
        
    }
    
    public double[] getIntervalo1(){
        return this.intervalo1;
    }
    
    public double[] getIntervalo2(){
        return this.intervalo2;
    }
    
    public double[] getNumIntervalos(){
        return this.qtdeFrequencia;
    }
    
}


//Calcula a media entre os intervalos X



