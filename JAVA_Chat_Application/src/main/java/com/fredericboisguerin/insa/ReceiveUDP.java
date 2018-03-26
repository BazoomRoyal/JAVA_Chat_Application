package com.fredericboisguerin.insa;


import java.io.IOException;
import java.lang.String ;

/**Une écoute UDP qui va nous servir à savoir qui est en ligne à l'aide de requête, de réponses, etc**/
public class ReceiveUDP implements IncomingMessageListener {

    private UDPMessageSenderService udpMessageSenderService ;

    public ReceiveUDP() {
        this.udpMessageSenderService = new UDPMessageSenderService() ;
    }

    /**Lance une écoute en UDP sur un port donné**/
    public void lancerecoute(IncomingMessageListener incomingMessageListener, Integer port) throws Exception {
        UDPMessageReceiverService recepteur = new UDPMessageReceiverService(port, incomingMessageListener) ;
        Thread listenThread = new Thread(recepteur) ;
        listenThread.start() ;
    }

    /**Traite les messsages en fonction du message reçu (requête de conexion, réponse à une requête, utilisateur qui quitte**/
    @Override
    public void onNewIncomingMessage(String message) throws Exception {
        String deb = message.substring(0, message.indexOf("/"));
        String newmess = message.substring(message.indexOf("/")+1, message.indexOf("&")+1);
        if (deb.contains("reponse")||deb.contains("presente")) {
            ajouterUtilisateur(newmess);
        }
        else if(deb.contains("requete")){
            repondreRequete(newmess);
        }
        else if(deb.contains("quit")){
            enleverUtilisateur(newmess);
        }
    }

    /**Enleve un utilisateur de la liste => méthode appelée lorsqu'on reçoit un "quit"**/
    private void enleverUtilisateur(String newmess) throws IOException {
        String pseudo = newmess.substring(0, newmess.indexOf("/"));
        Launch.list.enleverUtilisateur(pseudo);
    }

    /**Répond à une requête en indiquant notre pseudo, notre IP et notre port TCP**/
    private void repondreRequete(String newmess) throws Exception {
        String ip = newmess.substring(0, newmess.indexOf("/"));
        newmess = newmess.substring(newmess.indexOf("/") + 1, newmess.indexOf("&")+1);
        Integer port = Integer.valueOf(newmess.substring(0, newmess.indexOf("/")));
        if((port - Launch.portTCP)!=0){
            udpMessageSenderService.sendMessageOn(ip, 4500, "reponse/"+ Launch.pseudo+"/"+ Launch.myIP+ "/"+Launch.portTCP+"/&" );
        }
    }

    /**Ajoute un utilisateur à la liste dans ces deux cas :
     * -On avait envoyé une requête en se connectant et on attend des réponses pour pouvoir avoir un pseudo unique
     * -Quelqu'un s'est connécté et a choisi son pseudo, il nous envoit donc un message de presentation "presente" avec son pseudo, son IP et son port TCP pour le joindre**/
    private void ajouterUtilisateur(String newmess) throws IOException {
        String pseudo = newmess.substring(0, newmess.indexOf("/"));
        newmess = newmess.substring(newmess.indexOf("/") + 1, newmess.indexOf("&")+1);
        String ip = newmess.substring(0, newmess.indexOf("/"));
        newmess = newmess.substring(newmess.indexOf("/") + 1, newmess.indexOf("&")+1);
        Integer port = Integer.valueOf(newmess.substring(0, newmess.indexOf("/")));
        if((port - Launch.portTCP)!=0) {
            Launch.list.ajouterUtilisateur(pseudo,ip, port);
        }
    }
}

