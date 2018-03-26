package com.fredericboisguerin.insa;

import java.io.IOException;

/** Le but de cette classe est de lancer une écoute continue
 * dans le programme afin de d'écouter si un autre utilisateur veut initier une connexion avec nous**/
public class TCPEcouteContinue implements IncomingMessageListener {

    /**Lance un thread qui écoute les message en TCP**/
    public void lancerecoute(IncomingMessageListener incomingMessageListener, Integer port) throws IOException {
        TCPMessageReceiverService recepteur = new TCPMessageReceiverService(port, incomingMessageListener) ;
        Thread chatThread = new Thread(recepteur);
        chatThread.start();
    }

    /**Traitement du message reçu**/
    @Override
    public void onNewIncomingMessage(String message) throws Exception {
        String deb = message.substring(0, message.indexOf("/"));
        String newmess = message.substring(message.indexOf("/")+1, message.indexOf("&")+1);
        if(deb.contains("chattons")){
            nouveauChat(newmess);
        }
    }

    /**Crée la fenêtre de chat si quelqu'un veut parler avec nous**/
    private void nouveauChat(String newmess) throws IOException {
        String pseudo = newmess.substring(0, newmess.indexOf("/"));
        newmess = newmess.substring(newmess.indexOf("/") + 1, newmess.indexOf("&")+1);
        Integer portDest = Integer.valueOf(newmess.substring(0, newmess.indexOf("/")));
        newmess = newmess.substring(newmess.indexOf("/") + 1, newmess.indexOf("&")+1);
        Integer portSource = Integer.valueOf(newmess.substring(0, newmess.indexOf("/")));
        Interface chat = new Interface(pseudo, Launch.list.retournerIP(pseudo), portDest, portSource ) ;
    }
}
