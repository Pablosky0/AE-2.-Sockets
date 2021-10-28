import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Formato del mensaje al servidor:
 * 
 * 		accion:dato[::dato::dato::dato]
 * 
 * 		accion -- ISBN -> buscar libro po ISBN
 * 						ISBN:numeroISBN
 * 
 * 		accion -- titulo -> buscar libro por titulo
 * 						ISBN:numeroISBN
 * 
 * 		accion -- autor -> buscar libro por autor
 * 						ISBN:numeroISBN
 * 
 * 		accion -- libro -> crear libro
 * 						ISBN:numeroISBN::titulo::autor::precio
 */

/*
 * Formato del mensaje del servidor:
 * 
 * 		cuando no devuelve un libro:
 * 			mensaje
 * 		cuando devuelve un libro:
 * 			isbn::titulo::autor::precio
 * 		cuando devuelve varios libros:
 * 			isbn::titulo::autor::precio:::isbn::titulo::autor::precio
 */

public class Principal {

	private static BufferedReader oLector;
	public static final int PUERTO = 2017;
	public static final String IP_SERVER = "localhost"; 

	public static void main(String[] args) {
		
		boolean continuarBucle;
		String lectura;
		// Abrir el lector de teclado, utilizado BufferedReader porque Scanner da problemas con los números
		oLector = new BufferedReader(new InputStreamReader(System.in));
		
		// Bucle principal del programa
		continuarBucle = true;
		while (continuarBucle == true) {
			// mostramos el menu
			System.out.println("1.- Consultar libro por ISBN");
			System.out.println("2.- Consultar libro por titulo");
			System.out.println("3.- Consultar libro por autor");
			System.out.println("4.- Añadir libro");
			System.out.println("5.- Salir");

			// leemos el teclado
			lectura = "";
			try {
				lectura = introducirDato();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// llamamos a una función dependiendo de la opción elegida
			switch (lectura) {
			case "1":
				solicitarPorISBN();
				break;
			case "2":
				solicitarPorTitulo();
				break;
			case "3":
				solicitarPorAutor();
				break;
			case "4":
				crearLibro();
				break;
			case "5":
				continuarBucle = false;
				break;
			case "":
				System.out.println("No ha introducido una elección.");
				break;
			default:
				System.out.println("Ha introducido una opción no contemplada.");
			}
		}

		// cierra el lector de teclado
		try {
			oLector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		


	}

	// Captación entrada por teclado
	private static String introducirDato() {
		
		String resultado ="";

		try {
			resultado = oLector.readLine();
		} catch (IOException e) {
			resultado = "";
			e.printStackTrace();
		}		

		return resultado;
	}

	// Función que pide el ISBN y enviar una petición
	private static void solicitarPorISBN() {
		
		String dato;
		// Introdución del ISBN
		System.out.println("  solicitar libro por ISBN");
		System.out.println("----------------------------");
		System.out.println("Introducir ISBN:");				
		dato = introducirDato();
		
		System.out.println("solicitar ISBN " + dato);
		
		// Enviar petición al servidor
		envio_peticion("ISBN:" + dato);

	}

	// Función que pide el titulo y enviar una petición
	private static void solicitarPorTitulo() {
		
		String dato;
		
		// Introdución del titulo
		System.out.println("  solicitar libro por titulo");
		System.out.println("------------------------------");
		System.out.println("Introducir titulo:");				
		dato = introducirDato();
		
		System.out.println("solicitar titulo " + dato);

		// Enviar petición al servidor
		envio_peticion("titulo:" + dato);
		
	}

	// Función que pide el autor y enviar la petición
	private static void solicitarPorAutor() {
		
		String dato;
		
		// Introdución del autor
		System.out.println("  solicitar libro por autor");
		System.out.println("------------------------------");
		System.out.println("Introducir autor:");				
		dato = introducirDato();
		
		System.out.println("solicitar autor " + dato);

		// Enviar petición al servidor
		envio_peticion("autor:" + dato);
		
	}

	// Se pide los datos del libro y enviar la petición
	private static void crearLibro() {

		Libro dato = new Libro();
		String precio;
		float precioNumerico;
		
		System.out.println("  crear nuevo libro");
		System.out.println("---------------------");
		// petición de los datos del libro
		System.out.println("Introducir ISBN:");
		dato.setIsbn(introducirDato());		
		System.out.println("Introducir titulo:");
		dato.setTitulo(introducirDato());
		System.out.println("Introducir autor :");
		dato.setAutor(introducirDato());
		System.out.println("Introducir precio :");
		precio = introducirDato();
		precioNumerico = Float.parseFloat(precio);
		dato.setPrecio(precioNumerico);

		
		System.out.println("crear libro");

		// Enviar petición al servidor
		envio_peticion("libro:" + dato.toString(true));

	}

	// Función que se encarga de enviar y recibir mensajes del servidor
	public static void envio_peticion(String mensaje) {

		InetSocketAddress direccionServidor;
		Socket socketAlServidor;
		PrintStream salida;
		InputStreamReader entrada;
		BufferedReader lector;
		List<String> resultados = new ArrayList<String>(); 
		List<String> subResultados = new ArrayList<String>(); 
		String mensajeAMostrar = "";
		String resultado;
		Libro oLibro;

		System.out.println("        Respuesta del servidor         ");
		System.out.println("---------------------------------------");
		System.out.println(mensaje);
		System.out.println("\n\n");
		try {
			// Inicializar los objetos para comunicarse con el servidor
			direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
			socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			salida = new PrintStream(socketAlServidor.getOutputStream());
			entrada = new InputStreamReader(socketAlServidor.getInputStream());
			lector = new BufferedReader(entrada);
		
			// enviar el mensaje al servidor
			salida.println(mensaje);

			// recibir el mensaje del servidor 
			resultado = lector.readLine();
			
			// Dividir el mensaje recibido en varios libros por el separador de libros :::
			resultados =  Arrays.asList(resultado.split(":::"));
			
			 // Si se reciben varios libros se añade libro por libro.
			if (resultados.size() > 1) { 
				mensajeAMostrar = "";
				for (String sLibro : resultados) {
					subResultados = Arrays.asList(sLibro.split("::"));
					oLibro = new Libro(subResultados.get(0), subResultados.get(1), subResultados.get(2),
							Float.parseFloat(subResultados.get(3)));
					mensajeAMostrar += oLibro.toString(false) + "\n"; 
				}
			} else {
				/*
				 * Si se ha recibido solo un libro se divide en datos
				 * si solo tiene un dato es un mensaje de error
				 * si son varios es un libro y se muestra
				 * 
				 */
				if (resultados.size() == 1) {
					subResultados = Arrays.asList(resultados.get(0).split("::"));
					if (subResultados.size() == 1) { 
						mensajeAMostrar = subResultados.get(0);
					} else { 
						oLibro = new Libro(subResultados.get(0), subResultados.get(1), subResultados.get(2),
								Float.parseFloat(subResultados.get(3)));
						mensajeAMostrar += oLibro.toString(false) + "\n"; 
					}
					
				} else {
					
					// se ha recibido un mensaje incorrecto.
					mensajeAMostrar = "El formato de datos recibidos es incorrecto.";
				}
					
			}

			// Se muestra el mensaje
			System.out.println(mensajeAMostrar);
			System.out.println("\n\n");
			
			// Se cierra los elementos de la comunicación
			entrada.close();
			salida.close();
			socketAlServidor.close();
				
		}catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la dirección" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
		
	}




}
