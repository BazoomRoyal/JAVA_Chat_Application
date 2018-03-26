package com.fredericboisguerin.insa;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javax.rmi.CORBA.Util;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class ListUtilisateur {

    private ArrayList<Utilisateur> UtilisateurList;


    /** Constructeur*/
    public ListUtilisateur() {
        this.UtilisateurList = new ArrayList<>();
    }

    /**Retourne si le pseudo rentré en paramètre n'est pas dans la liste**/
    public boolean estPasDansLaList (String name){
        Boolean unique = true ;
        for (Utilisateur U : UtilisateurList) {
            if (U.getPseudo().toLowerCase().equals(name.toLowerCase()) ) {
                unique = false ;
            }
        }
        return unique ;
    }

    /** Ajoute un utilisateur en s'assurant que le pseudo est valable et qu'il est unique */
    public void ajouterUtilisateur(String name,String adresse, Integer port) throws IOException {
        Utilisateur nouveau = new Utilisateur(name,adresse, port);
        if (this.estPasDansLaList(name)) {

            this.UtilisateurList.add(nouveau);
            this.miseAJourUtilisateurs(this.UtilisateurList);
        }
    }

    /** Enlève l'utilisateur passé en paramètre**/
    public void enleverUtilisateur(String name) throws IOException {
        Integer index = 0 ;
        Boolean effacer = false;
        Utilisateur actuel ;

        while (!effacer) {
            actuel = this.UtilisateurList.get(index) ;
            if (actuel.getPseudo().equals(name)) {
                effacer = this.UtilisateurList.remove(actuel);
                System.out.println(effacer);
            }
            index += 1 ;
        }
        this.miseAJourUtilisateurs(this.UtilisateurList);
    }

    /** Cherche le port associé à l'utilisateur rentré en paramètre**/
    public Integer retournerPort(String name){
        Integer port=0 ;
        for (Utilisateur U : UtilisateurList) {
            if (U.getPseudo().toLowerCase().equals(name.toLowerCase()) ) {
                port = U.getPort() ;
            }
        }
        return port ;
    }

    /** Cherche l'ip associé à l'utilisateur rentré en paramètre**/
    public String retournerIP(String name){
        String ip= "" ;
        for (Utilisateur U : UtilisateurList) {
            if (U.getPseudo().toLowerCase().equals(name.toLowerCase()) ) {
                ip = U.getIp() ;
            }
        }
        return ip ;
    }

    /**Afficher tous les utilisateurs*/
    public void afficherUtilisateurs() {
        for (Utilisateur U : UtilisateurList) {
            U.afficherPseudo();
        }
    }

    public boolean pseudo_unique (String name){
        Boolean unique = true ;
        if (name.equals(null)  || name.equals("")) {
            unique = false ;
            System.out.println(" Le pseudo rentré est nul.");
        }
        for (Utilisateur U : UtilisateurList) {
            if (U.getPseudo().toLowerCase().equals(name.toLowerCase()) ) {
                System.out.println("Le pseudo est déjà pris, veuillez en choisir un plus original");
                unique = false ;
            }
        }
        return unique ;
    }


    /**Effectue une mise à jour de la liste des utilisateurs et du document qui stock**/
    public void miseAJourUtilisateurs (ArrayList<Utilisateur> userList) throws IOException {
        String fileName = "src/main/resources/Utilisateurs.csv";
        String[] entree = new String[userList.size()] ;
        String actuel ;
        Integer index = 0 ;

        for (Utilisateur u : userList){
            actuel = u.userToString() ;
            entree[index]= actuel ;
            index += 1 ;
        }
            try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
                writer.writeNext(entree);
            }
    }

}
