package com.goodtohave.servertools;

public class ReceivedRequest {

    private ClientConnection clientConnection;
    private long requestID;
    private String command;
    private String[] args;

    protected ReceivedRequest(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void answer(String stringToSendBack) {
        clientConnection.sendAnswer(this, stringToSendBack);
    }

    public void sendBytes(Byte[] bytesToSend) {
        clientConnection.sendBytes(this, bytesToSend);
    }

    public void informError(String errorMessage) {
        clientConnection.sendError(this, errorMessage);
    }

    public long getRequestID() {
        return requestID;
    }

    protected void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    protected ClientConnection getClientConnection() {
        return clientConnection;
    }
}
