package com.fredericboisguerin.insa;



import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**Le thread en TCP pour lancer une écoute et gérer l'initialisation de connexion, la fermeture etc**/
public class TCPMessageReceiverService extends Thread implements MessageReceiverService {
    private Integer port ;
    private IncomingMessageListener incomingMessageListener ;
    private ServerSocket serverSocket ;
    private BufferedReader reader ;
    private Boolean coInitialisee ;
    private Boolean coTerminee ;


    public TCPMessageReceiverService(Integer port, IncomingMessageListener incomingMessageListener) throws IOException {
        this.port=port ;
        this.incomingMessageListener = incomingMessageListener;
        this.serverSocket = new ServerSocket(port);
        this.coInitialisee = false ;
        this.coTerminee = false;
    }

    /**Lancement de l'écoute sur un port donné**/
    @Override
    public void listenOnPort(int port, IncomingMessageListener incomingMessageListener) throws Exception {

        if(!this.coInitialisee) {
            this.initialiserConnexion();
        }
        String message = this.reader.readLine();
        if (message != null) {
            if(message.contains("fermeture/&")){
                incomingMessageListener.onNewIncomingMessage(message);
                fermerConnexion();
            }
            else{
                incomingMessageListener.onNewIncomingMessage(message);
            }
        }
    }

    /**Initialise une connexion **/
    private void initialiserConnexion() throws IOException {
        Socket chatSocket = serverSocket.accept();
        InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());
        this.reader = new BufferedReader(stream);
        this.coInitialisee = true ;
    }

    /**Permet de fermer la connexion**/
    private void fermerConnexion() throws IOException {
        this.reader.close();
        this.serverSocket.close();
        this.coTerminee = true ;
    }

    public void run(){
        try{
            while(!coTerminee){
                this.listenOnPort(this.port, this.incomingMessageListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
