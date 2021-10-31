import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HiloServidor implements Runnable {
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;
	private List<Libros> catalogoLibros;
	
	//Pasamos por el constructor el ArrayList de libros para que pueda trabajar con el.
	public HiloServidor(Socket socketAlCliente, List<Libros> catalogoLibros) {
		numCliente++;
		hilo = new Thread(this, "Cliente: " + numCliente);
		this.socketAlCliente = socketAlCliente;
		hilo.start();
		this.catalogoLibros = catalogoLibros;
	}

	@Override
	public void run() {
		
		int contador = 0;
		
		System.out.println("Estableciendo comunicacion con: " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			salida = new PrintStream(socketAlCliente.getOutputStream());
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			
			String texto = "";
			boolean continuar = true;
			
			//Creamos un bucle para salir cuando lo indique el cliente
			while (continuar) {
				//Informamos de los numeros de libros que tenemos en el catalogo actualmente y leemos
				salida.println("Servidor: Actualmente tenemos: " + catalogoLibros.size() + " libros");
				texto = entradaBuffer.readLine();
				
				//En funcion de lo que nos haya mandado el cliente entraremos en una bloque o otro
				if (texto.trim().equalsIgnoreCase("5")) {
					salida.println("OK");
					//Mandamos un "OK" de confirmacion y cerramos el hilo
					System.out.println(hilo.getName() + " ha cerrado la comunicacion");
					continuar = false;
				} else if (texto.trim().equalsIgnoreCase("1")) {
					salida.println("1");
					texto = entradaBuffer.readLine();
					//Leemos el isbn que nos han mandado y con un bucle for recorremos el ArrayList buscandolo
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getIsbn())) {
							salida.println(muestra);
						}else {
							contador++;
						}
					}
					//Si el isbn no esta en el catalogo lo comunicamos
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					}
				}else if (texto.trim().equalsIgnoreCase("2")) {
					salida.println("2");
					texto = entradaBuffer.readLine();
					//Leemos el titulo que nos han mandado y con un bucle for recorremos el ArrayList buscandolo
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getTitulo())) {
							salida.println(muestra);
						}else {
							contador++;
						}
					}
					//Si el titulo no esta en el catalogo lo comunicamos
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					}
				}else if (texto.trim().equalsIgnoreCase("3")) {
					String concatenacion = "";
					salida.println("3");
					texto = entradaBuffer.readLine();
					//Leemos el autor que nos han mandado y con un bucle for recorremos el ArrayList buscandolo y lo vamos concatenando en una variable
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getAutor())) {
							concatenacion += muestra + "|";
						}else {
							contador++;
						}
					}
					//Si el autor no esta en el catalogo lo comunicamos
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					} 
					//Mandamos la concatenacion de los libros
					else {
						salida.println(concatenacion);
					}
				}else if (texto.trim().equalsIgnoreCase("4")) {
					salida.println("4");
					texto = entradaBuffer.readLine();
					//Recibimos el libro y como sabemos el formato usamos la funcion split para dividirlo y vamos creando el libro
					String[] libroNuevo = texto.split(",");
					Libros libro = new Libros();
					libro.setIsbn(libroNuevo[0]);
					libro.setTitulo(libroNuevo[1]);
					libro.setAutor(libroNuevo[2]);
					Integer precio = Integer.parseInt(libroNuevo[3]);
					libro.setPrecio(precio);
					//Agregamos el libro al ArrayList
					catalogoLibros.add(libro);
					salida.println(libro);
				}
			}  
			//Cerramos el hilo
			socketAlCliente.close();
		
		} catch (IOException e) {
			System.err.println("HiloServidor: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloServidor: error");
			e.printStackTrace();
		}
		
	}
	
	
}
