package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class Bandeja {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String nombreUsuario) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(nombreUsuario)) {
                bloqueos.put(nombreUsuario, new Object());
            }
            return bloqueos.get(nombreUsuario);
        }
    }

    public static void crearBandejaEntrada(String nombreUsuario) {

        synchronized (bloquear(nombreUsuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + nombreUsuario + "/Bandeja Entrada.txt"))) {
                bw.write("");
            } catch (IOException e) {
            }
        }
    }

    public static boolean eliminarBandejaEntrada(String nombreUsuario) {
        boolean resultado = false;

        synchronized (bloquear(nombreUsuario)) {
            File archivo = new File("./Datos/Usuarios/" + nombreUsuario + "/Bandeja Entrada.txt");
            if (archivo.delete()) {
                resultado = true;
            }
        }

        return resultado;
    }

    public String consultarBandejaEntrada(String nombreUsuario) {
        String avisos = "1";
        Show show = new Show();

        synchronized (bloquear(nombreUsuario)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Bandeja Entrada.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    String show_nombre = show.consultarShow(partes[1]);
                    if (show_nombre.isEmpty()) {
                        contenido += partes[0] + "," + partes[1] + "," + partes[2] + "," + partes[3] + "," + partes[4] + "@";
                    } else {
                        contenido += partes[0] + "," + show_nombre + "," + partes[2] + "," + partes[3] + "," + partes[4] + "@";
                    }

                }

                if(!contenido.isEmpty()){
                    avisos = contenido.substring(0, contenido.length() - 1);    
                }
                

            } catch (IOException e) {
            }
        }

        
        return avisos;
    }

    public void registrarEntrada(String usuario, String show_id, String cambio, String descripcion) {
        int nr = 1;
        synchronized (bloquear(usuario)) {

            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Bandeja Entrada.txt"))) {
                String linea;

                while ((linea = bf.readLine()) != null) {
                    nr = Integer.parseInt(linea.split(",")[0]) + 1;
                }

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Bandeja Entrada.txt", true))) {
                if(cambio.equals("ELIMINACIÓN")){
                    String nombre_show = new Show().consultarShow(show_id);
                    bw.write(nr + "," + nombre_show + "," + cambio + ",SI," + descripcion + ",SIN RECLAMAR\n");
                }
                else{
                    bw.write(nr + "," + show_id + "," + cambio + ",SI," + descripcion + ",SIN RECLAMAR\n");
                }
                
            } catch (IOException e) {
            }
        }
    }

    public String reclamarAlteracion(String aviso_id, String usuario) {
        String resultado = "0", contenido = "";

        synchronized (bloquear(usuario)) {
            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Bandeja Entrada.txt"))) {
                String linea;

                while ((linea = bf.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (aviso_id.equals(partes[0])) {
                        if (partes[5].equals("SIN RECLAMAR")) {
                            contenido += partes[0] + "," + partes[1] + "," + partes[2] + "," + partes[3] + "," + partes[4] + ",RECLAMADO" + "\n";
                        } else {
                            contenido += linea + "\n";
                            resultado = "1";
                        }
                    } else {
                        contenido += linea + "\n";
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
            }

            if (resultado.equals("0")) {

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Bandeja Entrada.txt"))) {
                    bw.write(contenido);
                    resultado = "2";
                } catch (IOException e) {
                }
            }
        }
        return resultado;
    }

    public void registrarEntrada(String show_id, String descripcion) {

        String[] usuarios = Perfil.consultarPerfiles().split("@");

        for (String usuario : usuarios) {

            int nr = 1;
            String nombre = usuario.split(",")[1];            
            synchronized (bloquear(nombre)) {

                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombre + "/Bandeja Entrada.txt"))) {
                    String linea;

                    while ((linea = bf.readLine()) != null) {
                        System.out.println(linea);
                        nr = Integer.parseInt(linea.split(",")[0]) + 1;
                    }

                } catch (IOException e) {
                    System.out.println(e);
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + nombre + "/Bandeja Entrada.txt", true))) {
                    bw.write(nr + "," + show_id + ",AGREGACIÓN,NO," + descripcion + ",NO RECLAMABLE\n");
                } catch (IOException e) {
                }
            }
        }

    }
}
