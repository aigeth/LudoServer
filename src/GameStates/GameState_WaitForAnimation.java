/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import Model.Player;

/**
 *
 * @author Aigeth
 */
public class GameState_WaitForAnimation extends GameState{

    public GameState_WaitForAnimation(GameState gameState) {
        super(gameState);
        System.out.println("GameState Wait for Animation");
    }

    @Override
    public GameState animationDone(Player issuedPlayer, int piece) {
        if(!getLobby().getPlayers().getTurn().equals(issuedPlayer)){
            return this;
        }
        
        getBoard().checkIfGonnaCut(issuedPlayer.getPiece(piece), getLobby().getPlayers().getTurn());
        
        if(getLobby().getPlayers().getTurn().getTries() == 0){
           getLobby().getPlayers().next();
        }
        
        return new GameState_Roll(this);
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
