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
			
			while (continuar) {
				salida.println("Servidor: Actualmente tenemos: " + catalogoLibros.size() + " libros");
				texto = entradaBuffer.readLine();
				
				if (texto.trim().equalsIgnoreCase("5")) {
					salida.println("OK");
					System.out.println(hilo.getName() + " ha cerrado la comunicacion");
					continuar = false;
				} else if (texto.trim().equalsIgnoreCase("1")) {
					salida.println("1");
					texto = entradaBuffer.readLine();
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getIsbn())) {
							salida.println(muestra);
						}else {
							contador++;
						}
					}
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					}
				}else if (texto.trim().equalsIgnoreCase("2")) {
					salida.println("2");
					texto = entradaBuffer.readLine();
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getTitulo())) {
							salida.println(muestra);
						}else {
							contador++;
						}
					}
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					}
				}else if (texto.trim().equalsIgnoreCase("3")) {
					String concatenacion = "";
					salida.println("3");
					texto = entradaBuffer.readLine();
					for (int i = 0; i < catalogoLibros.size(); i++) {
						Libros muestra = catalogoLibros.get(i);
						if (texto.equalsIgnoreCase(muestra.getAutor())) {
							concatenacion += muestra + "|";
						}else {
							contador++;
						}
					}
					if (contador == catalogoLibros.size()) {
						salida.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
					} else {
						salida.println(concatenacion);
					}
				}else if (texto.trim().equalsIgnoreCase("4")) {
					salida.println("4");
					texto = entradaBuffer.readLine();
					String[] libroNuevo = texto.split(",");
					Libros libro = new Libros();
					libro.setIsbn(libroNuevo[0]);
					libro.setTitulo(libroNuevo[1]);
					libro.setAutor(libroNuevo[2]);
					Integer precio = Integer.parseInt(libroNuevo[3]);
					libro.setPrecio(precio);
					catalogoLibros.add(libro);
					salida.println(libro);
				}
			}  
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
