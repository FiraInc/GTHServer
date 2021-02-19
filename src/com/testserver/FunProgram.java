package com.testserver;

import com.goodtohave.servertools.ReceivedRequest;
import com.goodtohave.servertools.ServerProgram;

public class FunProgram extends ServerProgram {

    public FunProgram(String programName) {
        super(programName);
    }

    @Override
    public void receivedCommand(ReceivedRequest receivedRequest) {
        if (receivedRequest.getCommand().equals("whattime")) {
            long timeMillis = System.currentTimeMillis();
            receivedRequest.informError(String.valueOf(timeMillis));
            receivedRequest.answer("Hello :)");
        }
    }
}
