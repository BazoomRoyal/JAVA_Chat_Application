package com.fredericboisguerin.insa;

public interface MessageSenderService {

    void sendMessageOn(String ipAddress, int port, String message) throws Exception;
}
