package com.goodtohave.servertools;

public abstract class ServerProgram implements ServerProgramInterface{

    String programName;

    public ServerProgram(String programName) {
        this.programName = programName;
    }
}
