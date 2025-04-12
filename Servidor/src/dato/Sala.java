package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Sala {

    private static final Object bloqueo = new Object();

    public String registrarSala(int filas, int columnas, int capMaxima) {
        String resultado;
        int nr = 1;

        synchronized (bloqueo) {

            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Salas.txt"))) {
                String linea = bf.readLine();

                while (linea != null) {
                    nr = Integer.parseInt(linea.split(",")[0]) + 1;
                    linea = bf.readLine();
                }

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Salas.txt", true))) {

                bw.write(nr + "," + filas + "," + columnas + "," + capMaxima + "\n");
                resultado = "1";

            } catch (IOException e) {
                resultado = "0";
                System.out.println(e);
            }

        }

        return resultado;
    }

    public String consultarSalas() {
        String salas = "";

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Salas.txt"))) {

            String linea, contenido = "";
            while ((linea = br.readLine()) != null) {
                contenido += linea + "@";
            }

            salas = contenido.substring(0, contenido.length() - 1);

        } catch (IOException e) {
        }

        return salas;
    }

    public static int[] consultarSala(String nro_sala) {
        int[] sala = new int[2];

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Salas.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                    String[] atributos = linea.split(",");
                    if (atributos[0].equals(nro_sala)) {
                        sala[0] = Integer.parseInt(atributos[1]);
                        sala[1] = Integer.parseInt(atributos[2]);
                    }
                }
            } catch (IOException e) {
            }

        }
        System.out.println("Fila: "+sala[0]+ " Columna: "+sala[1]);
        return sala;
    }

    public String eliminarSala(int nroSala) {
        String resultado;
        String contenido = "";

        synchronized (bloqueo) {

            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Salas.txt"))) {
                String linea = bf.readLine();

                while (linea != null) {
                    if (nroSala != Integer.parseInt(linea.split(",")[0])) {
                        contenido += linea + "\n";
                    }
                    linea = bf.readLine();
                }
            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Salas.txt"))) {

                bw.write(contenido);
                resultado = "1";

            } catch (IOException e) {
                resultado = "0";
            }

            if (resultado.equals("1")) {
                Funcion funcion = new Funcion();
                funcion.vaciarFuncionesSala(nroSala);
            }

        }

        return resultado;
    }

    public String modificarSala(int nroSala, int filas, int columnas, int capMaxima) {
        String resultado = "0";
        String contenido = "";

        Funcion funcion = new Funcion();

        synchronized (bloqueo) {

            if (!funcion.buscarFunciones(nroSala)) { // Si no tiene funciones asignadas
                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Salas.txt"))) {
                    String linea = bf.readLine();

                    while (linea != null) {
                        if (nroSala != Integer.parseInt(linea.split(",")[0])) {
                            contenido += linea + "\n";
                        } else {
                            contenido += nroSala + "," + filas + "," + columnas + "," + capMaxima + "\n";
                        }
                        linea = bf.readLine();
                    }

                } catch (IOException e) {
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Salas.txt"))) {

                    bw.write(contenido);
                    resultado = "1";

                } catch (IOException e) {
                    resultado = "0";
                }
            }
        }

        return resultado;
    }
}
