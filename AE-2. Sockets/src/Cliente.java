import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	//Declaramos constantes
	public static final int PUERTO = 2018;
	public static final String IP_SERVER = "localhost";
	
	
	public static void main(String[] args) {
		
		System.out.println("Aplicacion cliente");
		
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
		
		try (Scanner leer = new Scanner(System.in)) {
			System.out.println("Cliente: Esperando a que el servidor acepte la conexion");
			//Creamos un nuevo Socket
			Socket socketAlServidor = new Socket();
			//Le pasamos la direccion del servidor.
			socketAlServidor.connect(direccionServidor);
			System.out.println("Cliente: Conexion establecida a " + IP_SERVER + " por el puerto: " + PUERTO);
			
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBuffer = new BufferedReader(entrada);
			
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			
			String texto = "";
			boolean continuar = true;
			//Creamos un bucle del que no saldremos hasta que lo indiquemos
			do {
				String respuesta = entradaBuffer.readLine();
				System.out.println(respuesta);
				//Pedimos que nos den una opcion del menu y lo leemos
				System.out.println("Cliente: Elige la opcion de lo que quieras  hacer: \n 1) Consultar libro por ISBN \n 2) Consultar libro por titulo \n 3) Consultar libros por autor \n 4) Añadir libro \n 5) Salir");
				texto = leer.nextLine();
				salida.println(texto);
				System.out.println("CLiente: esperando respuesta");
				respuesta = entradaBuffer.readLine();
				
				//Comprobamos la respuesta del servidor y dependiendo de la respuesta cerraremos el programa o haremos lo indicado
				if ("OK".equalsIgnoreCase(respuesta)) {
					continuar = false;
				}else if (respuesta.equalsIgnoreCase("1")){
					System.out.println("Cliente: Di el isbn del libro: ");
					//Mandamos el isbn del libro que queremos consultar
					texto = leer.nextLine();
					salida.println(texto); 
					//Leemos la respueta del servidor
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}else if (respuesta.equalsIgnoreCase("2")){
					System.out.println("Cliente: Di el titulo del libro: ");
					//Mandamos el titulo del libro que queremos consultar
					texto = leer.nextLine();
					salida.println(texto); 
					//Leemos la respueta del servidor
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}else if (respuesta.equalsIgnoreCase("3")){
					System.out.println("Cliente: Di el autor del libro: ");
					//Mandamos el autor del libro que queremos consultar
					texto = leer.nextLine();
					salida.println(texto); 
					//Leemos la respueta del servidor
					respuesta = entradaBuffer.readLine();
					//Como la respuesta nos puede dar varios libros usamos la funcion split para separalo y con un for lo mostramos por pantalla
					String[] autores = respuesta.split("\\|");
					for (int i = 0; i < autores.length; i++) {
						String autor = autores[i];
						System.out.println(autor);
					}
					
				}else if (respuesta.equalsIgnoreCase("4")){
					System.out.println("Cliente: Di los datos del libro que quieres añadir en este orden y formato: \n isbn,titulo,autor,precio");
					//Mandamos los datos del libro a añadir en el formato que se nos pide.
					texto = leer.nextLine();
					salida.println(texto); 
					//Leemos la respueta del servidor
					respuesta = entradaBuffer.readLine();
					System.out.println(respuesta);
				}
				
			} while (continuar);
			//Cerramos el servidor
			socketAlServidor.close();
			
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
		
		System.out.println("Cliente: Fin del programa");

	}

}
