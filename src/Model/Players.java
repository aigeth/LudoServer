/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Aigeth
 */
public class Players {
    public ArrayList<Player> players;
    
    private Player turn = null;
    
    public Players(){
        players = new ArrayList<>();
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public void addPlayer(Player player){
        players.add(player);
    }
    
    public Player get(int i){
        return players.get(i);
    }
    
    public Player getPlayer(int id){
        for(Player player : players){
            if(player.getId() == id){
                return player;
            }
        }
        return null;
    }
    
    public Player getPlayer(String firstName, String lastName){
        for(Player player : players){
            if(player.getFirstName().equals(firstName) && player.getLastName().equals(lastName)){
                return player;
            }
        }
        return null;
    }
    
    public Player getTurn(){
        return turn;
    }
    
    public int getTurnIndex(){
        return players.indexOf(turn);
    }
    
    public int getSize(){
        return players.size();
    }
    
    public void start(){
        for(int i = 0; i < getSize(); i++){
            Player player = players.get(i);
            player.start(giveColor(i));
            System.out.println("Player color:" + player.getColor());
            player.serverConnection.sendStart();
        }
        turn = players.get(0);
        getTurn().incrementTries();
    }
    
    public void next(){
        if(players.get((getTurnIndex()+1)%players.size()).disconnected || players.get((getTurnIndex()+1)%players.size()).finished){
            turn = players.get((getTurnIndex()+1)%players.size());
            next();
        }
        turn = players.get((getTurnIndex()+1)%players.size());
        getTurn().incrementTries();
    }
    
    public void remove(Player player){
        players.remove(player);
    }
    
    public boolean isAllReady(){
        for(Player player : players){
            if(!player.isReady())
                return false;
        }  
        if(players.size() < 2){
            return false;
        }
        return true;
    }
    
    public boolean isEmpty(){
        return players.isEmpty();
    }

    @Override
    public String toString() {
        String stringToSend = "";
        for(Player player : getPlayers()){
            stringToSend += player.toString()+ "\n";
        }
        return stringToSend;
    }
    
    public String statusToString(){
        String stringToSend = "";
        for(Player player : getPlayers()){
            stringToSend += player.statusToString()+ "\n";
        }
        return stringToSend;
    }
    
    public int giveColor(int position){
        double value = 6/getSize();
        value = value * position;
        
        int x = (int) value;
        double y = value - x;
        
        if(y>0.5){
            return (int) (value + 0.5);
        }
        
        return x;
    }

}
