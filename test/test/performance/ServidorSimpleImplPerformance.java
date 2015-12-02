package test.performance;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import contenido.Cancion;
import contenido.Contenido;
import contenido.Emisora;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import servidor.Servidor;
import servidor.ServidorSimpleImp;
import test.generators.ContenidoDuracionGenerator;
import test.generators.GeneralNameGenerator;
import test.generators.ServidorTokenGenerator;
import util.Pair;

/**
 * Tests para la clase Servidor Simple Impl.
 */
public class ServidorSimpleImplPerformance {

	/** Generador de nombres válidos. */
	GeneralNameGenerator gNameGen = new GeneralNameGenerator();

	/** Generador de duraciones válidas. */
	ContenidoDuracionGenerator cDuracionGen = new ContenidoDuracionGenerator();

	/** Generador de duraciones válidas. */
	ServidorTokenGenerator sTokenGen = new ServidorTokenGenerator();

	/** Number of iteratios to check performance */
	private final Integer IT_NUMBER = 10000;

	/** JETM Monitor to check performance */
	private final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	/**
	 * Obtener nombre test.
	 */
	public void obtenerNombrePerformanceTest() {
		List<Servidor> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombre = gNameGen.next();
			Servidor s = new ServidorSimpleImp(nombre, null, "", "");
			testElements.add(s);
		}

		EtmPoint point = etmMonitor.createPoint("ServidorSimple:obtenerTitulo");

		for (Servidor s : testElements) {
			s.obtenerNombre();
		}

