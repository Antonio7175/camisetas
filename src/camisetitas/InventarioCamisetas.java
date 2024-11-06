package camisetitas;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InventarioCamisetas {

	public static void procesarArchivo(String inputFile, String outputFileCorrect, String outputFileErrors) {
		int totalLineas = 0;
		int lineasErroneas = 0;
		List<String> lineasIncorrectas = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
				BufferedWriter bwCorrect = new BufferedWriter(new FileWriter(outputFileCorrect));
				BufferedWriter bwErrors = new BufferedWriter(new FileWriter(outputFileErrors))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				totalLineas++;
				if (linea.chars().filter(ch -> ch == ',').count() == 5) {
					bwCorrect.write(linea);
					bwCorrect.newLine();
				} else {
					lineasErroneas++;
					lineasIncorrectas.add(linea);
				}
			}

			bwErrors.write("Total líneas analizadas: " + totalLineas + "\n");
			bwErrors.write("Total líneas eliminadas: " + lineasErroneas + "\n");
			bwErrors.write("Las líneas eliminadas son:\n");
			for (String incorrecta : lineasIncorrectas) {
				bwErrors.write(incorrecta + "\n");
			}

			System.out.println("Archivo procesado con éxito.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generarReporteFrecuencias(String inputFile, String outputFile) {
		Map<String, Integer> frecuenciaColor = new TreeMap<>();
		Map<String, Integer> frecuenciaMarca = new TreeMap<>();
		Map<String, Integer> frecuenciaModelo = new TreeMap<>();
		Map<String, Integer> frecuenciaTalla = new TreeMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] campos = linea.split(",");
				if (campos.length == 6) {
					frecuenciaColor.put(campos[2], frecuenciaColor.getOrDefault(campos[2], 0) + 1);
					frecuenciaMarca.put(campos[3], frecuenciaMarca.getOrDefault(campos[3], 0) + 1);
					frecuenciaModelo.put(campos[4], frecuenciaModelo.getOrDefault(campos[4], 0) + 1);
					frecuenciaTalla.put(campos[5], frecuenciaTalla.getOrDefault(campos[5], 0) + 1);
				}
			}

			bw.write("Frecuencia de colores:\n" + frecuenciaColor + "\n");
			bw.write("Frecuencia de marcas:\n" + frecuenciaMarca + "\n");
			bw.write("Frecuencia de modelos:\n" + frecuenciaModelo + "\n");
			bw.write("Frecuencia de tallas:\n" + frecuenciaTalla + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void crearArchivoFinal(String inputFile, String outputFile) {
	    try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
	         BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
	        
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            // Aquí puedo limpiar y transformar
	            bw.write(linea);
	            bw.newLine();
	        }

	        System.out.println("Archivo final generado con éxito.");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void generarSQL(String inputFile, String outputSQL) {
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputSQL))) {

			bw.write("CREATE TABLE IF NOT EXISTS camisetas (\n" + "  id INT AUTO_INCREMENT PRIMARY KEY,\n"
					+ "  cantidad INT,\n" + "  color VARCHAR(50),\n" + "  marca VARCHAR(50),\n"
					+ "  modelo VARCHAR(50),\n" + "  talla VARCHAR(10)\n" + ");\n\n");

			String linea;
			while ((linea = br.readLine()) != null) {
				String[] campos = linea.split(",");
				if (campos.length == 6) {
					String sql = String.format(
							"INSERT INTO camisetas (cantidad, color, marca, modelo, talla) "
									+ "VALUES (%s, '%s', '%s', '%s', '%s');\n",
							campos[1], campos[2], campos[3], campos[4], campos[5]);
					bw.write(sql);
				}
			}

			System.out.println("Archivo SQL generado con éxito.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String rutaArchivo = "src/camisetas.txt"; // Ruta del archivo, en este caso no es absoluta
		procesarArchivo(rutaArchivo, "camisetas_sin_errores_de_linea.txt", "camisetas_con_errores_de_linea.log");
		generarReporteFrecuencias("camisetas_sin_errores_de_linea.txt", "camisetas_frecuencias_antes.log");
		 // Creo el archivo final
	    crearArchivoFinal("camisetas_sin_errores_de_linea.txt", "camisetas_finales.txt");

	    // Genero el archivo SQL
	    generarSQL("camisetas_finales.txt", "camisetas.sql");
	}
}
