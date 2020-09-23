/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lobby;

import AccountManager.Account;
import AccountManager.AccountManager;
import GameStates.*;
import Model.Board;
import Model.Player;
import Model.Players;
import LudoServer.ServerConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aigeth
 */
public class Lobby_Game extends Lobby{
    private Player host;
    private Players players;
    private GameState gameState;
    private int id;
    private String name, password;
    private Lobby_General generalLobby;
    
    public Lobby_Game(int id, ServerConnection sc, String name, String password, Lobby_General generalLobby){
        this.id = id;
        this.name = name;
        players = new Players();
        this.password = password;
        this.generalLobby = generalLobby;
        
        System.out.println("Adding new player");
        gameState = new GameState_Start(this, new Board(this));
        addConnection(sc);
        host = sc.getPlayer();
        refreshConnections();
    }
    
    @Override
    public synchronized void commandHandler(String command, ServerConnection sc) {
        Player issuedPlayer = sc.getPlayer();
        String[] message = command.split("\\\\n");
        switch (message[0]) {
            case "DiceRolled":
                gameState = gameState.diceRolled(issuedPlayer);
                break;
            case "GetAllUsers":
                sc.sendAllUsersInLobby(getAllPlayers());
                break;
            case "AnimationDone"://Condition: int movedPiece
                gameState = gameState.animationDone(issuedPlayer, Integer.parseInt(message[1]));
                break;
            case "moveSent"://Condition: int movingPiece
                gameState = gameState.piecePicked(issuedPlayer, Integer.parseInt(message[1]));
                break;
            case "sendText":
                sendStringToAllClients(command);
                break;
            case "command":
                gameState.parseCommand(issuedPlayer, message[1]);
                break;
            case "ready":
                gameState = gameState.ready(issuedPlayer);
                break;
            case "404":
                gameState = gameState.playerDisconnected(issuedPlayer);
                break;
            case "BackPressed":
                gameState = gameState.backPressed(issuedPlayer);
                break;
            case "PiecePicked":
                gameState = gameState.piecePicked(issuedPlayer, Integer.parseInt(message[1]));
                break;
            case "Kick"://Condition int pos
                gameState = gameState.kickPlayer(issuedPlayer, Integer.parseInt(message[1]));
                break;
            default:
                break;
        }
    }

    public synchronized Player getHost() {
        return host;
    }

    public synchronized Players getPlayers() {
        return players;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }
    
    public synchronized boolean isEmpty(){
        return getPlayers().isEmpty();
    }

    @Override
    public final synchronized boolean addConnection(ServerConnection sc) {
        System.out.println("Adding connection");
        if(getPlayers().getSize() >= 6){
            return false;
        }
        
        Account account = sc.getAccount();
        Player player = new Player(account.getFirstName(), account.getLastName(), account.getId(), account.isFacebook(), account.getUrl(), sc);
        
        player.serverConnection.setLobby(this);
        System.out.println("Sending game lobby screen");
        player.serverConnection.sendGameLobbyScreen();
        player.serverConnection.setPlayer(player);
        getPlayers().addPlayer(player);
        gameState.sendEverything();
        return true;
    }
    
    public final synchronized boolean addDummyPlayer(String nmae, String ln) {
        System.out.println(getPlayers().getSize());
        if(getPlayers().getSize() >= 6){
            return false;
        }
        Player player = new Player(nmae, ln, 3435435+getPlayers().getSize(), true, "http://graph.facebook.com/108358553588927/picture?type=square", null);

        getPlayers().addPlayer(player);
        
        return true;
    }

    @Override
    public synchronized void removeConnection(ServerConnection sc) {
        getPlayers().remove(sc.getPlayer());
        if(getPlayers().isEmpty()){
            generalLobby.emptyLobbyRemover(this);
            return;
        }
        gameState.sendEverything();
    }
    
    public synchronized void moveToGeneralLobby(ServerConnection sc) {
        System.out.println("Client left lobby!");
        generalLobby.addConnection(sc);
        removeConnection(sc);
        gameState.sendEverything();
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized int getId() {
        return id;
    }
    
    public synchronized boolean hasPassword(){
        return !password.equals("-");
    }

    public String getName() {
        return name;
    }

    @Override
    public synchronized String toString() {
        return id + "£" + name + "£" + host.getUrl()+ "£" + getPlayers().getSize() + "£" + hasPassword();
    }
    
    public synchronized String getAllPlayers() {
        return getPlayers().toString();
    }
    
    public void sendStringToAllClients(String text) {
        for(Player player : getPlayers().getPlayers()){
            player.getServerConnection().sendStringToClient(text);
        }
    }
    
    public final void refreshConnections(){
        Thread thread = new Thread(){
            public void run(){
              while(true){
                    ArrayList<Player> tmpPlayer = new ArrayList<>();
                    for(Player player: getPlayers().players){
                        if(!player.getServerConnection().checkIfAlive()){
                            tmpPlayer.add(player);
                            System.out.println("Client disconnected" + player.getServerConnection().socket.getInetAddress());
                        }
                    }

                    for(Player player: tmpPlayer){
                        generalLobby.getLoginLobby().getAccountManager().removeAccount(player.getServerConnection().getAccount());
                        player.getServerConnection().close();
                        removeConnection(player.serverConnection);
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lobby_Game.class.getName()).log(Level.SEVERE, null, ex);

                }
              }
            }
        };
        
        thread.start();
    }
    
}
