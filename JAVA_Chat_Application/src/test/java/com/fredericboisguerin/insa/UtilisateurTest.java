package com.fredericboisguerin.insa;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilisateurTest {

    private ByteArrayOutputStream out;

    private String standardOutput() {
        return out.toString();
    }

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

   /* @org.junit.Test
    public void doit_afficher_pseudo (){

        Utilisateur userTest = new Utilisateur("Jacques") ;
        userTest.afficherPseudo();

        String expectedOutput = "Jacques";
        assertThat(standardOutput(), containsString(expectedOutput));
    }*/

   /* @org.junit.Test
    public void doit_ajouter_deux_utilisateurs()  {
        ListUtilisateur list = new ListUtilisateur();
        list.ajouterUtilisateur("Jacques");
        list.ajouterUtilisateur("Robert");

        list.afficherUtilisateurs();

        String standardOutput = standardOutput();
        String firstUtilisateurInfo = "Jacques";
        String secondUtilisateurInfo = "Robert";
        assertThat(standardOutput, containsString(firstUtilisateurInfo));
        assertThat(standardOutput, containsString(secondUtilisateurInfo));
    }*/

    /*@org.junit.Test(expected = InvalidPseudoException.class)
    public void fail_if_no_name() throws InvalidPseudoException{
        ListUtilisateur list = new ListUtilisateur();
        String noName = null;

        list.ajouterUtilisateur(noName);
    }

    @org.junit.Test(expected = InvalidPseudoException.class)
    public void fail_if_name_is_empty() throws InvalidPseudoException{
        ListUtilisateur list = new ListUtilisateur();
        String emptyName = "";

        list.ajouterUtilisateur(emptyName);
    }*/


}
