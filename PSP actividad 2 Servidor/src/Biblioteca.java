import java.util.ArrayList;

public class Biblioteca {
	private ArrayList<Libro> lista;
	
	public Biblioteca() {
		// inicializamos la biblioteca con 5 ejemplares
		lista = new ArrayList<Libro>();
		lista.add(new Libro("01234567890123", "neuromante", "William Gibson", 24.5f));
		lista.add(new Libro("54687821645489", "solaris", "Stanislaw Lem", 30.2f));
		lista.add(new Libro("00356899877789", "conde cero", "William Gibson", 27.6f));
		lista.add(new Libro("11122233344455", "Ciberíada", "Stanislaw Lem", 15.2f));
		lista.add(new Libro("66677788899900", "Cismatrix", "Bruce Sterling", 42.0f));
	}

	// Se busca un libro por ISBN, si no se encuentra se devuelve un null
	public synchronized Libro buscarLibroISBN(String ISBN) {
		Libro libroEncontrado = null;
		
		for (Libro oLibro : lista) {
			if (oLibro.getIsbn().contains(ISBN) == true) {
				libroEncontrado = oLibro;
			}
		}
		
		return libroEncontrado;		
	}
	
	// Se busca un libro por titulo, si no se encuentra se devuelve un null
	public synchronized Libro buscarLibroTitulo(String titulo) {
		Libro libroEncontrado = null;
		
		for (Libro oLibro : lista) {
			if (oLibro.getTitulo().contains(titulo) == true) {
				libroEncontrado = oLibro;
			}
		}
		
		return libroEncontrado;		
	}
	
	// Se busca un libro por autor, si no se encuentra se devuelve una lista vacia
	public synchronized ArrayList<Libro> buscarLibroAutor(String autor) {
		ArrayList<Libro> librosEncontrados = new ArrayList<Libro>();
		
		for (Libro oLibro : lista) {
			if (oLibro.getAutor().contains(autor) == true) {
				librosEncontrados.add(oLibro);
			}
		}
		
		return librosEncontrados;		
	}
	
	public synchronized void agregarLibro(Libro oLibro) {

		lista.add(oLibro);		
		
	}
	


}
