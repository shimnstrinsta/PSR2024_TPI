package dato;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class Perfil {

    private final static Object bloqueo = new Object();

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String usuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(usuario)) {
                bloqueos.put(usuario, new Object());
            }
            return bloqueos.get(usuario);
        }
    }

    public String buscarPerfil(String nombre, String contrasenia) {
        String resultado = "3";
        System.out.println("HOLALA???");
        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null) {
                    String palabras[] = linea.split(",");
                    if (palabras[1].equals(nombre) && palabras[2].equals(contrasenia)) {
                        resultado = palabras[3];
                    }
                }
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public static String consultarPerfiles() {
        String perfiles = "1";

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {
            synchronized (bloqueo) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if (!contenido.isEmpty()) {
                    perfiles = contenido.substring(0, contenido.length() - 1);
                }

            }
        } catch (IOException e) {
        }

        return perfiles;
    }

    public String registrarPerfil(String nombre, String contrasenia, String nroTarjeta, String vencimiento, String seguridad) {
        String resultado = "3";
        boolean esta = false;
        int nr = 1;

        synchronized (bloqueo) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null && !esta) {
                    String palabras[] = linea.split(",");
                    if (palabras[1].equals(nombre)) {                        
                        esta = true;
                    }
                }
            } catch (IOException e) {
            }
        }

        if (!esta) {
            synchronized (bloqueo) {
                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {
                    String linea = bf.readLine();

                    while (linea != null) {
                        nr = Integer.parseInt(linea.split(",")[0]) + 1;
                        linea = bf.readLine();
                    }

                } catch (IOException e) {
                    System.out.println(e);
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/Usuarios.txt", true))) {
                    bw.write(nr + "," + nombre + "," + contrasenia + "," + "4\n");

                } catch (IOException e) {
                }

            }
            synchronized (bloquear(nombre)) { // Nuevo bloqueo por usuario

                File carpeta = new File("./Datos/Usuarios/" + nombre);
                carpeta.mkdir();

                new Privilegio().registrarPrivilegio(nombre);
                new Tarjeta().registrarTarjeta(nombre, nroTarjeta, vencimiento, seguridad);
                Bandeja.crearBandejaEntrada(nombre);
                new Pago().crearPago(nombre);
                resultado = "4";
            }
        }
        

        return resultado;
    }

    public String eliminarUsuario(String nombre, Buzon buzon) {
        String resultado = "0";
        synchronized (bloqueo) {
            String contenido = "";

            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/Usuarios.txt"))) {
                String linea = bf.readLine();
                while (linea != null) {
                    if (!nombre.equals(linea.split(",")[1])) {
                        contenido += linea + "\n";
                    }
                    linea = bf.readLine();
                }
            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/Usuarios.txt"))) {
                bw.write(contenido);
                resultado = "1";
            } catch (IOException e) {
            }
        }

        synchronized (bloquear(nombre)) {
            if (resultado.equals("1")) {
                if (new Tarjeta().borrarTarjeta(nombre) && new Privilegio().eliminarPrivilegios(nombre) && Bandeja.eliminarBandejaEntrada(nombre) && new Pago().cancelarCompra(nombre)) {
                    File carpeta = new File("./Datos/Usuarios/" + nombre);
                    carpeta.delete();
                } else {
                    resultado = "0";
                }
            }
        }

        buzon.enviarEliminacion(nombre);
        return resultado;
    }
}
