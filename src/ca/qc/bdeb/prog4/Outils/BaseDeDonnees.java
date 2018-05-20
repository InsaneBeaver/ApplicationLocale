
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
    
//    public void viderEnfants(){
//        Statement stmt = null;
//        try{
//            stmt = connexion.createStatement();
//        }catch(SQLException e){
//            
//        }
//        String delete = "DELETE * FROM " + TABLE_ENFANTS;
//        ResultSet rs = null;
//        
//        try{
//            rs = stmt.executeQuery(delete);
//            
//        }catch(SQLException e){
//            
//        }
//        
//        
//        
//        try{
//            while(rs.next()){
//                System.out.println("mdr");
//            }
//            
//        }catch(SQLException e){
//            System.out.println("Loop");
//        }catch(NullPointerException e){
//            System.out.println(e.getMessage());
//        }
//    }
    
    public String getStringEnfant(int id) throws SQLException
    {
        JSONObject obj = new JSONObject();
        ResultSet rs = connexion.createStatement().executeQuery("Select * from " + TABLE_ENFANTS + " where id=" + id);
        if(!rs.next()) return "{}";
        obj.put("nom", rs.getString("nom"));
        obj.put("prenom", rs.getString("prenom"));
        obj.put("saitNager", rs.getInt("saitNager") == 1);
        obj.put("sexe", rs.getString("sexe"));
        obj.put("id", id);
        obj.put("estPresent", rs.getInt("estPresent") == 1);
        obj.put("dateNaissance", rs.getString("dateNaissance"));
        return obj.toString(); 

    }
    
    
    public Enfant getEnfant(int id) throws SQLException
    {
        JSONObject obj = new JSONObject();
        ResultSet rs = connexion.createStatement().executeQuery("Select * from " + TABLE_ENFANTS + " where id=" + id);
        if(!rs.next()) return null;
        obj.put("nom", rs.getString("nom"));
        obj.put("prenom", rs.getString("prenom"));
        obj.put("saitNager", rs.getInt("saitNager") == 1);
        obj.put("sexe", rs.getString("sexe"));
        obj.put("id", id);
        obj.put("estPresent", rs.getInt("estPresent") == 1);
        obj.put("dateNaissance", rs.getString("dateNaissance"));
        
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
    
    public void changerPresence(int id, String nouvelEtat) throws SQLException
    {
        connexion.createStatement().execute("UPDATE " + TABLE_ENFANTS + " SET estPresent='" + nouvelEtat + "' WHERE id=" + id);
    }
    
    public boolean estPermis(String hash, int id) throws SQLException
    {
        ResultSet rs = connexion.createStatement().executeQuery("SELECT enfants from " + TABLE_PARENTS_ENFANTS + " where mdphash='" + hash+"'");
        rs.next();
        JSONArray liste = new JSONArray(rs.getString("enfants"));
        boolean estPermis = false;
        for(int i = 0; i < liste.length() && !estPermis; i++) estPermis |= liste.getInt(i) == id;
        return estPermis;
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
                System.out.println("Erreur upload liste enfant");
            }
        }
    }
    
    public String getEnfants(String mdp) throws SQLException
    {
       ResultSet rs = connexion.createStatement().executeQuery("SELECT * from " + TABLE_PARENTS_ENFANTS + " where mdphash='" + getHash(mdp) + "'");
       if(!rs.next()) return "[]";
       
       JSONArray liste = new JSONArray(rs.getString("enfants"));
       JSONArray listeARetourner = new JSONArray();
       for(int i = 0; i < liste.length(); i++)
       {
           JSONObject infoEnfant = new JSONObject();
           int id = liste.getInt(i);
           ResultSet rs_enfant = connexion.createStatement().executeQuery("SELECT prenom, dateNaissance, estPresent from " + TABLE_ENFANTS + " where id=" + id);
           rs_enfant.next();

           infoEnfant.put("id", id);
           infoEnfant.put("prenom", rs_enfant.getString("prenom"));
           infoEnfant.put("dateNaissance", rs_enfant.getString("dateNaissance"));
           infoEnfant.put("estPresent", rs_enfant.getInt("estPresent") == 1);
           listeARetourner.put(infoEnfant);
       }
       return listeARetourner.toString();
    }
    
    /**
     * Ajoute un Enfant dans la base de données et lui assigne un ID unique
     * 
     * @param enfant Enfant à ajouter dans la base de données
     * @param id ID positif donné à l'Enfant
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
//        if(enfant.getDateNaissance() == null)
//            connexion.createStatement().execute("insert into " + TABLE_ENFANTS + " (nom, prenom, saitNager, sexe, id, estPresent, dateNaissance) values ('" + enfant.getNom() + "', '" + enfant.getPrenom() + "', " + valSaitNager + ", '" + 
//                enfant.getSexe() + "', " + valId + ", " + valEstPresent + ", '" + "" +"')");
//        else
//            connexion.createStatement().execute("insert into " + TABLE_ENFANTS + " (nom, prenom, saitNager, sexe, id, estPresent, dateNaissance) values ('" + enfant.getNom() + "', '" + enfant.getPrenom() + "', " + valSaitNager + ", '" + 
//                enfant.getSexe() + "', " + valId + ", " + valEstPresent + ", '" + enfant.getDateNaissance().toString()+"')");
    }
    
    public void mettreParent(String mdp, String listeIds) throws SQLException
    {
        System.out.println("insert into " + TABLE_PARENTS_ENFANTS + " values ('" + getHash(mdp) + "', '" + listeIds + "'");
        connexion.createStatement().execute("insert into " + TABLE_PARENTS_ENFANTS + " values ('" + getHash(mdp) + "', '" + listeIds + "')");
    }
    
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
