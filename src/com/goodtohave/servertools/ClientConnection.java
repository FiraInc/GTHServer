package com.goodtohave.servertools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class ClientConnection {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean connectionClosed = false;
    private final int STRING_DATA = 0;

    private GTHServer server;

    private UserProfile userProfile;

    public ClientConnection(Socket socket, GTHServer server) {
        Tools.printDebug("A new connection to the server was made!");
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.server = server;

        startReadingMessages();
    }

    private void startReadingMessages() {
        while (!connectionClosed) {
            try {
                byte[] requestIDByte = new byte[18];
                inputStream.read(requestIDByte);
                String requestIDString = new String(requestIDByte, StandardCharsets.UTF_8);
                long requestIDLong = Long.parseLong(requestIDString);

                byte[] dataTypeByte = new byte[1];
                inputStream.read(dataTypeByte);
                String dataTypeString = new String(dataTypeByte, StandardCharsets.UTF_8);
                int dataType = Integer.parseInt(dataTypeString);
                if (dataType == STRING_DATA) {
                    Tools.printDebug("Trying to read received string...");
                    readString(requestIDLong);
                }else {
                    Tools.printError("Error, could not find datatype: " + dataType);
                }
            } catch (IOException e) {
            }
        }
    }

    private void readString(long requestID) throws IOException {
        byte[] stringLengthByte = new byte[10];
        inputStream.read(stringLengthByte);

        String stringLenthString = new String(stringLengthByte, StandardCharsets.UTF_8);
        int stringLength = Integer.parseInt(stringLenthString);

        Tools.printDevDebug("stringLength: " + stringLenthString);

        byte[] readString = new byte[stringLength];
        inputStream.read(readString);
        String receivedString = new String(readString, StandardCharsets.UTF_8);
        Tools.printDevDebug("messageBeforeSplit: " + receivedString);

        String[] receivedMessageSplit = receivedString.split("#newInfo;");

        String program = receivedMessageSplit[0];
        String command = receivedMessageSplit[1];


        ReceivedRequest receivedRequest = new ReceivedRequest(this);
        receivedRequest.setRequestID(requestID);
        receivedRequest.setCommand(command);

        if (receivedMessageSplit.length > 2) {
            String[] args = new String[receivedMessageSplit.length-2];

            for (int i = 2; i < receivedMessageSplit.length; i++) {
                args[i-2] = receivedMessageSplit[i];
            }
            receivedRequest.setArgs(args);
        }

        server.launchProgramWith(program, receivedRequest);
    }

    protected void closeConnection() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionClosed = true;
    }

    protected void sendAnswer(ReceivedRequest receivedRequest, String stringToSendBack) {
        String reqString = String.valueOf(receivedRequest.getRequestID());
        byte[] requestBytes = reqString.getBytes(StandardCharsets.UTF_8);

        byte[] dataType = String.valueOf(STRING_DATA).getBytes(StandardCharsets.UTF_8);

        byte[] sendBackStringBytes = stringToSendBack.getBytes(StandardCharsets.UTF_8);

        String sendBackStringLength = String.valueOf(stringToSendBack.length());
        sendBackStringLength = addZeros(sendBackStringLength);
        byte[] sendBackStringLengthByte = sendBackStringLength.getBytes(StandardCharsets.UTF_8);

        byte[] allByteArray = new byte[requestBytes.length + dataType.length + sendBackStringLengthByte.length + sendBackStringBytes.length];
        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(requestBytes);
        buff.put(dataType);
        buff.put(sendBackStringLengthByte);
        buff.put(sendBackStringBytes);

        byte[] bytesJoined = buff.array();

        try {
            outputStream.write(bytesJoined);
            outputStream.flush();
            Tools.printDebug("Sent answer " + receivedRequest.getRequestID() + ". String it tried to send: " + stringToSendBack);
        } catch (IOException e) {
            Tools.printError("Failed to send answer with request " + receivedRequest.getRequestID() + ". String it tried to send: " + stringToSendBack);
            e.printStackTrace();
        }
    }

    protected void sendBytes(ReceivedRequest receivedRequest, Byte[] bytesToSend) {

    }

    public void sendError(ReceivedRequest receivedRequest, String errorMessage) {
        sendAnswer(receivedRequest, "#error;#newInfo;" + errorMessage);
    }

    protected String addZeros(String beforeZero) {
        String stringBefore = beforeZero;
        stringBefore.length();

        for (int i = 0; i < 18-stringBefore.length();i++) {
            stringBefore = "0" + stringBefore;
        }

        return stringBefore;
    }

    protected void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
