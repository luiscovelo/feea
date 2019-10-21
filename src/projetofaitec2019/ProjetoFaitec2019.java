/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetofaitec2019;

import javax.swing.JFrame;
import view.dashboard.Home;

/**
 *
 * @author COVELO
 */
public class ProjetoFaitec2019 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Home vd_Home = new Home();
        vd_Home.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vd_Home.setLocationRelativeTo(null);
        vd_Home.setVisible(true);
        
        //JanelaGrafico teste = new JanelaGrafico();
        //teste.setVisible(true);
                
    }
    
}
