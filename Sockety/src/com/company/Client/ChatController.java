package com.company.Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class ChatController {
    @FXML
    private HTMLEditor fieldHTML;

    @FXML
    private WebView areaWebView = new WebView();

    private Client client;

    public void init(Client client) {
        this.client = client;

        this.client.setClientEventChat(new ClientEventChat() {
            @Override
            public void messageReceived( String msg ) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder scrollHtml = scrollWebView( 0, (int)areaWebView.getMaxHeight() );
                        areaWebView.getEngine().loadContent( scrollHtml + ( areaWebView.getEngine().executeScript("document.documentElement.outerHTML") + msg ) );
                    }
                });
            }

            @Override
            public void disconnectedFromTheServer() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        areaWebView.getEngine().loadContent( areaWebView.getEngine().executeScript("document.documentElement.outerHTML") + "<html><br/><font color=\"red\" size=\"2\"><b>DISCONNECTED FROM THE Server!</b></font><br/></html>" );
                    }
                });
            }
        } );
        this.client.listen();
    }



    public void sendMessage() {
        if( !client.socket.isClosed() ) {
            String message = fieldHTML.getHtmlText();
            if( !message.isEmpty() ) {
                client.sendToAll("<" + client.username + "> " + message);
            }
        }
        else {
            areaWebView.getEngine().loadContent(areaWebView.getEngine().executeScript("document.documentElement.outerHTML") + "<html><br/><font color=\"red\" size=\"2\"><b>YOU ARE NOT CONNECTED TO THE Server!</b></font><br/></html>\n");
        }
        fieldHTML.setHtmlText( "" );
    }

    public static StringBuilder scrollWebView(int xPos, int yPos) {
        StringBuilder script = new StringBuilder().append("<html>");
        script.append("<head>");
        script.append("   <script language=\"javascript\" type=\"text/javascript\">");
        script.append("       function toBottom(){");
        script.append("           window.scrollTo(" + xPos + ", " + yPos + ");");
        script.append("       }");
        script.append("   </script>");
        script.append("</head>");
        script.append("<body onload='toBottom()'>");
        return script;
    }

    public void disconnectFromServer() {
        client.disconnect();
    }
}
