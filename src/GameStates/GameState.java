/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import Lobby.Lobby_Game;
import Model.Board;
import Model.Player;

/**
 *
 * @author Aigeth
 */
public abstract class GameState {
    private long timer = 0;
    private final Lobby_Game lobby;
    private final Board board;
    
    public GameState(Lobby_Game lobby, Board board){
        setTimer(System.currentTimeMillis());
        this.lobby = lobby;
        this.board = board;
    }
    
    protected GameState(GameState gameState){
        setTimer(System.currentTimeMillis());
        this.lobby = gameState.getLobby();
        this.board = gameState.getBoard();
    }
    
    public GameState diceRolled(Player issuedPlayer){
        return error(issuedPlayer);
    }
    
    public GameState piecePicked(Player issuedPlayer, int piece){
        return error(issuedPlayer);
    }
    
    public GameState addPlayer(Player player, Player issuedPlayer){
        return error(issuedPlayer);
    }
    
    public GameState kickPlayer(Player issuedPlayer, int pos){
        return this;
    }
    
    public GameState ready(Player issuedPlayer){
        return error(issuedPlayer);
    }
    
    public GameState backPressed(Player issuedPlayer){
        getLobby().moveToGeneralLobby(issuedPlayer.getServerConnection());
        return this;
    }
    
    private GameState error(Player issuedPlayer){
        issuedPlayer.serverConnection.sendProtocolError();
        return this;
    }
    
    public GameState animationDone(Player issuedPlayer, int piece){
        return error(issuedPlayer);
    }
    
    public long getTimer() {
        return timer;
    }

    private void setTimer(long timer) {
        this.timer = timer;
    }
    
    public boolean hasTimedOut(){
        return (System.currentTimeMillis() > timer + 10000) && (timer != 0);
    }

    public final Lobby_Game getLobby() {
        return lobby;
    }
    
    public final Board getBoard() {
        return board;
    }
        
    public void parseCommand(Player issuedPlayer, String command){
    }
    
    public abstract GameState playerDisconnected(Player player);
    
    public abstract GameState playerReconnected(Player player);
    
    public abstract GameState timeout();
    
    public final void sendStatus(int color, int id, int pos){
        for(Player player : getLobby().getPlayers().players){
            player.serverConnection.sendStatus(color, id, pos);
        }
    }   

    public final void finishedSound() {
       lobby.sendStringToAllClients("Finished£");
    }
    
    public final void sendPickAPiece(Player player) {
       lobby.sendStringToAllClients("PickAPiece£" + player.getColor());
    }
    
    public final void sendDiceRolled(int dice) {
        lobby.sendStringToAllClients("Dice£" + dice);
    }
    
    public String getEverything(){
        String players = "";
        for(Player player : getLobby().getPlayers().getPlayers()){
            players+= player.toString() + "\n";
        }
        return players;
    }
    
    public final void sendEverything(){
        String string = getEverything();
        for(Player player : getLobby().getPlayers().players){
            player.serverConnection.sendAllUsersInLobby(string);
            player.serverConnection.sendColor();
        }
    }


    
}
