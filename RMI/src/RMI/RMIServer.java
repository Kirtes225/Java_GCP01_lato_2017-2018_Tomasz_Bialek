package RMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        int port = 5060;

        String name = "rmi://" + port + "/RMICrawler";
        Registry registry = LocateRegistry.createRegistry(port);
        RMICrawlerProxy rmiCrawlerProxy = new RMICrawlerProxy();
        registry.bind(name, rmiCrawlerProxy);
        System.out.println("Server is running");
    }

    /*
    public static void main( String[] args ) throws NamingException, RemoteException, InterruptedException, AlreadyBoundException
	{
		// konfiguracja
		int port = 5060;

		// uruchamianie serwera

		String name = "rmi://" + port + "/PublicGameServerObject";
		Registry registry = LocateRegistry.createRegistry( port ); // uruchomienie rejestru dla rmi na wybranym porcie

		try
		{
			PublicGameServer gameServer = new PublicGameServer(); // tworzenie obiektu gry na serwerze
			registry.bind( name, gameServer ); // bindowanie obiektu gry

			//TODO: bindowanie wiekszej ilosci obiektow

			// przykladowa logika 'podtrzymujaca' aplikacje

			System.out.println( "Type 'exit' to exit server." );
			Scanner scanner = new Scanner( System.in );

			while ( true )
			{
				if ( scanner.hasNextLine() )
				{
					if ( "exit".equals( scanner.nextLine() ) )
						break;
				}
			}

			scanner.close();
		}
		finally
		{
			UnicastRemoteObject.unexportObject( registry, true ); // zwalnianie rejestru
			System.out.println( "Server stopped." ); // komunikat zakonczenia
		}
	}
     */
}
