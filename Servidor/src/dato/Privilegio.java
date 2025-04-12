package dato;

import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Privilegio {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String nombreUsuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(nombreUsuario)) {
                bloqueos.put(nombreUsuario, new Object());
            }
            return bloqueos.get(nombreUsuario);
        }
    }

    public static int cantidadLimiteTarjeta(String nombreUsuario) {
        int cantidad = 0;

        synchronized (bloquear(nombreUsuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt"))) {

                String linea = br.readLine();
                linea = br.readLine();                
                cantidad = Integer.parseInt(br.readLine());

            } catch (IOException e) {
            }
        }
        
        return cantidad;
    }

    public String tiempoMaximoCompra(String nombreUsuario) {
        String tiempo = "0";        

        synchronized (bloquear(nombreUsuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt"))) {                
                tiempo = br.readLine();                
                tiempo = br.readLine();
                                                
            } catch (IOException e) { System.out.println(e);
            }
        }
        
        return tiempo;
    }

    public String consultarPrivilegios(String nombreUsuario) {
        String privilegios = "0";
        synchronized (bloquear(nombreUsuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null) {
                    privilegios += linea + "@";
                }

            } catch (IOException e) {
            }
        }        
        return privilegios;
    }

    public String editarPrivilegio(String nombreUsuario, int limiteCompra, int tiempoCompra, int limiteTarjetas, int adm, boolean cambioRol) {
        String resultado;        

        synchronized (bloquear(nombreUsuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt"))) {
                if (cambioRol) {
                    if (adm == 1) {
                        bw.write(0 + "\n" + 0 + "\n" + 0 + "\n" + 1 + "\n");
                    } else {
                        bw.write(3 + "\n" + 3 + "\n" + 4 + "\n" + 0 + "\n");
                    }
                } else {
                    bw.write(limiteCompra + "\n" + tiempoCompra + "\n" + limiteTarjetas + "\n" + adm + "\n");
                }
                resultado = "1";
            } catch (IOException e) {
                System.out.println(e);
                resultado = "0";
            }
        }        

        return resultado;
    }

    public void registrarPrivilegio(String nombreUsuario) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt", true))) {
            bw.write("3\n2\n2\n4\n");

        } catch (IOException e) {
        }
    }

    public boolean eliminarPrivilegios(String nombreUsuario) {
        boolean resultado = false;

        synchronized (bloquear(nombreUsuario)) {
            File archivo = new File("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt");
            if (archivo.delete()) {
                resultado = true;
            }
        }

        return resultado;
    }

    public int cantidadMaximaCompraButaca(String nombreUsuario) {
        int cantidad = 0;        

        synchronized (bloquear(nombreUsuario)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Privilegio.txt"))) {                             
                cantidad = Integer.parseInt(br.readLine());
                                                
            } catch (IOException e) { System.out.println(e);
            }
        }
        
        return cantidad;
    }
}
