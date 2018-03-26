package com.fredericboisguerin.insa;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/** Le TCP sender qui permet d'initialiser une connexion et d'envoyer des messages en TCP**/
public class TCPMessageSenderService implements MessageSenderService {

    private Socket chatSocket ;
    private PrintWriter writer ;
    private Integer port ;
    private Boolean connectionInitialisee ;

    public TCPMessageSenderService(Integer p){
        this.port = p ;
        this.connectionInitialisee = false ;
    }


    /**Envoie d'un message donné à l'adresse IP donnée sur un port donné**/
    @Override
    public void sendMessageOn(String ipAddress, int port, String message) throws Exception {
        if(!connectionInitialisee){
            initialiserConnection();
        }
        writer.println(message);
        writer.flush();
    }

    /**Permet d'initialiser une connexion**/
    private void initialiserConnection() throws IOException {
        this.chatSocket = new Socket("127.0.0.1", this.port);
        this.writer = new PrintWriter(chatSocket.getOutputStream());
        this.connectionInitialisee = true;
    }
}
