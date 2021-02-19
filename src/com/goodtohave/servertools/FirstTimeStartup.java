package com.goodtohave.servertools;

import java.io.File;

class FirstTimeStartup {

    protected static void checkIfNecessaryArePresent() {
        String mainPath = Tools.getMainPath();
        if (mainPath != null) {
            File mainFolder = new File(mainPath);
            File configFile = new File(mainFolder.getPath() + File.separator + "config.properties");
            if (!configFile.exists()) {
                generateConfigFile();
            }else {
                Tools.printDevDebug("Config file already exists!");
            }
        }else {
            Tools.printDevDebug("Failed to create necessary folders");
        }

    }

    private static void generateConfigFile() {
        Tools.writeToFile("","config.properties","Server name=MyServer\nServer version=1.0.0\nServer build=0\nRequire authentication=false\nUser registration=true\nMaintenance mode=false");
    }
}
