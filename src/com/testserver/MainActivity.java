package com.testserver;

import com.goodtohave.servertools.GTHServer;

public class MainActivity {

    public static void main(String args[]) {
        GTHServer server = new GTHServer(5684, "votingapp");
        server.addProgram(new VotingHandler("votinghandler"));
        server.addProgram(new FunProgram("funprogram"));
        server.startServer();
    }
}
