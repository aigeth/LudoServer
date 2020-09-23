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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aigeth
 */
public class Lobby_Login extends Lobby{
    
    public Lobby_General generalLobby;
    public ArrayList<ServerConnection> connections;
    private AccountManager accountManager;
    public Lobby_Login(AccountManager accountManager){
        connections = new ArrayList<>();
        this.accountManager = accountManager;
        generalLobby = new Lobby_General(this); 
    }

    @Override
    public synchronized void commandHandler(String command, ServerConnection sc) {
        System.out.println("Login commandhandler"); 
        String[] message = command.split("\\\\n");
                
                switch (message[0]) {
                    case "Login_Guest"://Condition: String firstName
                        String[] subMessage = message[1].split("£");
                        if(subMessage.length < 2){
                            return;
                        }
                        addUser((int)(Math.random() * 100000), subMessage[0], subMessage[1], false, "http://graph.facebook.com/101691280931174/picture?type=square", sc);
                        break;
                    case "Login_Facebook": //Condition: int Facebook_id, String Facebook_first_Name, String Facebook_last_Name, String Facebook_url
                        subMessage = message[1].split("£");
                        if(subMessage.length < 3){
                            return;
                        }
                        addUser(Long.parseLong(subMessage[0]), subMessage[1], subMessage[2], true, subMessage[3], sc);
                        break;
                }
    }

    public synchronized void loginSuccessful(ServerConnection sc) {
        System.out.println("Login was successful " + sc.getAccount().toString());
        generalLobby.addConnection(sc);
        removeConnection(sc);
    }
    
    public synchronized void loginUnSuccessfull(ServerConnection sc) {
        System.out.println("login failed");
        sc.sendLoginFailed();
    }
    
    @Override
    public synchronized boolean addConnection(ServerConnection sc) {
        sc.setLobby(this);
        connections.add(sc);
        return true;
    }

    @Override
    public synchronized void removeConnection(ServerConnection sc) {
        connections.remove(sc);
    }
    
    public synchronized void addUser(long id, String firstName, String lastName, boolean isFacebook, String url, ServerConnection connection){
        refreshConnections();
        Account account = new Account(id, firstName, lastName, isFacebook, url);
        
        if(getAccountManager().addAccount(account)){
            connection.setAccount(account);
            loginSuccessful(connection);
            return;
        }
        
        loginUnSuccessfull(connection);
    }
    
    public final void refreshConnections(){
        generalLobby.refreshConnections();
        ArrayList<ServerConnection> tmpSC = new ArrayList<>();
        for(ServerConnection sc: connections){
            if(!sc.checkIfAlive()){
                tmpSC.add(sc);
                System.out.println("Client disconnected" + sc.socket.getInetAddress());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Lobby_Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        for(ServerConnection sc: tmpSC){
            sc.close();
        }

        connections.removeAll(tmpSC);    
  
    }
    
    public synchronized AccountManager getAccountManager(){
        return accountManager;
    }
    
    /*public void authorize(int id, String name, ServerConnection connection){
        Account account = accountManager.authorize(id, name);
        
        if(account != null){
            connection.setAccount(account);
            loginSuccessfull(connection);
        }
        
        loginUnSuccessfull(connection);
    }*/
    
}
