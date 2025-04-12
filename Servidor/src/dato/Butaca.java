package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Arrays;
import java.io.File;

public class Butaca {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String show_id, String funcion_id) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(show_id + "," + funcion_id)) {
                bloqueos.put(show_id + "," + funcion_id, new Object());
            }
            return bloqueos.get(show_id + "," + funcion_id);
        }
    }

    public boolean crearButacas(String show_id, String funcion_id, String sala) {

        boolean resultado = false;
        synchronized (bloquear(show_id, funcion_id)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt", true))) {
                int[] info_sala = Sala.consultarSala(sala);
                String butacas = "";

                for (int i = 0; i < info_sala[0]; i++) {
                    for (int j = 0; j < info_sala[1]; j++) {
                        butacas += String.valueOf(i) + "," + String.valueOf(j) + ",Disponible\n";
                    }
                }

                bw.write(butacas);
                resultado = true;
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public String consultarButacas(String show_id, String funcion_id) {
        String butacas = "1";        

        synchronized (bloquear(show_id, funcion_id)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if(!contenido.isEmpty()){
                    butacas = contenido.substring(0, contenido.length() - 1);
                }
                

            } catch (IOException e) {
            }
        }
        
        return butacas;
    }

    public String ocuparButacas(String show_id, String funcion_id, String butacasString, String usuario, String id_transaccion) {
        String resultado = "0";
        LinkedList<String> butacas = new LinkedList<>(Arrays.asList(butacasString.split("-")));
        String linea, contenido = "";
        boolean yaOcupada = false;
        
        if (butacas.size() <= new Privilegio().cantidadMaximaCompraButaca(usuario)) {            
            synchronized (bloquear(show_id, funcion_id)) {
                try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {

                    while ((linea = br.readLine()) != null && !yaOcupada) {
                        String[] palabras = linea.split(",");
                        String butacaBuscar = palabras[0] + "," + palabras[1];

                        if (butacas.contains(butacaBuscar)) {
                            if (palabras[2].equals("Disponible")) {
                                contenido += butacaBuscar + ",Bloqueada," + usuario + "," + id_transaccion + "\n";
                            } else {
                                yaOcupada = true;
                                resultado = "1";
                            }
                        } else {
                            contenido += linea + "\n";
                        }
                    }

                } catch (IOException e) {
                }

                if (!yaOcupada) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                        bw.write(contenido);
                        resultado = "2";
                    } catch (IOException e) {
                    }
                }

            }
        }

        return resultado;
    }

    public String comprarButacas(String show_id, String funcion_id, String nro_tarjeta, String usuario, String id_transaccion) {
        String resultado = "0";
        String linea, contenido = "";
        Pago pago = new Pago();

        synchronized (bloquear(show_id, funcion_id)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                while ((linea = br.readLine()) != null) {
                    String[] palabras = linea.split(",");

                    if (palabras[2].equals("Bloqueada")) {
                        if (palabras[3].equals(usuario) && palabras[4].equals(id_transaccion)) {
                            contenido += palabras[0] + "," + palabras[1] + ",Ocupada\n";
                            pago.registrarPago(show_id, funcion_id, usuario, nro_tarjeta, palabras[0] + "," + palabras[1]);
                        } else {
                            contenido += linea + "\n";
                        }
                    } else {
                        contenido += linea + "\n";
                    }
                }

            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                bw.write(contenido);
                resultado = "2";
            } catch (IOException e) {
            }

        }

        return resultado;

    }

    public String desocuparButacas(String show_id, String funcion_id, String id_transaccion) {
        String linea, contenido = "";
        synchronized (bloquear(show_id, funcion_id)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                while ((linea = br.readLine()) != null) {
                    String[] palabras = linea.split(",");

                    if (palabras[2].equals("Bloqueada")) {
                        if (palabras[4].equals(id_transaccion)) {
                            contenido += palabras[0] + "," + palabras[1] + ",Disponible\n";
                        } else {
                            contenido += linea + "\n";
                        }
                    } else {
                        contenido += linea + "\n";
                    }
                }

            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                bw.write(contenido);
            } catch (IOException e) {
            }

        }
        return "1";
    }

    public void liberarButaca(String show_id, String funcion_id, String butaca) {

        synchronized (bloquear(show_id, funcion_id)) {
            String linea,contenido = "";
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                while ((linea = br.readLine()) != null) {
                    String[] palabras = linea.split(",");
                    String butacaBuscar = palabras[0] + "," + palabras[1];
                    
                    if (butacaBuscar.equals(butaca)) {
                        contenido += butaca + ",Disponible\n";
                    } else {
                        contenido += linea + "\n";
                    }
                }

            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt"))) {
                bw.write(contenido);
            } catch (IOException e) {
            }
        }

    }
    
    public boolean liberarButaca(String show_id, String funcion_id) {
        boolean resultado = false;        
        
        synchronized (bloquear(show_id, funcion_id)) {                     
            File archivo = new File("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Butacas.txt");            
            if (archivo.delete()){
                resultado = true;
            }
        }
                
        return resultado;

    }
}
