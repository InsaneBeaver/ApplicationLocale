/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4.Vue;

import ca.qc.bdeb.prog4.Outils.BaseDeDonnees;
import ca.qc.bdeb.prog4.Enfant;
import ca.qc.bdeb.prog4.Outils.ExcelAdapter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 1645720
 */
public class Fenetre extends JFrame{

    
    private final int COLONNE_NOM = 11;
    private final int COLONNE_PRENOM = 12;
    private final int COLONNE_AGE = 14;
    private final int COLONNE_TEL1 = 24;
    private final int COLONNE_TEL2 = 27;
    private final int COLONNE_DATE = 13;
    private final int COLONNE_SEXE = 15;
    private final int COLONNE_NAGE = 16;
    
    private final BaseDeDonnees baseDonnees;
    
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFichiers = new JMenu("Fichiers");
    private JMenu menuAide = new JMenu("Aide");
    private JMenuItem itemExcel = new JMenuItem("Importer un fichier Excel...");
    private JMenuItem itemBD = new JMenuItem("Importer la base de données actuellement utilisée");
    private JMenuItem itemUpload = new JMenuItem("Envoyer la liste affichée à la base de données");
    private JMenuItem itemAide = new JMenuItem("Importation");
    private JFileChooser fileChooser = new JFileChooser();
    private ExcelAdapter excelAdapter;
    
    private FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Fichier Excel (.xlsx)", "xlsx");
    
    private JScrollPane scrPane = new JScrollPane();
    
    private ArrayList<Enfant> listEnfant = new ArrayList<Enfant>();
    
    private String excelFilePath = null;
    
    
    
    /**
     * Crée la fenêtre principale du programme.
     * 
     * @param base (BaseDeDonnées) Base de données utilisé par le programme
     */
    public Fenetre(BaseDeDonnees base) {
        setTitle("Modification base de données parents");
        this.baseDonnees = base;
//        baseDonnees.clear();
//        setEnfants();
        
        scrPane.setPreferredSize(new Dimension(500, 500));
        
        scrPane.getVerticalScrollBar().setUnitIncrement(16);
        scrPane.getHorizontalScrollBar().setUnitIncrement(16);
        add(scrPane);
        
        setMenu();
        
        setResizable(false);
        pack();
        
        setLayout(null);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    
    /**
     * Récupère une liste d'objets Enfant du ExcelAdapter et l'utilise pour 
     * remplir le JScrollPane
     * 
     */
    private void setEnfantsExcel(){
        int ligne = 1;
        int colonne = 0;
        int nbParents = 0;
        
        String nomComplet;
        String nom = null;
        String prenom = null;
        String age = null;
        String sexe = null;
        String tel1 = null;
        String tel2 = null;
        Date dateNaissance;
        boolean saitNager;
        
        listEnfant.clear();
        
//        listEnfant = excelAdapter.getListParent();
        while(!excelAdapter.isNextLignesVides(ligne, COLONNE_NOM, 5)){
            
            nom = excelAdapter.getCellString(ligne, COLONNE_NOM);
            prenom = excelAdapter.getCellString(ligne, COLONNE_PRENOM);
            
            
            //System.out.println(nom);
            //System.out.println(prenom);
            
            age = ""+excelAdapter.getCellString(ligne, COLONNE_AGE);
            sexe = excelAdapter.getCellString(ligne, COLONNE_SEXE);
            
            tel1 = excelAdapter.getCellString(ligne, COLONNE_TEL1);
            tel2 = excelAdapter.getCellString(ligne, COLONNE_TEL2);
            dateNaissance = excelAdapter.getCellDate(ligne, COLONNE_DATE);
            
            if(excelAdapter.getCellString(ligne, COLONNE_NAGE).equalsIgnoreCase("O"))
                saitNager = true;
            else
                saitNager = false;
            
            
            //System.out.println(age);
            
            listEnfant.add(new Enfant(nom, prenom, tel1, tel2, sexe, dateNaissance, saitNager, false));
            
            ligne++;
        }
        
        
        
        
        
        triAlpha(listEnfant);
        scrPane.setViewportView(new Grille(listEnfant));
        
        revalidate();
        repaint();
    }
    
    /**
     * Récupère une liste d'objets Enfant de la base de données et l'utilise pour 
     * remplir le JScrollPane
     */
    private void setEnfantBD(){
        listEnfant = baseDonnees.getListEnfants();
        triAlpha(listEnfant);
        
        scrPane.setViewportView(new Grille(listEnfant));
        
        revalidate();
        repaint();
    }
    
    /**
     * Ajoute la barre de menu et ses items, ajoute des listeners sur ces même items
     */
    private void setMenu(){
        setJMenuBar(menuBar);
        menuBar.add(menuFichiers);
        menuBar.add(menuAide);
        menuFichiers.add(itemExcel);
        menuFichiers.add(itemBD);
        menuFichiers.addSeparator();
        menuFichiers.add(itemUpload);
        menuAide.add(itemAide);
        
        fileChooser.addChoosableFileFilter(excelFilter);
        fileChooser.setFileFilter(excelFilter);
        
        itemExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(null);
                String excelFilePath = null;
                
                try{
                    excelFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                }catch (NullPointerException err){}
                
                if(excelFilePath!=null){
                    excelAdapter = new ExcelAdapter(excelFilePath);
                    if(excelAdapter.isValidFile())
                        setEnfantsExcel();
                }
            }
        });
        
        itemBD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnfantBD();
            }
        });
        
        itemUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baseDonnees.clear();
                baseDonnees.uploadListEnfants(listEnfant);
                
                
            }
        });
                
        itemAide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "<Importer un fichier Excel...> : Importe un fichier de type Classeur Excel(.xlsx) qui sera utilisé pour lister les enfants"
                        + "\n<Importer la base de données actuellement utilisée> : Importe la liste présente dans la base de données de ce programme et de l'application android");
            }
        });
        
        
    }
    
//    public static void triAlphabetique(ArrayList<Enfant> list){
//        int pos = 0;
//        
//        for (int i = 0; i < list.size(); i++) {
//            
//            for (int j = 0; list.get(i).getNom().compareTo(list.get(j).getNom())>0; j++) {
//                pos = j;
//                    
//            }
//             
//            list.add(pos, list.remove(i));
//        }
//        
//        
//    }
    
    /**
     * Trie une ArrayList d'objets Enfants en ordre alphabétique de leur nom.
     * 
     * @param list 
     */
    public static void triAlpha(ArrayList<Enfant> list){
        
        String min;
        int posMin;
        
        Enfant placeholder = null;
        
        for (int i = 0; i < list.size(); i++) {
            min = list.get(i).getNom();
            posMin = i;
            for (int j = i; j < list.size(); j++) {
                if (list.get(j).getNom().compareTo(min) < 0){
                    min = list.get(j).getNom();
                    posMin = j;
                }
            }
            
            
            placeholder = list.get(posMin);
            list.add(posMin, list.remove(i));
            list.remove(placeholder);
            list.add(i, placeholder);
            
            
            
        }
    }
    
    
}
