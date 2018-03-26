package com.fredericboisguerin.insa;

public class Utilisateur {

    /**Un utilisateur classique qui possède un pseudo, une adresse IP et un port pour être joignable**/
    private String pseudo;
    private String ip ;
    private Integer port ;

    public Utilisateur (String pseud , String ip, Integer p){
        this.pseudo=pseud ;
        this.ip=ip ;
        this.port=p ;
    }

    /**Afficher le pseudo de l'utilisateur**/
    public void afficherPseudo(){
        System.out.println(this.pseudo);
    }

    /**Renvoie le pseudo de l'utilisateur**/
    public String getPseudo() {
        return this.pseudo;
    }

    /**Renvoie le port de l'utilisateur**/
    public Integer getPort() {
        return this.port;
    }

    /**Renvoie l'adresse IP de l'utilisateur**/
    public String getIp(){return this.ip;}

    /**Crée un string à partir d'un utilisateur avec ses infos**/
    public String userToString() {
        return this.pseudo + "/" +this.ip+ "/"+ Integer.toString(this.port) ;
    }
}
