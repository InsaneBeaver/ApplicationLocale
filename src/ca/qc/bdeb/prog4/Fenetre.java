/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author 1645720
 */
public class Fenetre extends JFrame{
//    private DefaultListModel listModel = new DefaultListModel();
//    private JList list = new JList(listModel);
    private JScrollPane scrPane = new JScrollPane();
    
    ArrayList<Parent> listParents = new ArrayList<Parent>();
    
            
    
    public Fenetre() {
        setTitle("Modification base de données parents");
        
        
        setParents();
        
        scrPane.setPreferredSize(new Dimension(200, 200));
        add(scrPane);
        
        
        
        setResizable(false);
        pack();
        
        setLayout(null);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void setParents(){
        listParents.add(new Parent("Palisson", "Hugo"));
        listParents.add(new Parent("Palisson", "Hugo"));
        listParents.add(new Parent("Palisson", "Hugo"));
        listParents.add(new Parent("Palisson", "Hugo"));
        
        scrPane.getViewport().add(new Grille(listParents));
        scrPane.setPreferredSize(new Dimension(300, 300));
        
    }
}
