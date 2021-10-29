import java.util.ArrayList;
import java.util.List;

public class prueba {

	public static void main(String[] args) {
		Libros libro1 = new Libros("8466353771", "Dune", "Frank Herbert", 12);
		Libros libro2 = new Libros("8466357319", "El resplandor", "Stephen King", 13);
		Libros libro3 = new Libros("9788445000656", "El Hobbit", "J.R.R. Tolkien", 10);
		Libros libro4 = new Libros("8466657665", "El camino de los reyes", "Brandon Sanderson", 33);
		Libros libro5 = new Libros("978-8499083209", "Trilogía de la fundación", "Isaac Asimov", 15);
		Libros libro6 = new Libros("978-8499083209", "patata", "Isaac Asimov", 15);
		Libros libro7 = new Libros("978-8499083209", "pes", "Isaac Asimov", 15);
		Libros libro8 = new Libros("978-8499083209", "culo", "Isaac Asimov", 15);
		
		List<Libros> catalogoLibros = new ArrayList<Libros>();
		catalogoLibros.add(libro1);
		catalogoLibros.add(libro2);
		catalogoLibros.add(libro3);
		catalogoLibros.add(libro4);
		catalogoLibros.add(libro5);
		catalogoLibros.add(libro6);
		catalogoLibros.add(libro7);
		catalogoLibros.add(libro8);
		
		String concatenacion = "";
		int contador = 0;
		for (int i = 0; i < catalogoLibros.size(); i++) {
			Libros muestra = catalogoLibros.get(i);
			if ("isaac asimov".equalsIgnoreCase(muestra.getAutor())) {
				concatenacion += muestra + "|";
			}else {
				contador++;
			}
		}
		if (contador == catalogoLibros.size()) {
			System.out.println("No tenemos ese libro si quiere añadirlo pulse 4 en el menu");
		} else {
			String[] autores = concatenacion.split("\\|");
			for (int i = 0; i < autores.length; i++) {
				String autor = autores[i];
				System.out.println(autor);
			}
		}
		
		

	}

}
