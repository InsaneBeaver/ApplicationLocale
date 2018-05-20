/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.prog4.Outils;

import java.io.File;


/**
 *
 * @author 1645720
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class ExcelAdapter {
    
    private File file = null;
    private String fileName = null;
    private Workbook wb;
    
    /**
     * Crée un ExcelAdapter à partir du nom ou du path d'un document excel de type .xlsx
     * 
     * @param fileName nom ou du path d'un document excel
     */
    public ExcelAdapter(String fileName) {
        this.fileName = fileName;
        
        try{
            InputStream inp = new FileInputStream(fileName);
            wb = new XSSFWorkbook(inp);
        }catch (FileNotFoundException e){
            System.out.println("Blargh");
            System.exit(0);
        }catch (IOException e){
            System.out.println("Kek");
            System.exit(0);
        }catch (org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException e){
            JOptionPane.showMessageDialog(null, "Format incorrect"
                    + "\nSi votre document est un document Excel d'un format autre que .xlsx, "
                    + "\nveuillez exporter votre document Excel en format .xlsx"
                    + "\nDans Excel 2016 : Fichiers/Exporter/Modifier type de fichier/Classeur(.xlsx)");
        }
    }
    
//    public ExcelAdapter(File file) {
//        this.file = file;
//        
//        try{
//            InputStream inp = new FileInputStream(file);
//            wb = new XSSFWorkbook(inp);
//        }catch (FileNotFoundException e){
//            System.out.println("Blargh");
//            System.exit(0);
//        }catch (IOException e){
//            System.out.println("Kek");
//            System.exit(0);
//        }
//    }
    
    /**
     * Retourne la chaine de caractères se trouvant dans le document excel dans 
     * la cellule à l'intersection d'une ligne et d'une colonne précisées.
     * 
     * @param ligne
     * @param colonne
     * @return Chaine de caractère (String) contenu dans la cellule désignée
     */
    public String getCellString(int ligne, int colonne){
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(ligne);
        
        String valeur;
        
        try{
            Cell cell = row.getCell(colonne);
            cell.setCellType(CellType.STRING);
            valeur = cell.getStringCellValue();
        }catch (NullPointerException e){
            valeur = "";
        }
        
        
        return valeur;
    }
    
    /**
     * Retourne la date se trouvant dans le document excel dans 
     * la cellule à l'intersection d'une ligne et d'une colonne précisées.
     * 
     * @param ligne
     * @param colonne
     * @return Date (format Date) contenu dans la cellule désignée
     */
    public Date getCellDate(int ligne, int colonne){
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(ligne);
        
        Date valeur;
        
        try{
            Cell cell = row.getCell(colonne);
            
            valeur = cell.getDateCellValue();
        }catch (NullPointerException e){
            valeur = null;
        }
        
        
        return valeur;
    }
    
    
    /**
     * Retourne la valeur numérique se trouvant dans le document excel dans 
     * la cellule à l'intersection d'une ligne et d'une colonne précisées.
     * 
     * @param ligne
     * @param colonne
     * @return Valeur numérique entière (int) contenu dans la cellule désignée
     */
    public int getCellInt(int ligne, int colonne){
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(ligne);
        
        int valeur;
        
        try{
            Cell cell = row.getCell(colonne);
            //System.out.println(cell.getNumericCellValue());
            valeur = (int) cell.getNumericCellValue();
        }catch (NullPointerException e){
            valeur = 0;
        }
        
        
        return valeur;
    }
    
    
    
//    public ArrayList<Enfant> getListParent(){
//        ArrayList<Enfant> list = new ArrayList<Enfant>();
//        
//        Sheet sheet = wb.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//        
//        String nom = null;
//        String prenom = null;
//        
//        while(rowIterator.hasNext()){
//            
//            
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();
//            
//            nom = cellIterator.next().getStringCellValue();
//            prenom = cellIterator.next().getStringCellValue();
//            
//            list.add(new Enfant(nom, prenom));
//            
//        }
//        
//        
//        return list;
//    }
    
    /**
     * Retourne true si un certain nombre de cellules en dessous d'une cellule 
     * précisée sont toute vide. Autrement, retourne false.
     * 
     * @param ligne Ligne de la cellule désignée
     * @param colonne Colonne de la cellule désignée
     * @param nbLignes Nombre de cellules à vérifier en dessous de la cellule désignée
     * @return 
     */
    public boolean isNextLignesVides(int ligne, int colonne, int nbLignes){
        boolean vide = true;
        
        for (int i = 0; i < nbLignes && vide; i++) {
            vide = getCellString(ligne+i, colonne).equals("");
        }
        
        return vide;
    } 
    
    /**
     * Vérifie si le path ou le nom du fichier est null ou pas.
     * 
     * @return false si le path ou le nom du fichier est null, true autrement.
     */
    public boolean isValidFile(){
        return (fileName != null);
    }
}
