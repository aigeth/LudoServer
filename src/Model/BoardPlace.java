package Model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Model.Piece;
import java.util.ArrayList;

/**
 *
 * @author Aigeth
 */
public class BoardPlace {
    
    ArrayList<Piece> pieces;
    boolean safezone;
    
    public BoardPlace(boolean safeZone){
        pieces = new ArrayList<>();
    }
    
    public Piece getPiece(Piece piece){
        for(Piece tmpPiece: pieces){
            if(tmpPiece.getColor() == piece.getColor()){
                return tmpPiece;
            }
        }
        return null;
    }
    
    
    
}
