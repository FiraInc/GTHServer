package com.goodtohave.servertools;

class ServerConfig {

    private static String serverName;
    private static String serverVersion;
    private static int serverBuild;
    private static boolean requireAuth;
    private static boolean userRegistration;
    private static boolean maintenanceMode;

    protected static boolean loadConfigFromFiles() {
        String mainConfig = Tools.readFromFile("config.properties");
        String[] configLines = mainConfig.split("\\r?\\n");
        for (String line:configLines) {
            String[] currentLine = line.split("=");
            if (currentLine.length != 2) {
                Tools.printError("Error in config file. This line: " + line + ", does not have an '=' character");
                return false;
            }
            switch (currentLine[0]) {
                case "Server name": {
                    serverName = currentLine[1];
                    break;
                }
                case "Server version": {
                    serverVersion = currentLine[1];
                    break;
                }
                case "Server build": {
                    serverBuild = Integer.parseInt(currentLine[1]);
                    break;
                }
                case "Require authentication": {
                    requireAuth = !currentLine[1].equals("false");
                    break;
                }
                case "User registration": {
                    userRegistration = currentLine[1].equals("true");
                    break;
                }
                case "Maintenance mode": {
                    maintenanceMode = currentLine[1].equals("true");
                    break;
                }
                default: {
                    Tools.printError("Syntax error in config file! Server don't know what this is: " + currentLine[0]);
                    return false;
                }
            }
        }
        return true;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        ServerConfig.serverName = serverName;
    }
}
