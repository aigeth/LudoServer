/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import Model.Player;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aigeth
 */
public class GameState_Roll extends GameState{
    
    public GameState_Roll(GameState gameState) {
        super(gameState); 
        System.out.println("GameState_Roll");
        sendTurn();
    }

    @Override
    public GameState diceRolled(Player issuedPlayer) {
        if(!getLobby().getPlayers().getTurn().equals(issuedPlayer)){
            return this;
        }
        
        for(Player player: getLobby().getPlayers().getPlayers()){
            player.serverConnection.sendDiceRolling();
        }
        
        Random rand = new Random();
        
        try {
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(500) + 800);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameState_Roll.class.getName()).log(Level.SEVERE, null, ex);
        }
        getBoard().setDice(1 + rand.nextInt(5), issuedPlayer);
        
        for(Player player: getLobby().getPlayers().getPlayers()){
            player.serverConnection.sendDice(getBoard().getDice());
        }
        
        if(getBoard().anythingToMove(issuedPlayer)){
            return new GameState_Pick(this);
        }
        getLobby().getPlayers().next();
        return new GameState_Roll(this); 
    }
    
    private void sendTurn(){
        Player turn = getLobby().getPlayers().getTurn();
        for(Player player: getLobby().getPlayers().getPlayers()){
            player.serverConnection.sendTurn(turn.getColor());
        }
        
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
