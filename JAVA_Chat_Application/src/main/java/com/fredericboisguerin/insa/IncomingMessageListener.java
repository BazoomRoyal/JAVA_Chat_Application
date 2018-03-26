package com.fredericboisguerin.insa;

import java.io.IOException;

public interface IncomingMessageListener {
    void onNewIncomingMessage(String message) throws Exception;
}
