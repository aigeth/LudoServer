package Model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Lobby.Lobby;
import Lobby.Lobby_Game;
import LudoServer.ServerConnection;
import LudoServer.ServerConnection;
import Model.Player;
import Model.Piece;
import java.util.ArrayList;

/**
 *
 * @author Aigeth
 */
public class Board {
    BoardPlace[] boardPlace;
    private int dice;
    private Lobby_Game gameLobby;
    private final int boardType = 6;
    
    public Board (Lobby_Game gameLobby){
        boardPlace = new BoardPlace[145];
        this.gameLobby = gameLobby;
        for(int i = 0; i < boardPlace.length; i++){
            boardPlace[i] = new BoardPlace(isSafeZone(i));
        }
    }

    public int getBoardType() {
        return boardType;
    }

    public boolean isSafeZone(int i){
        if(i>= boardType * 10){
            return true;
        }
        return i%10 == 0;
    }
    
    public boolean anythingToMove(Player player){
        boolean anythingToMove = notStuck(player, dice);
        
        return anythingToMove && !player.isFinished() && !player.isDisconnected();
    }
    
    public boolean notStuck(Player player, int dice){
        boolean outSide = false;
        boolean stuck = true;
        boolean canGoOut = false;
        dice++;
        
        for(Piece tmpPiece: player.getPieces()){
            if(tmpPiece.getPos() < boardType * 10)
                outSide = true;
            
            if(tmpPiece.getPos() >= boardType * 10 && tmpPiece.getPos() < boardType * 14)
                canGoOut = true;
            
            canGoOut = canGoOut && (dice == 1 || dice == 6);
            
            if(tmpPiece.getPos() + dice > player.getEndingPlace() + 4 && tmpPiece.getPos() >= boardType * 14){
                stuck = stuck && true;
            }else if(tmpPiece.getPos() >= boardType * 14 || (tmpPiece.getEndingPos() - tmpPiece.getPos() <= 1 && dice >= 5 && tmpPiece.getPos() < boardType * 10)){
                stuck = false;
            }
        }

        return outSide || !stuck || canGoOut;
    }
    
    public void startingPiece(int dice, Player player, Piece piece){
        System.out.println("dice in startinpiece:" + dice);
        if (dice == 1 || dice == 6){
            switchPlace(piece, piece.getStartingPos() + (dice-1), player.serverConnection);
        }
    }
    
    public void checkIfGonnaCut(Piece piece, Player player){
        ArrayList<Piece> removedPiece = new ArrayList<>();
            
            for(Piece tmpPiece : boardPlace[piece.getPos()].pieces){
                if(piece.getColor() != tmpPiece.getColor() && !isSafeZone(piece.getPos())){
                    removedPiece.add(tmpPiece);
                    player.incrementTries();
                }
            }
            
            if(removedPiece.size() > 1){
                player.resetTries();
                return;
            }

            for(Piece tmpPiece: removedPiece){
                removePiece(tmpPiece, player);
            }
            
    }
    
    public void movePiece(Player player, Piece piece){
        int dice = this.dice + 1;
        if(dice == 1 || dice == 6){
            player.incrementTries();
        }
        
        if(piece.getPos()>=boardType * 14){
            if(piece.getPos() - player.getEndingPlace() + dice == 4){
                if(pieceDone(piece, player)){
                    player.resetTries();
                    return;
                }
                    
                player.incrementTries();
                return;
            }else if(piece.getPos() - player.getEndingPlace() + dice < 4){
                switchPlace(piece, piece.getPos() + dice, player.serverConnection);
                return;
            }else{
                return;
            }
            
        }
        
        if(piece.getPos()>=boardType * 10){
            startingPiece(dice, player, piece);
            return;
        }

        
        if(piece.getPos()+dice > piece.getEndingPos() && piece.getPos() <= piece.getStartingPos() - 1  && piece.getColor() != 0  && piece.getPos() < boardType * 10){ //Finishes the game
            if((dice-piece.getEndingPos()+piece.getPos()) - 1 + player.getEndingPlace() == player.getEndingPlace()+4){
                if(pieceDone(piece, player)){
                    player.resetTries();
                    return;
                }
                player.incrementTries();
                return;
            }else if((dice-piece.getEndingPos()+piece.getPos()) - 1 + player.getEndingPlace() > player.getEndingPlace()+4)
                return;
            
            switchPlace(piece, ((dice-piece.getEndingPos() + piece.getPos()) - 1) + player.getEndingPlace(), player.serverConnection);
            return;
        }else if(piece.getColor() == 0 && piece.getPos() + dice > piece.getEndingPos() && piece.getPos() < boardType * 10){
            if((dice-piece.getEndingPos()+piece.getPos()) - 1 + player.getEndingPlace() == (boardType * 14) + 4){
                if(pieceDone(piece, player)){
                    player.resetTries();
                    return;
                }
                player.incrementTries();
                return;
            }else if((dice-piece.getEndingPos()+piece.getPos()) - 1 + player.getEndingPlace() > (boardType * 14) + 4)
                return;
            
            System.out.println(piece.getPos() + dice + ":" + piece.getEndingPos());
            switchPlace(piece, ((dice-piece.getEndingPos()+piece.getPos()) - 1) + player.getEndingPlace(), player.serverConnection);
            return;        
        }
            
        int newPlace;
        if(piece.getPos()+dice >= boardType * 10){
            newPlace = piece.getPos()+dice-(boardType*10);
        }else{
            newPlace = piece.getPos()+dice;
        }
        switchPlace(piece, newPlace, player.serverConnection);

    }
    
    public void removePiece(Piece piece, Player player){
        switchPlace(piece, player.getStartingPlace() + piece.getId(), player.serverConnection);
    }
    
    public boolean pieceDone(Piece piece, Player player){
        switchPlace(piece, boardType * 18, player.serverConnection);
        
        boolean finished = true;
        
        for(Piece tmpPiece: player.pieces){
            if(tmpPiece.getPos() != boardType * 18){
                finished = false;
            }
        }
        
        if(finished){
            player.finished = true;
            gameLobby.getGameState().finishedSound();
            System.out.println("Player " + player.getColor() + " finished");
        }
        
        return finished;
        
    }
    
    public void switchPlace(Piece piece, int second, ServerConnection sc){
        boardPlace[second].pieces.add(piece);
        boardPlace[piece.getPos()].pieces.remove(piece);
        piece.setPos(second);
        gameLobby.getGameState().sendStatus(piece.getColor(), piece.getId(), piece.getPos());
    }

    public int getDice() {
        return dice;
    }

    public void setDice(int dice, Player player) {
        player.decrementTries();
        this.dice = dice;
    }
    

}
