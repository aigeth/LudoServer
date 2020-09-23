package Model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * state 0 = starting pos; state 1 = main game; state 2 = ending pos;
 */ 


/**
 *
 * @author Aigeth
 */
public class Piece {
    private int color;
    private int startingPos, endingPos, id, pos;
    
    
    public Piece(int id, int color, int startingPos, int endingPos, int pos){
        this.id = id;
        this.color = color;
        this.startingPos = startingPos;
        this.endingPos = endingPos;
        this.pos = pos;
    }
    
    public boolean equalsPiece(int color, int id){
        return this.id == id && this.color == color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStartingPos() {
        return startingPos;
    }

    public void setStartingPos(int startingPos) {
        this.startingPos = startingPos;
    }

    public int getEndingPos() {
        return endingPos;
    }

    public void setEndingPos(int endingPos) {
        this.endingPos = endingPos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    
    @Override
    public String toString(){
        return id + ":" + pos;
    }

    public String statusToString(){
        return id + ":" + pos;
    }
    

}
