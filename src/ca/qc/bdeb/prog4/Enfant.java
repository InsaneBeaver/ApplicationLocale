/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4;

import java.util.Date;

/**
 *
 * @author 1645720
 */
public class Enfant {
    private String nom;
    private String prenom;
    //private String age;
    private String tel1;
    private String tel2;
    private String sexe;
    private Date dateNaissance;
    private boolean isPresent;
    private boolean saitNager;

    
    /**
     * Crée un objet Enfant.
     * 
     * @param nom   (String) Nom de l'enfant
     * @param prenom    (String) Prénom de l'enfant
     * @param tel1  (String) Numéro de téléphone principal du premier répondant
     * @param tel2  (String) Numéro de téléphone principal du second répondant
     * @param sexe  (String) Sexe de l'enfant (M/F)
     * @param dateNaissance (Date) Date de naissance de l'enfant
     * @param saitNager (boolean) true si l'enfant sait nager, false dans le cas contraire
     * @param isPresent (boolean) true si l'enfant est présent, false dans le cas contraire
     */
    public Enfant(String nom, String prenom, String tel1, String tel2, String sexe, Date dateNaissance, boolean saitNager, boolean isPresent) {
        this.nom = nom;
        this.prenom = prenom;
        //this.age = age;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.saitNager = saitNager;
        this.isPresent = isPresent;
    }
    
    
    
//    public Enfant(String nom, String prenom, String tel1, String tel2, String sexe, Date dateNaissance) {
//        this.nom = nom;
//        this.prenom = prenom;
//        //this.age = age;
//        this.tel1 = tel1;
//        this.tel2 = tel2;
//        this.sexe = sexe;
//        this.dateNaissance = dateNaissance;
//        this.isPresent = false;
//    }

    

    
    
    
    
    

    
//    public Enfant(String nom, String prenom) {
//        this.nom = nom;
//        this.prenom = prenom;
//    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getInfoLength(){
        return (nom+prenom+sexe+tel1+tel2+dateNaissance).length() + 10;
    }

//    public String getAge() {
//        return age;
//    }

    public String getTel1() {
        return tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
        //System.out.println(isPresent + " " + nom);
    }

    

    public boolean saitNager() {
        return saitNager;
    }

    

    

    
    
}
