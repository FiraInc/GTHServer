package com.goodtohave.servertools;

import java.io.*;
import java.net.URISyntaxException;

import static javax.swing.JOptionPane.showMessageDialog;

class Tools {

    static String serverName = "";
    protected static boolean developerDecode = true;


    protected static void printError(String message) {
        System.out.println(ServerConfig.getServerName() + " (Error): " + message);
    }

    protected static void printDebug(String message) {
        System.out.println(ServerConfig.getServerName() + " (Debug): " + message);
    }

    protected static void printDevDebug(String message) {
        if (developerDecode) {
            System.out.println(ServerConfig.getServerName() + " (DevDebug): " + message);
        }
    }

    protected static void showAlert(String message) {
        showMessageDialog(null, message);
    }

    protected static String getMainPath() {
        try {
            String path = new File(Tools.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getPath();
            printDevDebug("Main path: " + path);
            return path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFromFile (String filename) {
        File file = new File(getMainPath() + File.separator + filename);
        if (!file.exists()) {
            printError("Could not find file: " + filename);
            return "";
        }
        StringBuilder text = new StringBuilder();
        try {
            int counter = 0;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (counter != 0) {
                    text.append("\n");
                }
                text.append(line);
                counter = counter + 1;
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (String.valueOf(text).equals("")) {
            return "";
        }else {
            return String.valueOf(text);
        }
    }

    public static void writeToFile (String directories, String filename, String value) {
        File file = new File(filename);
        File directoriesFile = new File(getMainPath() + File.separator + directories);
        if (!directoriesFile.mkdirs()) {
            printDevDebug("Error creating directory " + directoriesFile);
        }
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(directoriesFile + File.separator + file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
            outputStreamWriter.write(value);
            outputStreamWriter.flush();
            fileOutput.getFD().sync();
            outputStreamWriter.close();
            printDevDebug("Saved file " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            printDevDebug("Error saving file " + filename);
        }
    }
}
