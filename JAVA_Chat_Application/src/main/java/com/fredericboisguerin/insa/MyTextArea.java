package com.fredericboisguerin.insa;

import javax.swing.*;
import java.awt.*;

public class MyTextArea extends JTextArea {

    public MyTextArea() {
        super();
        initialize();
    }

    /**Ecris un petit message de bienvenue**/
    private void initialize() {
        String l_texte = "Bienvenue dans le chat\n";
        this.append(l_texte);
    }

    /**Ajoute un texte donné à la zone de texte**/
    public void ajouterTexte(String message){
        this.append(message) ;
    }

}