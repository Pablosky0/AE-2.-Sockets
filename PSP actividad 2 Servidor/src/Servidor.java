import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * Formato del mensaje del cliente:
 * 		accion:dato[::dato::dato::dato]
 * 		accion -- ISBN -> buscar libro po ISBN
 * 						ISBN:numeroISBN
 * 		accion -- titulo -> buscar libro por titulo
 * 						ISBN:numeroISBN
 * 		accion -- autor -> buscar libro por autor
 * 						ISBN:numeroISBN
 * 		accion -- libro -> crear libro
 * 						ISBN:numeroISBN::titulo::autor::precio
 */

/*
 * Formato del mensaje al cliente - contruido en la clase Libro con toString(true)
 * 
 * 		cuando no devuelve un libro:
 * 			mensaje
 * 		cuando devuelve un libro:
 * 			isbn::titulo::autor::precio
 * 		cuando devuelve varios libros:
 * 			isbn::titulo::autor::precio:::isbn::titulo::autor::precio
 */
public class Servidor extends Thread {

	// Propiedades del hilo
	
	// Conexión heredada del hilo principal de la aplicación
	private Socket conectorServidor;
	// Contenedor que tiene los 5 libros inicialesy las funciones de acceso directos a estos.
	Biblioteca libreria;
	
	
	// Constructor donde nos indican que conector y biblioteca utilizar e inicializa la ejecución
	public Servidor(Socket conectorAlCliente,Biblioteca pLibreria) {
		this.conectorServidor = conectorAlCliente;
		this.libreria = pLibreria;
		this.start();
	}


	// Metodo que se ejecuta el tratamiento de la petición del cliente
	@Override
	public void run() {
		// Declaración de la variables
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		String texto = "";
		String texto_Accion = "";
		String texto_Datos = "";
		String resultado = "";
		int i;
		Libro oLibro = null;
		ArrayList<Libro> oLibros = new ArrayList<Libro>(); 
		
		System.out.println("procesando.");
		try {
			// Establecemos el canal de salida al cliente
			salida = new PrintStream(conectorServidor.getOutputStream());

			// Establecemos el canal de entrada del cliente
			entrada = new InputStreamReader(conectorServidor.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			
			/*
			 *  Leemos el mesaje del cliente
			 *  tiene el formato :
			 *  	acción:datos
			 *  acción indica el tipo de acción que el cliente pide
			 *  datos propociona el dato o datos necesarios para procesar la acción
			 *  sin son varios estarán separados por dos dobles puntos (::)
			 *  
			 */
			
			texto = entradaBuffer.readLine();
			texto_Accion = texto.substring(0,texto.indexOf(":"));
			texto_Datos = texto.replaceFirst(texto_Accion + ":", "");
			
			
			switch (texto_Accion) {
				// Accion buscar por ISBN
				// si la libreria nos devuelve un null le mandamos un mensaje al cliente
				// si nos devuelve un libro le devolvemos su string para redes
				case "ISBN":
					oLibro = new Libro(); 
					oLibro = libreria.buscarLibroISBN(texto_Datos);
					if (oLibro == null) {
						resultado = "El libro con ISBN " + texto_Datos + " no se ha encontrado.";
					} else {
						resultado = oLibro.toString(true);
					}
					break;
				// Accion buscar por titulo
				// si la libreria nos devuelve un null le mandamos un mensaje al cliente
				// si nos devuelve un libro le devolvemos su string para redes
				case "titulo":
					oLibro = new Libro(); 
					oLibro = libreria.buscarLibroTitulo(texto_Datos);
					if (oLibro == null) {
						resultado = "El libro con titulo " + texto_Datos + " no se ha encontrado.";
					} else {
						resultado = oLibro.toString(true);
					}
					break;
				// Accion buscar por autor
				// si la libreria nos devuelve una lista vacia le mandamos un mensaje al cliente
				// si nos devuelve un libro le devolvemos un string con string de libros para redes
				case "autor": 
					oLibros = libreria.buscarLibroAutor(texto_Datos);
					if (oLibros.size() == 0) { // lista vacia
						resultado="No se ha encontrado ningún libro de " + texto_Datos;
					} else { 
						// convertimos los libros de la lista a texto separados por :::
						resultado = "";
						for (i = 0;i < oLibros.size(); i++) {
							resultado += oLibros.get(i).toString(true);
							if (i < oLibros.size()) {
								resultado += ":::";
							}
						}
					}
					break;
				// Accion crear libro
				// Creamos un nuevo libro con los datos recibidos y le devolvemos un mensaje de exito.
				case "libro":
					List<String> datos = Arrays.asList(texto_Datos.split("::"));
					oLibro = new Libro();
					oLibro.setIsbn(datos.get(0));
					oLibro.setTitulo(datos.get(1));
					oLibro.setAutor(datos.get(2));
					oLibro.setPrecio(Float.valueOf(datos.get(3)));
					libreria.agregarLibro(oLibro);
					resultado = "Libro agregado con exito";
					break;
				default:
			}

			// mandamos el mensaje contruido y cerramos el conector
			salida.println(resultado);
			
			conectorServidor.close();
		} catch (IOException e) {
			System.err.println("Error de en la respuesta a" + texto);
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error general en la respuesta a" + texto);
			e.printStackTrace();
		}
		System.out.println("procesado.");

	}

	
	
}
