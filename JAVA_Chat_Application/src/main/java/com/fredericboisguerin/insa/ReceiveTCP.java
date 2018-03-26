package com.fredericboisguerin.insa;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Object ;
import java.lang.String ;
import java.net.SocketException;

/** Un receiveTCP qui est utilisé lors du chat pour traiter les messages**/
public class ReceiveTCP implements IncomingMessageListener{

    private Interface monChat ;

    public ReceiveTCP(Interface i){
        this.monChat = i ;
    }

    /**Lance l'écoute sur un port donné**/
    public void lancerecoute(IncomingMessageListener incomingMessageListener, Integer port) throws IOException {
        TCPMessageReceiverService recepteur = new TCPMessageReceiverService(port, incomingMessageListener) ;
        Thread chatThread = new Thread(recepteur);
        chatThread.start();
    }

    /**Traitement du message avec arrêt de la fenêtre si on recoit un message de fermeture**/
    public void onNewIncomingMessage(String message) throws Exception {
        if(message.contains("fermeture/&")) {
            this.monChat.ecrireHistorique();
            this.monChat.dispose();
        }
        else{
            String newmess = (this.monChat.pseudo+ " : " + message+"\n") ;
            this.monChat.espaceTexte.ajouterTexte(newmess);
            this.monChat.historique.add(newmess) ;
        }
    }
}
