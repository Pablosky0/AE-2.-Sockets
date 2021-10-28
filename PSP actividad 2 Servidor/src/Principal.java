import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Principal {

	public static final int PUERTO = 2017;

	public static void main(String[] args) {
	
		Biblioteca libreria = new Biblioteca();
		
		System.out.println("        SERVIDOR biblioteca        ");
		System.out.println("-----------------------------------");		

		
		try (ServerSocket servidor = new ServerSocket()){
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			servidor.bind(direccion);

			while (true) {
				Socket socketAlCliente = servidor.accept();

				new Servidor(socketAlCliente, libreria);
			}			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}
	
}
