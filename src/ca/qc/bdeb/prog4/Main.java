/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4;

import ca.qc.bdeb.prog4.Outils.BaseDeDonnees;
import ca.qc.bdeb.prog4.Vue.Fenetre;
import java.sql.SQLException;

/**
 *
 * @author 1645720
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BaseDeDonnees base1 = null;
        try{
            base1 = new BaseDeDonnees();
        }catch(Exception e)
        {
            e.printStackTrace();
            System.exit(0);
            
           
        }
        
        Fenetre fenetre1 = new Fenetre(base1);
        
        
    }
    
}
