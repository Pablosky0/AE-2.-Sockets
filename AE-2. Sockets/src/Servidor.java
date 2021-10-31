import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Servidor {
	
	public static final int PUERTO = 2018;

	public static void main(String[] args) {
		//Creamos un ArrayList con los 5 libros que tendra por defecto la aplicacion
		Libros libro1 = new Libros("8466353771", "Dune", "Frank Herbert", 12);
		Libros libro2 = new Libros("8466357319", "El resplandor", "Stephen King", 13);
		Libros libro3 = new Libros("9788445000656", "El Hobbit", "J.R.R. Tolkien", 10);
		Libros libro4 = new Libros("8466657665", "El camino de los reyes", "Brandon Sanderson", 33);
		Libros libro5 = new Libros("978-8499083209", "Trilogía de la fundación", "Isaac Asimov", 15);

		List<Libros> catalogoLibros = new ArrayList<Libros>();
		catalogoLibros.add(libro1);
		catalogoLibros.add(libro2);
		catalogoLibros.add(libro3);
		catalogoLibros.add(libro4);
		catalogoLibros.add(libro5);
		
		System.out.println("Aplicacion Servidor");
		
		int peticion = 0;
		
		try (ServerSocket servidor = new ServerSocket()) {
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			servidor.bind(direccion);
			
			System.out.println("Servidor: Esperando la peticion por el puerto: " + PUERTO);
			
			//Creamos un bucle infinito para que nuestro servidor este siempre activo
			while (true) {
				//Aceptamos la peticion del cliente
				Socket socketAlCliente = servidor.accept();
				System.out.println("Servidor: Peticion numero: " + ++peticion + " recibida");
				//Creamos un nuevo hilo para poder estar siempre a disposicion de nuevos clientes.
				new HiloServidor(socketAlCliente, catalogoLibros);
			}
		} catch (IOException e) {
			System.err.println("Servidor: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Servidor: Errror");
			e.printStackTrace();
		}
	}
}
