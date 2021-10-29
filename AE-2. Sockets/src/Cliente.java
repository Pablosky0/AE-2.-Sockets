import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	
	public static final int PUERTO = 2018;
	public static final String IP_SERVER = "localhost";
	
	
	public static void main(String[] args) {
		
		System.out.println("Aplicacion cliente");
		
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
		
		try (Scanner leer = new Scanner(System.in)) {
			System.out.println("Cliente: Esperando a que el servidor acepte la conexion");
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			System.out.println("Cliente: Conexion establecida a " + IP_SERVER + " por el puerto: " + PUERTO);
			
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBuffer = new BufferedReader(entrada);
			
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			
			String texto = "";
			boolean continuar = true;
			do {
				String respuesta = entradaBuffer.readLine();
				System.out.println(respuesta);
				System.out.println("Cliente: Elige la opcion de lo que quieras  hacer: \n 1) Consultar libro por ISBN \n 2) Consultar libro por titulo \n 3) Consultar libros por autor \n 4) Añadir libro \n 5) Salir");
				texto = leer.nextLine();
				
				
				salida.println(texto);
				System.out.println("CLiente: esperando respuesta");
				respuesta = entradaBuffer.readLine();
				
				if ("OK".equalsIgnoreCase(respuesta)) {
					continuar = false;
				}else if (respuesta.equalsIgnoreCase("1")){
					System.out.println("Cliente: Di el isbn del libro: ");
					texto = leer.nextLine();
					salida.println(texto); 
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}else if (respuesta.equalsIgnoreCase("2")){
					System.out.println("Cliente: Di el titulo del libro: ");
					texto = leer.nextLine();
					salida.println(texto); 
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}else if (respuesta.equalsIgnoreCase("3")){
					System.out.println("Cliente: Di el autor del libro: ");
					texto = leer.nextLine();
					salida.println(texto); 
					respuesta = entradaBuffer.readLine();
					String[] autores = respuesta.split("\\|");
					for (int i = 0; i < autores.length; i++) {
						String autor = autores[i];
						System.out.println(autor);
					}
					
				}else if (respuesta.equalsIgnoreCase("4")){
					System.out.println("Cliente: Di los datos del libro que quieres añadir en este orden y formato: \n isbn,titulo,autor,precio");
					texto = leer.nextLine();
					salida.println(texto); 
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}
				
			} while (continuar);
			
			socketAlServidor.close();
			
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
		
		System.out.println("Cliente: Fin del programa");

	}

}
