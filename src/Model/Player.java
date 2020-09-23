package Model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import LudoServer.ServerConnection;
import java.util.ArrayList;

/**
 *
 * @author Aigeth
 */
public class Player {
    private String firstName, lastName, url;
    private int color = 10, tries = 0;
    private long id;
    private int startingPlace, endingPlace, boardType = 6, status = 0;
    boolean isFacebook, finished, disconnected = false;
    ArrayList<Piece> pieces;
    public ServerConnection serverConnection;
    
    
    public Player(String firstName, String lastName, long id, boolean isFacebook, String url, ServerConnection serverConnection) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.isFacebook = isFacebook;
        this.url = url;
        this.serverConnection = serverConnection;
        pieces = new ArrayList<>();
        finished = false;

    }
    
    public Piece getPiece(int id){
        for(Piece tmpPiece:pieces){
            if(tmpPiece.equalsPiece(color, id))
                return tmpPiece;
        }
        return null;
    }
    
    public void disconnected(){
        disconnected = true;
    }
    
    public void reconnected(){
        disconnected = false;
    }
    
    public void ready(){
        status = 1;
    }
    
    public boolean isReady(){
        return status == 1;
    }
    
    public void setInGame(){
        status = 2;
    }
    
    public boolean isInGame(){
        return status == 2;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getColor() {
        return color;
    }

    public int getStartingPlace() {
        return startingPlace;
    }

    public void setStartingPlace(int startingPlace) {
        this.startingPlace = startingPlace;
    }

    public int getEndingPlace() {
        return endingPlace;
    }

    public void setEndingPlace(int endingPlace) {
        this.endingPlace = endingPlace;
    }

    public boolean isIsFacebook() {
        return isFacebook;
    }

    public void setIsFacebook(boolean isFacebook) {
        this.isFacebook = isFacebook;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
    
    public void start(int color) {
        this.color = color;
        int startingPos = color * 10;
        int endingPos;
        if((color * 10) - 1 < 0)
            endingPos = (boardType * 10) -1;
        else
            endingPos = (color * 10) - 1;
        
        startingPlace = (boardType * 10) + (color*4);
        endingPlace = (boardType * 14) + (color*4);
        
        for(int i = 0; i < 4; i++)
            pieces.add(new Piece(i, color, startingPos, endingPos, startingPlace + i));
        
        setInGame();
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String statusToString() {
        String piecesToSend = "";
        for(Piece piece : pieces){
            piecesToSend = piece.statusToString() + "£";
        }
        return piecesToSend;
    }
    
    @Override
    public String toString() {
        String piecesToSend = "";
        for(Piece piece : pieces){
            piecesToSend += piece.toString() + "§";
        }
        return id + "£" + firstName + "£" + lastName + "£" + url + "£" + color + "£" + status + "£" + piecesToSend;
    }

    public int getTries() {
        return tries;
    }
    
    public void resetTries(){
        tries = 0;
    }
    
    public void incrementTries(){
        tries++;
    }
    
    public void decrementTries(){
        tries--;
    }
    
    
    
}
