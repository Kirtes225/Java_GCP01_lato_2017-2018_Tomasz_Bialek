package com.company.Client;

import javafx.event.ActionEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements IClient {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public String username, password;
    public Socket socket;

    public boolean isRunning = false;
    private Thread thread;

    private ClientEventLogin clientEventLogin;
    private ClientEventChat clientEventChat;

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setClientEventLogin(ClientEventLogin clientEventLogin) {
        this.clientEventLogin = clientEventLogin;
    }

    public void setClientEventChat(ClientEventChat clientEventChat ) {
        this.clientEventChat = clientEventChat;
    }

    public void listen() {
        thread = new Thread(){
            public void run() {
                if( !isRunning ) {
                    isRunning = true;
                    sendToAll( "<html><font color=\"green   \" size=\"2\"><b> has joined to conversation\n.</b></font><br/></html>" );
                    showStats();

                    while( isRunning ) {
                        try {
                            String msg = dataInputStream.readUTF();
                            clientEventChat.messageReceived( msg );
                        }
                        catch( IOException e ) {
                            clientEventChat.disconnectedFromTheServer();
                            disconnect();
                            isRunning = false;
                        }
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void sendToAll( String msg ) {
        try {
            dataOutputStream.writeInt( MessageType.MESSAGE );
            dataOutputStream.writeUTF( msg );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean connect(String ip, int port) {
        boolean result;
        try
        {
            socket = new Socket( ip, port );
            dataOutputStream = new DataOutputStream( socket.getOutputStream() );
            dataInputStream = new DataInputStream( socket.getInputStream() );
            result = true;
        }
        catch( IOException e )
        {
            clientEventLogin.cannotConnectLogin();
            clientEventLogin.cannotConnectRegister();
            result = false;
        }
        return result;
    }

    @Override
    public void disconnect() {
        if(!socket.isClosed()) {
            try {
                dataOutputStream.writeInt(MessageType.LOGOUT);
                socket.close();
                dataOutputStream.close();
                dataInputStream.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void login( ActionEvent ev ) {
        try {
            dataOutputStream.writeInt( MessageType.LOGIN );
            dataOutputStream.writeUTF( username );
            dataOutputStream.writeUTF( password );
            if( dataInputStream.readBoolean() )
                clientEventLogin.successfulLogin( ev );
            else
                clientEventLogin.invalidLogin();
        }
        catch( IOException e ) {e.printStackTrace();}
    }

    public void register() {
        try {
            dataOutputStream.writeInt( MessageType.REGISTER );
            dataOutputStream.writeUTF( username );
            dataOutputStream.writeUTF( password );
            if( dataInputStream.readBoolean() )
                clientEventLogin.newAccountCreated();
            else
                clientEventLogin.usernameUsed();
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public void showStats() {
        try{
            dataOutputStream.writeInt(MessageType.STATISTICS);
        } catch (IOException e){e.printStackTrace();}
    }
}
