package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Parametro {

    private final static Object bloqueo = new Object();

    public int sesionesMaximasServidor() {
        int resultado = 0;
        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Servidor.txt"))) {
                resultado = Integer.parseInt(br.readLine().split(",")[1]);
            } catch (IOException e) {
            }
        }
        return resultado;
    }

    public String editarParametro(int valor) {
        String resultado = "0";
        System.out.println("Hola llege");
        synchronized (bloqueo) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Servidor.txt"))) {
                bw.write("Sesiones máximas simultaneamente,"+valor);
                resultado = "1";
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public String consultarParametrosServidor() {
        String resultado = "0";
        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Servidor.txt"))) {
                resultado = (br.readLine().split(",")[1]);
            } catch (IOException e) {
            }
        }
        return resultado;
    }
}
