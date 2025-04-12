package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.io.File;
import java.util.LinkedList;

public class Pago {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();
    private final static Hashtable<String, Object> bloqueosUsuario = new Hashtable<>();

    private static final Object bloquear(String show_id, String funcion_id) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(show_id + "," + funcion_id)) {
                bloqueos.put(show_id + "," + funcion_id, new Object());
            }
            return bloqueos.get(show_id + "," + funcion_id);
        }
    }

    private static final Object bloquearUsuario(String usuario) {
        synchronized (bloqueosUsuario) {
            if (!bloqueosUsuario.containsKey(usuario)) {
                bloqueosUsuario.put(usuario, new Object());
            }
            return bloqueosUsuario.get(usuario);
        }
    }

    public boolean crearArchivoPago(String show_id, int nr) {
        boolean resultado = false;
        synchronized (bloquear(show_id, String.valueOf(nr))) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + nr + "/Pagos.txt", true))) {
                bw.write("");
                resultado = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return resultado;
    }

    public boolean crearPago(String usuario) {
        boolean resultado = false;
        synchronized (bloquearUsuario(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Pagos.txt"))) {
                bw.write("");
                resultado = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return resultado;
    }

    public String consultarCompras(String nombreUsuario) {
        String compras = "1";
        Funcion oFuncion = new Funcion();
        Show oShow = new Show();       
        
        synchronized (bloquearUsuario(nombreUsuario)) {            
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Usuarios/" + nombreUsuario + "/Pagos.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");                                       
                    if (partes.length <= 7) {
                        String show = oShow.consultarShow(partes[0]);
                        String[] horario_funcion = oFuncion.consultarHorario(partes[0], partes[1]);
                        String sala = oFuncion.consultarSala(partes[0], partes[1]);

                        contenido += show + "," + horario_funcion[0] + "," + horario_funcion[1] + "," + sala + "," + partes[2] + "," + partes[3] + "," + partes[4] + "," + partes[5] + "," + partes[6] + "," + partes[0] + "," + partes[1] + "@";
                    } else { // La funcion ya no existe y algunos valores se harcodearon
                        contenido += linea + "@";
                    }

                }

                if(!contenido.isEmpty()){                                     
                    compras = contenido.substring(0, contenido.length() - 1);
                }                

            } catch (IOException e) {
            }
        }

        
        
        return compras;
    }

    public boolean registrarPago(String show_id, String funcion_id, String usuario, String nro_tarjeta, String butaca) {
        boolean resultado = false;

        synchronized (bloquear(show_id, funcion_id)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Pagos.txt", true))) {
                bw.write(butaca + "," + usuario + "," + nro_tarjeta + "\n");
                resultado = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Pagos.txt", true))) {

                Show oShow = new Show();

                String precio = oShow.consultarPrecio(show_id);

                bw.write(show_id + "," + funcion_id + "," + butaca + "," + nro_tarjeta + "," + precio + ",VIGENTE\n");
                resultado = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return resultado;
    }

    public String cancelarCompra(String show_id, String funcion_id, String butaca, String usuario) { // Cancela una compra individualmente
        String resultado = "0", compra = "CANCELADA";
        Funcion funcion = new Funcion();

        String[] horario = funcion.consultarHorario(show_id, funcion_id);

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        LocalDate fechaComparar = LocalDate.parse(horario[0], formatoFecha);
        LocalTime horaComparar = LocalTime.parse(horario[1], formatoHora);
        LocalTime horaLimite = horaComparar.minusHours(1);

        if (fechaActual.isEqual(fechaComparar) && horaLimite.isBefore(horaActual)) {
            compra = "VENCIDA";
        } else {
            new Butaca().liberarButaca(show_id, funcion_id, butaca);
        }

        synchronized (bloquearUsuario(usuario)) {
            String contenido = "";

            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Pagos.txt"))) {
                String linea = bf.readLine();
                while (linea != null) {
                    String[] partes = linea.split(",");
                    String butacaBuscar = partes[2] + "," + partes[3];

                    if (show_id.equals(partes[0]) && funcion_id.equals(partes[1]) && butaca.equals(butacaBuscar)) {
                        contenido += show_id + "," + funcion_id + "," + butaca + "," + partes[4] + "," + partes[5] + "," + compra + "\n";
                    } else {
                        contenido += linea + "\n";
                    }
                    linea = bf.readLine();
                }

            } catch (IOException e) {
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuario + "/Pagos.txt"))) {
                bw.write(contenido);
                if (!compra.equals("VENCIDA")) {
                    resultado = "2";
                } else {
                    resultado = "1";
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return resultado;
    }

    public boolean cancelarCompra(String usuario) { // Cancela todas las compras de un usuario especifico
        boolean resultado = false;

        synchronized (bloquearUsuario(usuario)) {
            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuario + "/Pagos.txt"))) {
                String linea = bf.readLine();

                while (linea != null) {

                    String[] partes = linea.split(",");
                    String show_id = partes[0], funcion_id = partes[1];
                    String butaca = partes[2] + "," + partes[3];

                    new Butaca().liberarButaca(show_id, funcion_id, butaca);

                    linea = bf.readLine();
                }

            } catch (IOException e) {
                System.out.println(e);
            }

            File archivo = new File("./Datos/Usuarios/" + usuario + "/Pagos.txt");

            if (archivo.delete()) {
                resultado = true;
            }

        }

        

        return resultado;
    }

    public String cancelarCompra(String show_id, String funcion_id, Buzon buzon) { // Cancela todas las compras de una fucion
        String resultado = "0", lineaPago, linea;
        LinkedList<String> usuariosAfectados = new LinkedList<>();
        Bandeja bandeja = new Bandeja();
        
        synchronized (bloquear(show_id, funcion_id)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Pagos.txt"))) {

                while ((lineaPago = br.readLine()) != null) {

                    String usuarioAfectado = lineaPago.split(",")[2], contenido = "";                    

                    if (!usuariosAfectados.contains(usuarioAfectado)) {
                        usuariosAfectados.add(usuarioAfectado);
                        bandeja.registrarEntrada(usuarioAfectado,show_id,"ELIMINACIÓN","La función fue cancelada por un error imprevisto");
                    }

                    try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Usuarios/" + usuarioAfectado + "/Pagos.txt"))) {
                        while ((linea = bf.readLine()) != null) {
                            String[] partes = linea.split(",");

                            if (show_id.equals(partes[0]) && funcion_id.equals(partes[1])) {
                                Funcion oFuncion = new Funcion();
                                String show = new Show().consultarShow(show_id);
                                String[] horario_funcion = oFuncion.consultarHorario(partes[0], partes[1]);
                                String sala = oFuncion.consultarSala(partes[0], partes[1]);
                                
                                contenido += show + "," + horario_funcion[0] + "," + horario_funcion[1] + "," + sala + "," + partes[2] + "," + partes[3] + "," + partes[4] + "," + partes[5] + ",CANCELADO\n";
                            } else {
                                contenido += linea + "\n";
                            }
                        }

                    } catch (IOException e) {
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Usuarios/" + usuarioAfectado + "/Pagos.txt"))) {
                        bw.write(contenido);
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                    
                }

            } catch (IOException e) {
                resultado = "0";
            }

            File archivo = new File("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Pagos.txt");
            if (archivo.delete()) {
                resultado = "1";
            }

        }
        buzon.notificarNuevaEntrada(usuariosAfectados);
        

        return resultado;
    }

    public void notificarModificacionFuncion(String show_id, String funcion_id, String descripcion,Buzon buzon) {
        LinkedList<String> usuariosAfectados = new LinkedList<>();
        String lineaPago;
        Bandeja bandeja = new Bandeja();
        
        synchronized (bloquear(show_id, funcion_id)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id + "/Pagos.txt"))) {
                while ((lineaPago = br.readLine()) != null) {
                    String usuarioAfectado = lineaPago.split(",")[2];
                    if (!usuariosAfectados.contains(usuarioAfectado)) {
                        usuariosAfectados.add(usuarioAfectado);
                        bandeja.registrarEntrada(usuarioAfectado,show_id,"MODIFICACIÓN",descripcion);
                    }
                    
                }
                
            } catch (IOException e) {
            }

        }
        buzon.notificarNuevaEntrada(usuariosAfectados);

    }

}
