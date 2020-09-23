/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccountManager;

/*import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;*/
import java.util.ArrayList;
/*import java.util.logging.Level;
import java.util.logging.Logger;*/

/**
 *
 * @author Aigeth
 */
public class AccountManager {
    ArrayList<Account> accounts;
    
    public AccountManager() {
        accounts = new ArrayList<>();
        /*try {
            FileReader file = new FileReader("C:/Users/Aigeth/OneDrive/User_Accounts.txt");
            BufferedReader reader = new BufferedReader(file);

            String line = reader.readLine();
            while(line != null){
                line = reader.readLine();
                String[] account = line.split("\\S");;
                saveAccountsToMemory(Integer.parseInt(account[0]), account[1], account[3], account[4]);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    /*public void saveAccountsToMemory(int id, String name, String facebook, String url){
        accounts.add(new Account(id, name, Boolean.parseBoolean(facebook), url));
    }*/
    
    /*public Account authorize(int id, String name){
        for(Account account: accounts){
            if(account.getName().equals(name) && account.getId() == id){
                account.setAuthorized();
                return account;
            }
        }
        return null;
    }*/
    
    public ArrayList<Account> searchByName(String name){
        ArrayList<Account> tmpAccounts = new ArrayList<>();
        getAccounts().stream().filter((account) -> (name.contains(account.getFirstName()) || name.contains(account.getLastName()))).forEachOrdered((account) -> {
            tmpAccounts.add(account);
        });
        return tmpAccounts;
    }
    
    public Account getAccountById(int id){
        
        for(Account account: getAccounts()){
            if(account.getId() == id){
                return account;
            }
        }
        
        return null;
    }
    
    public synchronized ArrayList<Account> getAccounts(){
        return accounts;
    }
    
    public boolean addAccount(Account newAccount){
        for(Account account : getAccounts()){
            if(account.getFirstName().equals(newAccount.getFirstName()) &&  account.getLastName().equals(newAccount.getLastName())|| account.getId() == newAccount.getId())
                return false;
        }
        
        newAccount.setAuthorized();
        getAccounts().add(newAccount);
        /*try {
            File file = new File("C:/Users/Aigeth/OneDrive/User_Accounts.txt");
            PrintWriter output = new PrintWriter(file);
            output.println(tmpAccount.toString());
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        return true;
    }
    
    public void removeAccount(Account account){
        getAccounts().remove(account);
    }
    
    
}
