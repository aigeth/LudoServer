package LudoServer;




import AccountManager.AccountManager;
import Lobby.Lobby_General;
import Lobby.Lobby_Login;
import Model.Board;
import Model.Player;
import Model.Piece;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class LudoServer {
    
    ServerSocket ss;
    private Lobby_Login loginLobby;
    private AccountManager accountManager;


    boolean started = false;
    public static void main(String[] args) {
        new LudoServer();
    }

    public LudoServer(){
        accountManager = new AccountManager();
        loginLobby = new Lobby_Login(accountManager);
        
        try{
            ss = new ServerSocket(5454);
            while(true){
                Socket s = ss.accept();
                ServerConnection sc = new ServerConnection(s);
                sc.start();
                System.out.println("Client Connected with ip:" + s.getInetAddress() + s.getPort());
                loginLobby.refreshConnections();
                loginLobby.addConnection(sc);
                Thread.sleep(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(LudoServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(LudoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

