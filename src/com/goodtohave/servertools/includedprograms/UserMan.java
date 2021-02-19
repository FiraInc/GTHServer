package com.goodtohave.servertools.includedprograms;

import com.goodtohave.servertools.GTHServer;
import com.goodtohave.servertools.ReceivedRequest;
import com.goodtohave.servertools.ServerProgram;

public class UserMan extends ServerProgram {

    GTHServer server;

    public UserMan(String programName, GTHServer server) {
        super(programName);
        this.server = server;
    }

    @Override
    public void receivedCommand(ReceivedRequest receivedRequest) {
        if (receivedRequest.getCommand().equals("login")) {
            if (server.userManager.authenticateUser(this, receivedRequest)) {
                receivedRequest.answer("You successfully signed in!");
            }else {
                receivedRequest.informError("Wrong credentials!");
            }
        }
    }
}