		point.collect();
	}

	/**
	 * Agregar test.
	 */
	@Test
	public void agregarPerformanceTest() {
		List<Pair<Emisora, Cancion>> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombreEmisora = gNameGen.next();
			Emisora emisora = new Emisora(nombreEmisora);

			String nombreCancion = gNameGen.next();
			Integer duracionCancion = cDuracionGen.next();
			Cancion cancion = new Cancion(nombreCancion, duracionCancion);

			testElements.add(new Pair<>(emisora, cancion));
		}

		EtmPoint point = etmMonitor.createPoint("ServidorSimple:agregar");

		for (Pair<Emisora, Cancion> pair : testElements) {
			pair.getLeft().agregar(pair.getRight(), pair.getLeft());
		}

		point.collect();
	}

	/**
	 * Eliminar test.
	 */
	@Test
	public void eliminarPerformanceTest() {
		List<Pair<Emisora, Cancion>> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombre = gNameGen.next();
			String passwd = sTokenGen.next().getLeft();
			String token = sTokenGen.next().getLeft();
			;
			Servidor s = new ServidorSimpleImp(nombre, null, passwd, token);
			String titulo = gNameGen.next();
			Integer duracion = cDuracionGen.next();
			Cancion cancion = new Cancion(titulo, duracion);
			s.agregar(cancion, passwd);
			Emisora emisora = new Emisora(nombre);
			emisora.agregar(cancion, emisora);
			testElements.add(new Pair<>(emisora, cancion));
		}

		EtmPoint point = etmMonitor.createPoint("ServidorSimple:eliminar");

		for (Pair<Emisora, Cancion> pair : testElements) {
			pair.getLeft().eliminar(pair.getRight());
		}

		point.collect();
	}

	/**
	 * Buscar token invalido test.
	 */
	@Test
	public void buscarTokenInvalidoPerformanceTest() {
		List<Pair<Servidor, Pair<String, String>>> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombre = gNameGen.next();
			String passwd = gNameGen.next();
			String tokenValido = sTokenGen.next().getLeft();

			Servidor servidor = new ServidorSimpleImp(nombre, null, passwd,
					tokenValido);

			String titulo = gNameGen.next();
			String titulo1 = gNameGen.next();
			String titulo2 = gNameGen.next();

			Integer duracion = cDuracionGen.next();
			Integer duracion1 = cDuracionGen.next();
			Integer duracion2 = cDuracionGen.next();

			Contenido cancion = new Cancion(titulo, duracion);
			Contenido cancion1 = new Cancion(titulo1, duracion1);
			Contenido cancion2 = new Cancion(titulo2, duracion2);

			servidor.agregar(cancion, passwd);
			servidor.agregar(cancion1, passwd);
			servidor.agregar(cancion2, passwd);
			servidor.agregar(cancion2, passwd);

			testElements.add(new Pair<>(servidor, new Pair<>(titulo,
					tokenValido)));
		}

		EtmPoint point = etmMonitor
				.createPoint("ServidorSimple:buscarTokenInvalido");

		for (Pair<Servidor, Pair<String, String>> pair : testElements) {
			pair.getLeft().buscar(pair.getRight().getLeft(),
					sTokenGen.next().getLeft());
		}

		point.collect();
	}

	/**
	 * Buscar con token válido test.
	 */
	@Test
	public void buscarTokenValidoPerformanceTest() {
		List<Pair<Servidor, Pair<String, String>>> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombre = gNameGen.next();
			String passwd = gNameGen.next();
			String tokenValido = sTokenGen.next().getLeft();

			Servidor servidor = new ServidorSimpleImp(nombre, null, passwd,
					tokenValido);

			String titulo = gNameGen.next();
			String titulo1 = gNameGen.next();
			String titulo2 = gNameGen.next();

			Integer duracion = cDuracionGen.next();
			Integer duracion1 = cDuracionGen.next();
			Integer duracion2 = cDuracionGen.next();

			Contenido cancion = new Cancion(titulo, duracion);
			Contenido cancion1 = new Cancion(titulo1, duracion1);
			Contenido cancion2 = new Cancion(titulo2, duracion2);

			servidor.agregar(cancion, passwd);
			servidor.agregar(cancion1, passwd);
			servidor.agregar(cancion2, passwd);
			servidor.agregar(cancion2, passwd);

			testElements.add(new Pair<>(servidor, new Pair<>(titulo,
					tokenValido)));
		}

		EtmPoint point = etmMonitor
				.createPoint("ServidorSimple:buscarTokenValido");

		for (Pair<Servidor, Pair<String, String>> pair : testElements) {
			pair.getLeft().buscar(pair.getRight().getLeft(),
					pair.getRight().getRight());
		}

		point.collect();
	}

	/**
	 * Buscar con token valido más de diez elementos test.
	 */
	@Test
	public void buscarTokenValidoMasDeDiezPerformanceTest() {
		List<Pair<Servidor, Pair<String, String>>> testElements = new ArrayList<>();
		for (int i = 0; i < IT_NUMBER; i++) {
			String nombre = gNameGen.next();
			String passwd = gNameGen.next();
			String tokenValido = sTokenGen.next().getLeft();

			Servidor servidor = new ServidorSimpleImp(nombre, null, passwd,
					tokenValido);

			String titulo = gNameGen.next();
			String titulo1 = gNameGen.next();
			String titulo2 = gNameGen.next();

			Integer duracion = cDuracionGen.next();
			Integer duracion1 = cDuracionGen.next();
			Integer duracion2 = cDuracionGen.next();

			Contenido cancion = new Cancion(titulo, duracion);
			Contenido cancion1 = new Cancion(titulo1, duracion1);
			Contenido cancion2 = new Cancion(titulo2, duracion2);

			for (int j = 0; j <= 15; j++) {
				servidor.agregar(cancion, passwd);
				servidor.agregar(cancion1, passwd);
				servidor.agregar(cancion2, passwd);
			}
			testElements.add(new Pair<>(servidor, new Pair<>(titulo,
					tokenValido)));
		}

		EtmPoint point = etmMonitor
				.createPoint("ServidorSimple:buscarTokenValidoMasDeDiezElementos");

		for (Pair<Servidor, Pair<String, String>> pair : testElements) {
			pair.getLeft().buscar(pair.getRight().getLeft(),
					pair.getRight().getRight());
		}

		point.collect();
	}

}