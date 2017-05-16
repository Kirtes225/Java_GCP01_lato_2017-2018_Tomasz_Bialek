package com.company.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server implements IServer {
    private static final int PORT = 1234;
    private List<ClientThread> threadList = new ArrayList<>();
    private static Server server;
    private boolean isRunning = false;
    private ServerSocket finalServerSocket;

    public static void main(String args[]){
        server = new Server();
        server.start();
    }

    public List<ClientThread> getThreadList(){
        return threadList;
    }

    @Override
    public void start(){
        ServerSocket serverSocket = null;
        final Socket[] socket = {null};

        try {
            serverSocket = new ServerSocket( PORT );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }

        finalServerSocket = serverSocket;
        new Thread(){
            public void run(){
                if( !isRunning ) {
                    isRunning = true;
                    while( isRunning ) {
                        try {
                            socket[0] = finalServerSocket.accept();
                        }
                        catch(IOException el) {
                            el.printStackTrace();
                            break;
                        }
                        ClientThread clientThread = new ClientThread(socket[0], server );
                        clientThread.setDaemon( true );
                        clientThread.start();
                        threadList.add(clientThread);
                    }
                }
            }
        }.start();
    }

    @Override
    public void stop(){
        new Thread(){
            public void run() {
                isRunning = false;

                for( ClientThread el: threadList )
                    el.close();

                try {
                    finalServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void sayAll(String user, String msg) throws IOException{
        SimpleDateFormat dateformat = new SimpleDateFormat( "HH:mm:ss" );
        for( ClientThread clientThread: threadList )
            clientThread.say( "<html><font size=\"3\">[" + dateformat.format( new Date() ) + "] " + user + "</font></html>" + msg );
    }

    public void removeThread(ClientThread clientThread) {
        threadList.remove(clientThread);
    }
}
