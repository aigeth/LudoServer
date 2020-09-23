/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lobby;

import Model.Player;
import LudoServer.ServerConnection;

/**
 *
 * @author Aigeth
 */


public abstract class Lobby {
    public abstract void commandHandler(String command, ServerConnection sc);
    public abstract boolean addConnection(ServerConnection sc);
    public abstract void removeConnection(ServerConnection sc);
}
