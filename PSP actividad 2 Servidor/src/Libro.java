
public class Libro {
	
	private String isbn;
	private String titulo;
	private String autor;
	private Float precio;

	/*
	 * Constructores
	 */
	public Libro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Libro(String isbn, String titulo, String autor, Float precio) {
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.precio = precio;
	}


	/*
	 * Acceso a las propiedades
	 */


	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Float getPrecio() {
		return precio;
	}

	public void setPrecio(Float precio) {
		this.precio = precio;
	}

	
	public String toString(boolean paraRed) {
		String resultado;
		if (paraRed == true) {
			resultado = isbn + "::";
			resultado += titulo + "::"; 
			resultado += autor + "::";
			resultado += precio;
		} else {
			resultado =  "Libro = isbn : " + isbn + "\n";
			resultado += "        titulo : " + titulo + "\n"; 
			resultado += "        autor : " + autor + "\n";
			resultado += "        precio : " + precio + "\n";
		}
		return resultado;
	}
	
	
}
