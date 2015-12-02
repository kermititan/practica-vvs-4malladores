package test.junit;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import contenido.Cancion;
import contenido.Contenido;
import servidor.Servidor;
import servidor.ServidorSimpleImp;
import test.generators.ContenidoDuracionGenerator;
import test.generators.GeneralNameGenerator;
import test.generators.ServidorTokenGenerator;

/**
 * Tests para la clase Servidor Simple Impl.
 */
public class ServidorSimpleImplTest {

	/** Generador de nombres válidos. */
	GeneralNameGenerator gNameGen = new GeneralNameGenerator();

	/** Generador de duraciones válidas. */
	ContenidoDuracionGenerator cDuracionGen = new ContenidoDuracionGenerator();

	/** Generador de tokens válidos. */
	ServidorTokenGenerator sTokenGen = new ServidorTokenGenerator();

	/**
	 * Obtener nombre test.
	 */
	@Test
	public void obtenerNombreTest() {
		String nombre = gNameGen.next();
		Servidor s = new ServidorSimpleImp(nombre, null, "", "");
		assertEquals(s.obtenerNombre(), nombre);
	}

	/**
	 * Agregar test.
	 */
	@Test
	public void agregarTest() {
		String nombre = gNameGen.next();
		String passwd = sTokenGen.next().getLeft();
		String token = sTokenGen.next().getLeft();
		Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);

		String titulo = gNameGen.next();
		Integer duracion = cDuracionGen.next();

		Contenido cancion = new Cancion(titulo, duracion);

		s.agregar(cancion, passwd);

		List<Contenido> result = s.buscar(titulo, token);
		assertEquals(result.get(0), cancion);
	}

	/**
	 * Eliminar test.
	 */
	@Test
	public void eliminarTest() {
		String nombre = gNameGen.next();
		String passwd = sTokenGen.next().getLeft();
		String token = sTokenGen.next().getLeft();
		Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);

		String titulo = gNameGen.next();
		Integer duracion = cDuracionGen.next();

		Contenido cancion = new Cancion(titulo, duracion);

		s.agregar(cancion, passwd);

		List<Contenido> resultAntes = s.buscar(titulo, token);

		s.eliminar(cancion, passwd);

		List<Contenido> resultDespois = s.buscar(titulo, token);

		assertEquals(resultAntes.get(0), cancion);
		assertEquals(resultDespois.size(), 0);
	}

	/**
	 * Buscar token invalido test.
	 */
	@Test
	public void buscarTokenInvalidoTest() {
		String nombre = gNameGen.next();
		String passwd = sTokenGen.next().getLeft();
		String token = sTokenGen.next().getLeft();
		Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);

		String titulo = gNameGen.next();
		Integer duracion = cDuracionGen.next();

		Contenido cancion = new Cancion(titulo, duracion);

		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);

		List<Contenido> result = s.buscar(titulo, sTokenGen.next().getLeft());
		int i = 0;
		assertEquals(result.get(i++).obtenerTitulo(), "PUBLICIDAD");
		assertEquals(result.get(i++).obtenerTitulo(), titulo);
		assertEquals(result.get(i++).obtenerTitulo(), titulo);
		assertEquals(result.get(i++).obtenerTitulo(), titulo);
		assertEquals(result.get(i++).obtenerTitulo(), "PUBLICIDAD");
	}

	/**
	 * Buscar con token válido test.
	 */
	@Test
	public void buscarTokenValidoTest() {
		String nombre = gNameGen.next();
		String passwd = sTokenGen.next().getLeft();
		String token = sTokenGen.next().getLeft();
		Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);

		String titulo = gNameGen.next();
		Integer duracion = cDuracionGen.next();

		Contenido cancion = new Cancion(titulo, duracion);

		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		s.agregar(cancion, passwd);
		List<Contenido> result = s.buscar(titulo, token);
		int i = 0;
		assertEquals(titulo, result.get(i++).obtenerTitulo());
		assertEquals(titulo, result.get(i++).obtenerTitulo());
		assertEquals(titulo, result.get(i++).obtenerTitulo());
	}

	/**
	 * Buscar con token valido más de diez elementos test.
	 */
	@Test
	public void buscarTokenValidoMasDeDiezTest() {
		String nombre = gNameGen.next();
		String passwd = sTokenGen.next().getLeft();
		String token = sTokenGen.next().getLeft();
		Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);

		String titulo = gNameGen.next();
		Integer duracion = cDuracionGen.next();

		Contenido cancion = new Cancion(titulo, duracion);

		for (int j = 0; j <= 15; j++)
			s.agregar(cancion, passwd);
		List<Contenido> result = s.buscar(titulo, token);
		int i;
		for (i = 0; i < 10; i++)
			assertEquals(titulo, result.get(i).obtenerTitulo());

		assertEquals("PUBLICIDAD", result.get(i++).obtenerTitulo());
		assertEquals(titulo, result.get(i++).obtenerTitulo());
		assertEquals(titulo, result.get(i++).obtenerTitulo());
		assertEquals(titulo, result.get(i++).obtenerTitulo());
		assertEquals("PUBLICIDAD", result.get(i).obtenerTitulo());
	}

}