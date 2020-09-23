/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lobby;

import AccountManager.Account;
import AccountManager.AccountManager;
import Model.Player;
import LudoServer.ServerConnection;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aigeth
 */
public class Lobby_General extends Lobby{
    public ArrayList<Lobby_Game> lobbies;
    public ArrayList<ServerConnection> connections;
    private Lobby_Login loginLobby;
    
    public Lobby_General(Lobby_Login loginLobby){
        connections = new ArrayList<>();
        lobbies = new ArrayList<>();
        this.loginLobby = loginLobby;
        
    }

    @Override
    public synchronized void commandHandler(String command, ServerConnection sc) {
        System.out.println("General commandhandler");  
        String[] message = command.split("\\\\n");
                
                switch (message[0]) {
                    case "Lobby_Create": //Condition: String password
                        String[] subMessage = message[1].split("£");
                        if(subMessage.length < 2){
                            return;
                        }
                        createLobby(sc, subMessage[0], subMessage[1]);
                        break;
                    case "Lobby_GetAll":
                        if(message.length == 1)
                            sc.sendAllLobbies(getAllLobbies());
                        else
                            sc.sendSeacrhedLobbies(findLobbies(message[1]));
                        break;
                    case "Lobby_Join": //Condition: int lobbyID
                        joinLobby(sc, Integer.parseInt(message[1]));
                        break;
                    case "Lobby_Join_Password": //Condition: int lobbyID, String password
                        subMessage = message[1].split("£");
                        if(subMessage.length < 2){
                            return;
                        }
                        joinPasswordLobby(sc, Integer.parseInt(subMessage[0]), subMessage[1]);
                        break;
                    case "Lobby_FindUser": //Condition: String name
                        sc.sendSeacrhedUsers(findUser(message[1]));
                        break;
                    case "404": //Condition: String name
                        removeConnection(sc);
                          System.out.println("Client disconnected" + sc.socket.getInetAddress());
                        break;
                    case "LogOut": //Condition: String name
                        logOut(sc);
                        System.out.println("Client logged out" + sc.socket.getInetAddress());
                    case "BackPressed":
                         logOut(sc);
                         System.out.println("Client logged out" + sc.socket.getInetAddress());
                        break;
                }
    }
    
    public Lobby_Game getGameLobbyById(int lobbyID){
        for(Lobby_Game gameLobby : getGameLobbies()){
            if(gameLobby.getId() == lobbyID){
                return gameLobby;
            }
        }
        return null;
    }
    
    public synchronized void joinLobby(ServerConnection sc, int lobbyID) {
        
        Lobby_Game gameLobby = getGameLobbyById(lobbyID); 
        if(gameLobby == null){
            sc.sendAllLobbies(getAllLobbies());
            return;
        }
        
        if(gameLobby.hasPassword()){
            sc.sendPasswordJoinScreen(lobbyID);
            return;
        }
        
        if(gameLobby.addConnection(sc)){
            removeConnection(sc);
        }
    }
    
    public synchronized void joinPasswordLobby(ServerConnection sc, int lobbyID, String password) {
        Lobby_Game gameLobby = getGameLobbies().get(lobbyID);
        
        if(!gameLobby.getPassword().equals(password)){
            sc.sendWrongPasswordJoinScreen();
            return;
        }
        
        try{
            if(gameLobby.addConnection(sc)){
                removeConnection(sc);
            }
        }catch(NullPointerException e){
                System.out.println(sc.getAccount().getFirstName() + " " + sc.getAccount().getLastName() + " tried to join a lobby with invalid lobby ID!");
        }
    }

    @Override
    public synchronized boolean addConnection(ServerConnection sc) {
        refreshConnections();
        sc.sendGeneralLobbyScreen();
        sc.setLobby(this);
        getConnections().add(sc);
        return true;
    }

    @Override
    public synchronized void removeConnection(ServerConnection sc) {
        getConnections().remove(sc);
        loginLobby.getAccountManager().removeAccount(sc.getAccount());
    }
    
    public synchronized void logOut(ServerConnection sc) {
        loginLobby.addConnection(sc);
        removeConnection(sc);
    }
    
    public synchronized ArrayList<Lobby_Game> getGameLobbies(){
        return lobbies;
    }

    public synchronized void createLobby(ServerConnection sc, String name, String password) {
        Account account = sc.getAccount();
        if(account.isAuthorized()){
            Lobby_Game gameLobby = new Lobby_Game(getGameLobbies().size(), sc, name, password, this);
            getGameLobbies().add(gameLobby);
            getConnections().remove(sc);
        }
    }
    
    /*public synchronized void createDummyLobby() {
        ServerConnection sc = new ServerConnection(new Socket());
        Lobby_Game gameLobby = new Lobby_Game(lobbies.size(), new Player("Sandra", 234, true, "https://scontent.farn1-1.fna.fbcdn.net/v/t1.0-1/c0.0.100.100a/p100x100/46519460_1146448985519159_7973793826554773504_n.jpg?_nc_cat=103&_nc_ht=scontent.farn1-1.fna&oh=b129a8d9b0504c397b2d2fbc1b673657&oe=5CB94EB8", sc), "");
        lobbies.add(gameLobby);
        
    }*/
    
    public synchronized String getAllLobbies(){
        String lobbys = "";
        for(Lobby_Game gameLobby : getGameLobbies()){
            lobbys += gameLobby.toString() + "\n";
        }
        
        return lobbys;
    }
    
    public synchronized String findUser(String name){
        List<Account> accounts = loginLobby.getAccountManager().searchByName(name).subList(0, 10);
        String userAccounts = "";
        for(Account account : accounts){
            userAccounts += account.toString() + "\n";
        }
        
        return userAccounts;
    }
    
    public synchronized String findLobbies(String name){
        String lobbys = "";
        for(Lobby_Game gameLobby : lobbies){
            if(gameLobby.getName().toUpperCase().contains(name.toUpperCase()))
                lobbys += gameLobby.toString() + "\n";
        }
        
        return lobbys;
    }
    
    public synchronized ArrayList<ServerConnection> getConnections(){
        return connections;
    }
    
    public final void refreshConnections(){

        ArrayList<ServerConnection> tmpSC = new ArrayList<>();
        for(ServerConnection sc: getConnections()){
            if(!sc.checkIfAlive()){
                tmpSC.add(sc);
                System.out.println("Client disconnected" + sc.socket.getInetAddress());
            }
        }

        for(ServerConnection sc: tmpSC){
            removeConnection(sc);
            sc.close();
        }


    }
    
    public Lobby_Login getLoginLobby(){
        return loginLobby;
    }
    
    public synchronized final void emptyLobbyRemover(Lobby_Game gameLobby){
       getGameLobbies().remove(gameLobby);
    }

}
