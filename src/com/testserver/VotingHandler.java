package com.testserver;

import com.goodtohave.servertools.ReceivedRequest;
import com.goodtohave.servertools.ServerProgram;

public class VotingHandler extends ServerProgram {

    public VotingHandler(String programName) {
        super(programName);
    }

    @Override
    public void receivedCommand(ReceivedRequest receivedRequest) {
        System.out.println("ReceivedRequest! " + receivedRequest.getCommand());
        if (receivedRequest.getCommand().equals("testcon")) {
            receivedRequest.answer("testsuccess");
        }
    }
}
