package com.goodtohave.servertools;

import com.goodtohave.servertools.includedprograms.UserMan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GTHServer {

    private ArrayList<ServerProgram> programs;
    private boolean showServerWindow = true;
    private int port;

    private ServerSocket serverSocket;
    private ArrayList<ClientConnection> connections;

    private boolean serverRunning = false;

    public UserManager userManager;

    public GTHServer(int port, String serverName) {
        if (!ServerConfig.loadConfigFromFiles()) {
            return;
        }

        programs = new ArrayList<>();
        connections = new ArrayList<>();
        Tools.serverName = serverName;
        this.port = port;
        FirstTimeStartup.checkIfNecessaryArePresent();

        userManager = new UserManager();
        addProgram(new UserMan("userman",this));
    }

    public void setShowServerWindow(Boolean visible) {
        showServerWindow = visible;
    }

    public void addProgram(ServerProgram program) {
        programs.add(program);
    }

    public void startServer() {
        Tools.printDebug("Attempting to start server...");
        if (!serverRunning) {
            serverRunning = true;
            if (showServerWindow) {
                // show window
            }
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startAcceptingConnections();
        }else {
            Tools.printError("Error, this server is already running!");
        }
    }

    private void startAcceptingConnections() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.printDebug("Server now open for connections on port: " + port + "!");
                while (serverRunning) {
                    try {
                        Socket newConnection = serverSocket.accept();
                        if (newConnection != null) {
                            createNewClientWithSocket(newConnection);
                        }
                    } catch (IOException e) {

                    }
                }
            }
        });
        thread.start();
    }

    private void createNewClientWithSocket(Socket newConnection) {
        ClientConnection clientConnection = new ClientConnection(newConnection, this);
        connections.add(clientConnection);
    }

    protected void launchProgramWith(String programName, ReceivedRequest receivedRequest) {
        Tools.printDevDebug("Searching for program: " + programName);
        for (int i = 0; i < programs.size(); i++) {
            if (programs.get(i).programName.equals(programName)) {
                programs.get(i).receivedCommand(receivedRequest);
                return;
            }
        }

        Tools.printError("Error, did not find requested program: " + programName);
    }

    public void stopServer() {
        Tools.showAlert("Server shutting down...");
        serverRunning = false;
        closeConnections();
    }

    private void closeConnections() {
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).closeConnection();
        }
    }
}
