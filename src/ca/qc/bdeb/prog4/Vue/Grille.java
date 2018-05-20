/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4.Vue;


import ca.qc.bdeb.prog4.Enfant;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author 1645720
 */
public class Grille extends JPanel{
    
    private ArrayList<Enfant> listEnfant;
    private final Font HEADER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 15);
    
    
    private ArrayList<JCheckBox> checklist = new ArrayList<>();
    
    private JPanel grilleNom;
    private JPanel grillePrenom;
    //private JPanel grilleAge;
    private JPanel grilleSexe;
    private JPanel grilleNaissance;
    private JPanel grilleTelephone1;
    private JPanel grilleTelephone2;
    private JPanel grillePresent;
    
    
//    private JPanel grilleSexe;
//    private JPanel grilleSaitNager;
    
    /**
     * Crée un JPanel contenant plusieurs GridLayout affichant différentes 
     * informations à partir des enfants dans la liste fournit en paramètres
     * 
     * @param listEnfant (ArrayList<Enfant>) liste d'enfants
     */
    public Grille(ArrayList<Enfant> listEnfant) {
        this.listEnfant = listEnfant;
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        setPreferredSize(new Dimension(getLongestGridSize()*9, listEnfant.size()*25));
        
        addGrids();
        setCheckboxes();
        
    }
    
    
    
    /**
     * Retourne le nombre de caractère des informations de l'enfant qui a le plus 
     * de caractères utilisé pour ses informations affiché.
     * 
     * @return (int) nombre de caraactères maximal
     */
    private int getLongestGridSize(){
        int max = 0;
        int longueur = 0;
        
        for (int i = 0; i < listEnfant.size(); i++) {
            longueur = listEnfant.get(i).getInfoLength();
            if(longueur > max)
                max = longueur;
        }
        
        
        return max;
    }
    
    /**
     * Crée les checkbox de présence des enfants et ajoute l'itemListener qui
     * change la présence de l'enfant associé.
     */
    private void setCheckboxes(){
        int marqueur = 0;
        
        for (int i = 0; i < listEnfant.size(); i++) {
            marqueur = i;
            
            checklist.get(marqueur).addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        listEnfant.get(checklist.indexOf((JCheckBox) e.getSource())).setIsPresent(true);
                    }
                    else{
                        listEnfant.get(checklist.indexOf((JCheckBox) e.getSource())).setIsPresent(false);
                    }
                }
            });
            
        }
    }
    
    /**
     * Remplit les grilles verticales avec les informations appropriées à chaque 
     * enfant et ajoute ces grille au layout.
     */
    private void addGrids(){
        grilleNom = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grillePrenom = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        //grilleAge = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grilleSexe = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grilleNaissance = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grilleTelephone1 = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grilleTelephone2 = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        grillePresent = new JPanel(new GridLayout(listEnfant.size()+1, 1));
        
        JTextField headerNom = new JTextField("Nom");
        JTextField headerPrenom = new JTextField("Prénom");
        //JTextField headerAge = new JTextField("Âge");
        JTextField headerSexe = new JTextField("Sexe");
        JTextField headerNaissance = new JTextField("Date Naissance");
        JTextField headerTel1 = new JTextField("Tél 1");
        JTextField headerTel2 = new JTextField("Tél 2");
        JTextField headerPresent = new JTextField("Présent");
        
        
        headerNom.setFont(HEADER_FONT);
        headerPrenom.setFont(HEADER_FONT);
        //headerAge.setFont(HEADER_FONT);
        headerSexe.setFont(HEADER_FONT);
        headerNaissance.setFont(HEADER_FONT);
        headerTel1.setFont(HEADER_FONT);
        headerTel2.setFont(HEADER_FONT);
        headerPresent.setFont(HEADER_FONT);
        
        headerNom.setEditable(false);
        headerPrenom.setEditable(false);
        //headerAge.setEditable(false);
        headerSexe.setEditable(false);
        headerNaissance.setEditable(false);
        headerTel1.setEditable(false);
        headerTel2.setEditable(false);
        headerPresent.setEditable(false);
        
        grilleNom.add(headerNom);
        grillePrenom.add(headerPrenom);
        //grilleAge.add(headerAge);
        grilleSexe.add(headerSexe);
        grilleNaissance.add(headerNaissance);
        grilleTelephone1.add(headerTel1);
        grilleTelephone2.add(headerTel2);
        grillePresent.add(headerPresent);
        
        for (int i = 0; i < listEnfant.size(); i++) {
            JTextField txtNom = new JTextField(""+listEnfant.get(i).getNom());
            JTextField txtPrenom = new JTextField(""+listEnfant.get(i).getPrenom());
            //JTextField txtAge = new JTextField(""+listEnfant.get(i).getAge());
            JTextField txtSexe = new JTextField(""+listEnfant.get(i).getSexe());
            JTextField txtNaissance = new JTextField(""+listEnfant.get(i).getDateNaissance());
            JTextField txtTel1 = new JTextField(""+listEnfant.get(i).getTel1());
            JTextField txtTel2 = new JTextField(""+listEnfant.get(i).getTel2());
            
            checklist.add(new JCheckBox());
            
            checklist.get(i).setSelected(listEnfant.get(i).isPresent());
            
            
            
            txtNom.setEditable(false);
            txtPrenom.setEditable(false);
            //txtAge.setEditable(false);
            txtSexe.setEditable(false);
            txtNaissance.setEditable(false);
            txtTel1.setEditable(false);
            txtTel2.setEditable(false);
                    
                    
            grilleNom.add(txtNom);
            grillePrenom.add(txtPrenom);
            
            grillePresent.add(checklist.get(i));
            
            //grilleAge.add(txtAge);
            grilleSexe.add(txtSexe);
            grilleNaissance.add(txtNaissance);
            grilleTelephone1.add(txtTel1);
            grilleTelephone2.add(txtTel2);
        }
        
        
        add(grilleNom);
        add(grillePrenom);
        
        add(grillePresent);
        
        //add(grilleAge);
        add(grilleSexe);
        add(grilleNaissance);
        add(grilleTelephone1);
        add(grilleTelephone2);
        
        
    }
  
    
    
}
