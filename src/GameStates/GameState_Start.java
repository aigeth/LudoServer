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
public class GameState_Start extends GameState{

    public GameState_Start(Lobby_Game lobby, Board board) {
        super(lobby, board);
        System.out.println("GameState_Start");
    }
    
    @Override
    public GameState addPlayer(Player player, Player issuedPlayer){
        getLobby().getPlayers().addPlayer(player);
        sendEverything();
        return this;
    }
    
    @Override
    public GameState kickPlayer(Player issuedPlayer, int pos){
        if(issuedPlayer.equals(getLobby().getHost())){
            getLobby().getPlayers().getPlayers().get(pos).getServerConnection().sendKicked();
            getLobby().moveToGeneralLobby(getLobby().getPlayers().getPlayers().get(pos).getServerConnection());
            return this;
        }
        
        sendEverything();
        return this;
    }
    
    @Override
    public GameState ready(Player player){
        player.ready();
        if(getLobby().getPlayers().isAllReady()){
            getLobby().getPlayers().start();
            sendEverything();
            return new GameState_Roll(this);
        }
        sendEverything();
        return this;
    }
    
    @Override
    public GameState playerDisconnected(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public GameState playerReconnected(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GameState timeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
