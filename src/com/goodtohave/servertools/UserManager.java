package com.goodtohave.servertools;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.goodtohave.servertools.includedprograms.UserMan;

public class UserManager {

    public boolean authenticateUser(UserMan userManProgram, ReceivedRequest receivedRequest) {
        if (userManProgram != null) {
            String email = receivedRequest.getArgs()[0];
            String password = receivedRequest.getArgs()[1];

            String userIDSDatabase = Tools.readFromFile("userids.json");
            String usersDatabase = Tools.readFromFile("userids.json");

            String salt;
            String hashedPassword;

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode node = objectMapper.readTree(userIDSDatabase);
                String userID = node.get(email).asText();

                JsonNode users = objectMapper.readTree(usersDatabase);
                salt = users.get(userID).get("salt").asText();
                hashedPassword = users.get(userID).get("hashedpassword").asText();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }

            CryptoManager cryptoManager = new CryptoManager();
            String typedInPassCrypt = cryptoManager.getHashSaltedPassword(password,salt);

            if (typedInPassCrypt.equals(hashedPassword)) {
                UserProfile userProfile = new UserProfile("","");
                receivedRequest.getClientConnection().setUserProfile(userProfile);
                Tools.printDebug("A user just signed in!");
                return true;
            }else {
                Tools.printDebug("A user just tried to sign in with the wrong credentials: " + typedInPassCrypt);
            }
        }else {
            Tools.printError("Wrong use of authenticateUser. You should run ServerConnection.authenticateUser on the client");
        }
        return false;
    }

    public boolean registerNewUser(UserMan userManProgram, ReceivedRequest receivedRequest) {
        if (userManProgram != null) {
            String database = Tools.readFromFile("users.json");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode actualObj = objectMapper.readTree(database);

                ObjectNode newUserNode = objectMapper.createObjectNode();
                //newUserNode.set("", )

               // ((ObjectNode) actualObj).set("sadhfh5343", )
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            Tools.printError("Wrong use of authenticateUser. You should run ServerConnection.authenticateUser on the client");
        }
        return false;
    }
}
