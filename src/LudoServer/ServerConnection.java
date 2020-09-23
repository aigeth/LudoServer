package LudoServer;




import Model.Player;
import AccountManager.Account;
import Lobby.Lobby;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection extends Thread{
    
    public Socket socket;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true, admin = false;
    private Player player;
    InetAddress ip;
    public int port, id;
    private Lobby lobby = null;
    private Account account = null;
    
    public ServerConnection(Socket socket) {
        super("ServerConnectionThread");
        this.socket = socket;
        player = null;
    }
    
    public void sendStringToClient(String text){
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException ex) {
            close();
        }
    }

    public void run(){
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            while(shouldRun){
                
                while (din.available() == 0){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        if(!shouldRun)
                            return;
                        else
                            close();
                    }
                }
                
                String textIn = din.readUTF();
                System.out.println(textIn);
                lobby.commandHandler(textIn, this);
            }
        } catch (IOException ex) {
            if(shouldRun)
                close();
        }
        
    }
    
    
    public void close(){

        try {
            shouldRun = false;
            /*if(server.board.getTurn() == this.player)
                server.board.nextTurn().serverConnection.sendRollTheDice();*/
            din.close();
            dout.close();
            socket.close();
        } catch (IOException | NullPointerException ex) {
        } 
        //sendStringToAllClients("sendText£ " + name + "£ disconnected!");
        
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public boolean checkIfAlive(){
        try {
            dout.writeUTF("Heartbeat\n");
            dout.flush();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    public void sendLoginFailed(){
        sendStringToClient("Lobby_loginfailed\n");
    }

    public void sendGeneralLobbyScreen(){
        sendStringToClient("Lobby_General\n");
    }
    
    public void sendGameLobbyScreen(){
        sendStringToClient("Lobby_Game\n");
    }
    
    public void sendAllLobbies(String lobbies){
        sendStringToClient("AllLobbies\n" + lobbies);
    }
    
    public void sendAllUsersInLobby(String users){
        sendStringToClient("AllLobbyUsers\n" + users);
    }
    
    public void sendSeacrhedUsers(String users){
        sendStringToClient("SearchedUsers\n" + users);
    }
    
    public void sendSeacrhedLobbies(String lobbies){
        sendStringToClient("AllLobbies\n" + lobbies);
    }
    
    public void sendKickDenied(){
        sendStringToClient("Kick_Denied\n");
    }
    
    public void sendPasswordJoinScreen(int id){
        sendStringToClient("PasswordJoinScreen\n" + id);
    }
    
    public void sendWrongPasswordJoinScreen(){
        sendStringToClient("PasswordWrongJoinScreen\n");
    }
    
    public void sendProtocolError(){
        sendStringToClient("Error\n");
    }
    
    public void sendKicked(){
        sendStringToClient("Kicked\n");
    }
    
    public void sendStart(){
        sendStringToClient("Game_Start\n" + player.getColor());
    }
    
    public void sendTurn(int color){
        sendStringToClient("Your_Turn\n" + color);
    }
    
    public void sendColor(){
        sendStringToClient("Color\n" + player.getColor());
    }
    
    public void sendDiceRolling(){
        sendStringToClient("DiceRolling\n");
    }
    
    public void sendDice(int dice){
        sendStringToClient("DiceStopRolling\n" + dice);
    }
    
    public void send(int dice){
        sendStringToClient("DiceStopRolling\n" + dice);
    }
    
    public void sendStatus(int color, int id, int pos){
        sendStringToClient("Status\n" + color + "£" + id + "£" + pos);
    }
}
