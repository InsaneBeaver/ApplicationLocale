/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author 1645720
 */
public class Grille extends JPanel{
    
    private ArrayList<Parent> listParent;

    public Grille(ArrayList listeParent) {
        this.listParent = listeParent;
        setLayout(null);
        setPreferredSize(new Dimension(300, listeParent.size()*40));
        
        addGrids();
        
    }
    
    private void addGrids(){
        JPanel grilleNom = new JPanel(new GridLayout(1, listParent.size()));
        JPanel grillePrenom = new JPanel(new GridLayout(1, listParent.size()));
        
        for (int i = 0; i < listParent.size(); i++) {
            JTextField txtNom = new JTextField(listParent.get(i).getNom());
            JTextField txtPrenom = new JTextField(listParent.get(i).getPrenom());
            
            txtNom.setSize(150, 40);
            txtPrenom.setSize(150, 40);
            
            grilleNom.add(txtNom);
            grillePrenom.add(txtPrenom);
        }
        
        grilleNom.setLocation(0, 0);
        grillePrenom.setLocation(grilleNom.getWidth(), 0);
        
        add(grilleNom);
        add(grillePrenom);
        
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        
//        g.fillRect(0, 0, 200, 200);
//    }
    
    
}
