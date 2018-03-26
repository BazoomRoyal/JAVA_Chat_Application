package com.fredericboisguerin.insa;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**L'UDP receiver qui va permettre de lancer une écoute**/
public class UDPMessageReceiverService extends Thread implements MessageReceiverService {

    private Integer port ;
    private IncomingMessageListener incomingMessageListener ;
    private DatagramSocket receiverSocket ;


    public UDPMessageReceiverService (Integer port, IncomingMessageListener incomingMessageListener) throws SocketException {
        this.port=port ;
        this.incomingMessageListener = incomingMessageListener;
        this.receiverSocket = new DatagramSocket(port);
    }

    /**Lancement d'une écoute sur le port choisi**/
    @Override
    public void listenOnPort(int port, IncomingMessageListener incomingMessageListener) throws Exception {
        DatagramPacket receivedPacket = new DatagramPacket(new byte[500], 500);
        this.receiverSocket.receive(receivedPacket);
        byte[] data = receivedPacket.getData();
        incomingMessageListener.onNewIncomingMessage(new String(data));
    }

    public void run(){
        Boolean oui = true ;
        try {
            while (oui){
                this.listenOnPort(this.port,this.incomingMessageListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
