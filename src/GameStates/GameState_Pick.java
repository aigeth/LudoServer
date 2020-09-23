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
public class GameState_Pick extends GameState{

    public GameState_Pick(GameState gameState) {
        super(gameState);
        System.out.println("GameState_pick");
        sendPickAPiece(getLobby().getPlayers().getTurn());
    }

    @Override
    public GameState piecePicked(Player issuedPlayer, int piece) {
        if(!getLobby().getPlayers().getTurn().equals(issuedPlayer)){
            return this;
        }
        
        getBoard().movePiece(issuedPlayer, issuedPlayer.getPiece(piece));

        return new GameState_WaitForAnimation(this);
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
