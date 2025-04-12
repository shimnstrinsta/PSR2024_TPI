package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.io.File;

public class Tarjeta {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String nombreUsuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(nombreUsuario)) {
                bloqueos.put(nombreUsuario, new Object());
            }
            return bloqueos.get(nombreUsuario);
        }
    }

    public String consultarTarjetas(String nombreUsuario) {
        String tarjetas = "";

        synchronized (nombreUsuario) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt"))) {

                String linea, contenido = "";
                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                tarjetas = contenido.substring(0, contenido.length() - 1);

            } catch (IOException e) {
            }
        }

        return tarjetas;
    }

    public String borrarTarjeta(String nombreUsuario, String numeroTarjeta) {
        String resultado = "0", contenido = "";
        int cantidad = 0;
        boolean aux = true;

        synchronized (bloquear(nombreUsuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (!numeroTarjeta.equals(linea.split(",")[0])) {
                        contenido += linea + "\n";
                        cantidad++;
                    }
                }

            } catch (IOException e) {
                aux = false;
            }

            if (cantidad >= 1 && aux) {

                try (FileWriter fileWriter = new FileWriter("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt", false)) {
                    fileWriter.write(contenido);
                    resultado = "1";
                } catch (IOException e) {
                }
            }
        }

        return resultado;
    }

    public String registrarTarjeta(String nombreUsuario, String nroTarjeta, String vencimiento, String seguridad) {
        String resultado = "1";
        synchronized (bloquear(nombreUsuario)) {
            if (!buscarTarjeta(nombreUsuario, nroTarjeta)) { // Si no esta cargada la tarjeta

                int cantidadTarjetas = 0;
                
                try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt"))) {

                    String linea;
                    while ((linea = br.readLine()) != null) {
                        cantidadTarjetas++;
                    }

                } catch (IOException e) {
                }

                System.out.println("Llego hasta aca?");
                if (cantidadTarjetas+1 <= Privilegio.cantidadLimiteTarjeta(nombreUsuario)) { // Si no supera el limite de tarjetas
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt", true))) {

                        bw.write(nroTarjeta + "," + vencimiento + "," + seguridad + "\n");
                        resultado = "0";

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else {
                    resultado = "2";
                }

                System.out.println("No puedo");
            }
        }
        return resultado;
    }

    private boolean buscarTarjeta(String nombreUsuario, String nroTarjeta) {
        boolean resultado = false;

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Tarjetas.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null && !resultado) {
                System.out.println("Tarjeta: " + nroTarjeta + " Tiene que coincidir con " + linea.split(",")[0]);
                if (linea.split(",")[0].equals(nroTarjeta)) {
                    resultado = true;
                }
            }

        } catch (IOException e) {
        }

        System.out.println("Resultado busqueda tarjeta: "+resultado);
        return resultado;
    }

    public boolean borrarTarjeta(String nombreUsuario){
        boolean resultado = false;
        
        synchronized(bloquear(nombreUsuario)){
            File archivo = new File("./Datos/Usuarios/"+nombreUsuario+"/Tarjetas.txt");
            if (archivo.delete()) resultado = true;
        }
        
        System.out.println("Resultado de eliminar tarjetas: "+resultado);
        
        return resultado;
    }
}
