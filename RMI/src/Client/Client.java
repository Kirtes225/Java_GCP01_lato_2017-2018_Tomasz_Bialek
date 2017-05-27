package Client;

import Crawler.StudentListener;
import FXML.CrawlerController;
import FXML.LoginFormController;
import FXML.NewUserFormController;
import Loggers.ConsoleLogger;
import Loggers.GUILogger;
import Loggers.Logger;
import RMI.InterfaceRMICrawlerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Client extends Application {

    public static CrawlerController crawlerController;
    public static boolean running = true;

    public static void main(String[] args) {

        Properties properties = new Properties();
        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            File file = new File("config.properties");
            properties.setProperty("user", "qwerty");
            properties.setProperty("password", passHash("qwerty"));

            outputStream = new FileOutputStream(file);

            properties.store(outputStream, "PROPERTIES");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        properties = new Properties();

        try {
            inputStream = new FileInputStream("config.properties");

            properties.load(inputStream);

            System.out.println("User: " + properties.getProperty("user"));
            System.out.println("Password: " + properties.getProperty("password"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        (new Thread((new Runnable() {
            @Override
            public void run() {
                Application.launch(Client.class, args);
            }
        }))).start();


        (new Thread(() -> {
            try {
                final Logger[] loggers = new Logger[]{
                        new ConsoleLogger(),
                        new GUILogger()
                };

                while(running){
                    TimeUnit.SECONDS.sleep(3);
                }

/*
                // konfiguracja
		        String host = "localhost";
		        int port = 5060;

		        // nawiazywanie polaczenia

		        String name = "rmi://" + port + "/PublicGameServerObject";

		        Registry registry = LocateRegistry.getRegistry( host, port ); // nawiazywanie polaczenia
		        GameServer gameServer = (GameServer) registry.lookup( name ); // pobranie zbindowanej na serwerze logiki gry
*/

                String host = "localhost";
                int port = 5060;

                String name = "rmi://" + port + "/RMICrawler";

                Registry registry = LocateRegistry.getRegistry(host, port);
                InterfaceRMICrawlerProxy interfaceRMICrawlerProxy = (InterfaceRMICrawlerProxy) registry.lookup(name);

                interfaceRMICrawlerProxy.studentAddedListener((StudentListener) Student -> {
                            for (Logger logger : loggers) logger.log("ADDED ", Student);
                        }
                );

                interfaceRMICrawlerProxy.studentRemovedListener((StudentListener) Student -> {
                            for (Logger logger : loggers) logger.log("REMOVED ", Student);
                        }
                );

                interfaceRMICrawlerProxy.run();

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        })).start();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        LoginFormController loginFormController;
        NewUserFormController newUserFormController;

        FXMLLoader loginFormLoader = new FXMLLoader(getClass().getResource("/FXML/loginForm.fxml"));
        FXMLLoader newUserFormLoader = new FXMLLoader(getClass().getResource("/FXML/newUserForm.fxml"));
        FXMLLoader crawlerLoader = new FXMLLoader(getClass().getResource("/FXML/crawler.fxml"));

        try {
            loginFormLoader.load();
            newUserFormLoader.load();
            crawlerLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent loginFormLoaderRoot = loginFormLoader.getRoot();
        Parent newUserFormLoaderRoot = newUserFormLoader.getRoot();
        Parent crawlerLoaderRoot = crawlerLoader.getRoot();

        loginFormController = loginFormLoader.getController();
        newUserFormController = newUserFormLoader.getController();
        crawlerController = crawlerLoader.getController();

        Scene scene = new Scene(loginFormLoaderRoot);
        Scene scene2 = new Scene(newUserFormLoaderRoot);
        Scene scene3 = new Scene(crawlerLoaderRoot, 800, 600);

        crawlerController.setStage(primaryStage);
        crawlerController.setScene(scene);

        loginFormController.setStage(primaryStage);
        loginFormController.setSceneCrawler(scene3);
        loginFormController.setSceneNewUser(scene2);

        newUserFormController.setStage(primaryStage);
        newUserFormController.setScene(scene);

        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String passHash(String password) {
        String tmp = new String();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] bytes = md5.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            tmp = stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmp;
    }
}
