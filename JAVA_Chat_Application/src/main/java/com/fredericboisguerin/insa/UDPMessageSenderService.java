package com.fredericboisguerin.insa;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.String ;

/**Le sender en UDP qui permet d'envoyer les messages qui vont servir à gérer les utilisateurs**/
public class UDPMessageSenderService implements MessageSenderService {
    @Override
    public void sendMessageOn(String ipAddress, int port, String message) throws Exception {
        DatagramSocket senderSocket = new DatagramSocket();
        byte[] data = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket (data, data.length);
        datagramPacket.setAddress(InetAddress.getByName(ipAddress));
        datagramPacket.setPort(port);
        senderSocket.send(datagramPacket);
        senderSocket.close();
    }


}