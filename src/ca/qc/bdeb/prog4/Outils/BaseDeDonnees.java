
package ca.qc.bdeb.prog4.Outils;
import ca.qc.bdeb.prog4.Enfant;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.json.*;


public class BaseDeDonnees {
    private final static String NOM_BASE = "CampDeJourbdeb.db";
    private final static String TABLE_PARENTS_ENFANTS = "parents";
    private final static String TABLE_ENFANTS = "enfants";
    
  
    Connection connexion;
    
    public BaseDeDonnees() throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        File f = new File(NOM_BASE);
        boolean existe = f.exists();
        
        connexion = DriverManager.getConnection("jdbc:sqlite:"+NOM_BASE);

        if(!existe)
        {
            connexion.createStatement().execute("CREATE TABLE " + TABLE_PARENTS_ENFANTS + " (mdphash varchar(256) NOT NULL UNIQUE, enfants varchar(1024))");
            connexion.createStatement().execute("CREATE TABLE " + TABLE_ENFANTS + " (nom varchar(64), prenom varchar(64), tel1 varchar(64), tel2 varchar(64), saitNager int, sexe varchar(1), id int NOT NULL UNIQUE, estPresent int, dateNaissance varchar(64))");
        }
    }
    
    
    /**
     * Vide la base de donnée
     */
    public void clear(){
        boolean stop = false;
        String delete;
        Statement stmt = null;
        
        try{
            stmt = connexion.createStatement();
        }catch(SQLException err){
            System.out.println(err.getMessage());
        }
        
        
        for (int i = 0; !stop; i++) {
            delete = "DELETE FROM " + TABLE_ENFANTS + " where id=" + i;
            
            try{
                stop = (getEnfant(i) == null);
                if(!stop)
                    stmt.executeUpdate(delete);
                
            }catch(SQLException err){
                System.out.println("Erreur ajout enfant");
            }catch(NullPointerException err){
                System.out.println(err.getMessage());
            }
            
        }
        
        
        try{
            stmt.close();
        }catch(SQLException err){
            System.out.println(err.getMessage());
        }
    }
    
    /**
     * Retourne l'enfant correspondant à l'ID en question
     * @param id L'ID
     * @return L'enfant
     * @throws SQLException 
     */
    public Enfant getEnfant(int id) throws SQLException
    {
        ResultSet rs = connexion.createStatement().executeQuery("Select * from " + TABLE_ENFANTS + " where id=" + id);
        if(!rs.next()) return null;
        
        Enfant enfant = null;
       
        if(rs.getString("dateNaissance")==null){
            try{
                enfant = new Enfant(rs.getString("nom"), rs.getString("prenom"), rs.getString("tel1"), rs.getString("tel2"), rs.getString("sexe"), null, rs.getInt("saitNager") == 1, rs.getInt("estPresent") == 1);
            }catch(SQLException err){
                System.out.println(err.getMessage());
            }
        }
        else{
            try{
                enfant = new Enfant(rs.getString("nom"), rs.getString("prenom"), rs.getString("tel1"), rs.getString("tel2"), rs.getString("sexe"), new java.util.Date(rs.getString("dateNaissance")), rs.getInt("saitNager") == 1, rs.getInt("estPresent") == 1);
            }catch(SQLException err){
                System.out.println(err.getMessage());
            }
        }
        
        return enfant;

    }
    
    
    /**
     * Retourne la liste de tous les enfants dans la base de données
     * 
     * @return ArrayList<Enfant> contenant tous les enfants de la base de données
     */
    public ArrayList<Enfant> getListEnfants(){
        ArrayList<Enfant> listEnfants = new ArrayList<>();
        boolean stop = false;
        
        for (int i = 0; !stop ; i++) {
            try{
                stop = (getEnfant(i) == null);
                if(!stop)
                    listEnfants.add(getEnfant(i));
                
            }catch(SQLException err){
                System.out.println("Erreur ajout enfant");
            }
            
            
            
        }
        
        return listEnfants;
    }
    
    /**
     * Import une liste d'objets de type Enfant et l'exporte dans la base de données
     * 
     * @param listEnfant ArrayList<Enfant> à exporter
     */
    public void uploadListEnfants(ArrayList<Enfant> listEnfant){
        for (int i = 0; i < listEnfant.size(); i++) {
            
            try{
                mettreEnfant(listEnfant.get(i), i);
            }catch(SQLException err){
                err.printStackTrace();
                System.out.println("Erreur upload liste enfant");
            }
        }
    }
    
    /**
     * Ajoute un enfant dans la base de données et lui assigne un ID unique
     * 
     * @param enfant Enfant à ajouter dans la base de données
     * @param id ID positif donné à l'enfant
     * @throws SQLException 
     */
    public void mettreEnfant(Enfant enfant, int id) throws SQLException
    {
        String valId = ""+id;
        String valSaitNager = enfant.saitNager() ? "1" : "0";
        String valEstPresent = enfant.isPresent() ? "1" : "0";
        
        try{
            if(enfant.getDateNaissance() == null)
                connexion.createStatement().execute("insert into " + TABLE_ENFANTS + " (nom, prenom, saitNager, sexe, id, estPresent, tel1, tel2) values ('" + enfant.getNom() + "', '" + enfant.getPrenom() + "', " + valSaitNager + ", '" + 
                    enfant.getSexe() + "', " + valId + ", " + valEstPresent + ", '" + enfant.getTel1() +"', '"+ enfant.getTel2() +"')");
               
            else
                connexion.createStatement().execute("insert into " + TABLE_ENFANTS + " (nom, prenom, saitNager, sexe, id, estPresent, dateNaissance, tel1, tel2) values ('" + enfant.getNom() + "', '" + enfant.getPrenom() + "', " + valSaitNager + ", '" + 
                    enfant.getSexe() + "', " + valId + ", " + valEstPresent + ", '" + enfant.getDateNaissance().toString()+"', '"+ enfant.getTel1() +"', '"+ enfant.getTel2() +"')");
        
        }catch(SQLException err){
            System.out.println(err.getMessage());
        }
        
        associerEnfantAParent(enfant, id);
    }
    
    /**
     * Ajoute l'enfant dans la liste des enfants associés à ce parent (en l'occurence au premier numéro de téléphone)
     * @param enfant L'enfant
     * @param id L'ID
     * @throws SQLException 
     */
    public void associerEnfantAParent(Enfant enfant, int id) throws SQLException
    {
        String numeroTelephone = enfant.getTel1();
        String motDePasseHache = getHash(calculerMotDePasse(numeroTelephone));
        ResultSet rs = connexion.createStatement().executeQuery("select * from " + TABLE_PARENTS_ENFANTS + " where mdphash='" + motDePasseHache+"'");
        if(!rs.next())
            connexion.createStatement().execute("insert into " + TABLE_PARENTS_ENFANTS + " values ('" + motDePasseHache + "','[" + id + "]');");
        
          
        else
        {
            JSONArray liste = new JSONArray(rs.getString("enfants"));
            liste.put(id);
            connexion.createStatement().execute("update " + TABLE_PARENTS_ENFANTS + " set enfants='" + liste.toString()+ "' where mdphash='" + motDePasseHache + "'");

        }
    }
    
    /**
     * Calcule le mot de passe à partir du numéro de téléphone, c'est-à-dire en supprimant tous les caractères non numériques.
     * P. ex. '(514) 102-1114  '=>5141021114
     * @param telephone Numéro de téléphone
     * @return Le mot de passe
     */
    public final static String calculerMotDePasse(String telephone)
    {
        // On forme un mot de passe à partir du numéro de téléphone
        String motDePasse = "";
        char caractere;
        for(int i = 0; i<telephone.length(); i++)
        {
            caractere = telephone.charAt(i);
            if(Character.isDigit(caractere))
                motDePasse += caractere;
        }
        return motDePasse;
    }
    
    /**
     * Hashe une chaîne avec sha-256.
     * @param chaine La chaine
     * @return Le hash
     */
    public final static String getHash(String chaine)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String hash = Base64.getEncoder().encodeToString(digest.digest(chaine.getBytes(StandardCharsets.UTF_8)));
            return hash;
        }
        catch(Exception e) {}
        return "";
    }
    
    
    

}
