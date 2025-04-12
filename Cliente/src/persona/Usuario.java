package persona;

import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JLabel;
import background.Temporizador;
import java.util.Hashtable;

public class Usuario extends Persona {

    private static final Hashtable<String, Thread> temporizadores = new Hashtable<>();

    public Usuario(Persona persona) {
        this.nombre = persona.getNombre();
        this.contrasenia = persona.getContrasenia();
        this.isr = persona.getIsr();
        this.osw = persona.getOsw();
        this.cliente = persona.getCliente();
        this.ip_host = persona.getIp_host();        
    }

    public Usuario() {
    }

    public String[][] seleccionarTarjetas() {
        String[][] tarjetas = null;

        try {
            char[] mensaje = new char[255];

            osw.write("consultarTarjetas@" + nombre);
            osw.flush();

            isr.read(mensaje, 0, 255);

            String smensaje = new String(mensaje).trim();

            String[] s_tarjetas = smensaje.split("@");
            tarjetas = new String[s_tarjetas.length][3];

            for (int i = 0; i < s_tarjetas.length; i++) {
                tarjetas[i] = s_tarjetas[i].split(",");
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return tarjetas;
    }

    public char eliminarTarjeta(String numeroTarjeta) {
        char resultado = '0';
        try {
            char[] mensaje = new char[255];

            osw.write("borrarTarjeta@" + nombre + "@" + numeroTarjeta);
            osw.flush();

            isr.read(mensaje, 0, 255);
            resultado = mensaje[0];
        } catch (IOException e) {
            System.out.println(e);
        }
        return resultado;
    }

    public int cargarTarjeta(String nroTarjeta, String vencimiento, String seguridad) {
        int resultado = 3;
        if (validarTarjeta(nroTarjeta, seguridad)) {
            try {
                char[] mensaje = new char[255];

                osw.write("registrarTarjeta@" + nombre + "@" + nroTarjeta + "@" + vencimiento + "@" + seguridad);
                osw.flush();

                isr.read(mensaje, 0, 255);
                String smensaje = new String(mensaje).trim();

                resultado = Integer.parseInt(smensaje);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return resultado;
    }

    public String[][] seleccionarCompras() {
        String[][] compras = null;

        try {
            char[] mensaje = new char[100000];

            osw.write("consultarCompras@" + nombre);
            osw.flush();

            isr.read(mensaje, 0, 100000);

            String smensaje = new String(mensaje).trim();

            String[] s_compras = smensaje.split("@");
            compras = new String[s_compras.length][3];

            for (int i = 0; i < s_compras.length; i++) {
                compras[i] = s_compras[i].split(",");
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return compras;
    }

    public String[][] accederFuncion(String show_id, String funcion_id, JLabel tiempo, String id_ventana) {
        String[][] butacas = null;

        try {
            char[] mensaje = new char[100000];

            osw.write("consultarButacas@" + show_id + "@" + funcion_id);
            osw.flush();

            isr.read(mensaje, 0, 100000);

            String smensaje = new String(mensaje).trim();
            String[] s_butacas = smensaje.split("@");
            butacas = new String[s_butacas.length][3];

            for (int i = 0; i < s_butacas.length; i++) {
                butacas[i] = s_butacas[i].split(",");
            }

            // Activar temporizador    
            char[] mensaje2 = new char[10];

            osw.write("tiempoMaximoCompra@" + nombre);
            osw.flush();

            isr.read(mensaje2, 0, 10);

            int tiempoMax = Integer.parseInt(new String(mensaje2).trim());
            tiempo.setText(String.format("%02d", tiempoMax) + ":00");

            Thread tempo_thread = new Thread(new Temporizador(tiempoMax, tiempo, id_ventana, show_id, funcion_id, this));
            temporizadores.put(id_ventana, tempo_thread);
            tempo_thread.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (tempo_thread.isAlive()) {                    
                    cancelarPago(show_id, funcion_id, id_ventana); // Si se cierra el programa cancelar compra
                }

            }));

        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }

        return butacas;
    }

    public int seleccionarButacas(String show_id, String funcion_id, LinkedList<String> butacas, String id_transaccion) {
        int resultado = 0;

        try {
            char[] mensaje = new char[255];
            String butacas_s = "";
            for (String butaca : butacas) {
                butacas_s += butaca + "-";
            }

            osw.write("ocuparButacas@" + show_id + "@" + funcion_id + "@" + butacas_s + "@" + nombre + "@" + id_transaccion);
            osw.flush();

            isr.read(mensaje, 0, 255);

            resultado = Integer.parseInt(new String(mensaje).trim());

        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }

        return resultado;
    }

    public int comprarButacas(String show_id, String funcion_id, String nro_tarjeta, String id_transaccion) {
        int resultado = 0;

        try {
            char[] mensaje = new char[255];

            osw.write("comprarButacas@" + show_id + "@" + funcion_id + "@" + nro_tarjeta + "@" + nombre + "@" + id_transaccion);
            osw.flush();

            isr.read(mensaje, 0, 255);

            temporizadores.get(id_transaccion).interrupt();
            resultado = Integer.parseInt(new String(mensaje).trim());

        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }

        return resultado;
    }

    public void cancelarPago(String show_id, String funcion_id, String transaccion_id) {
        try {
            char[] mensaje = new char[255];

            osw.write("desocuparButacas@" + show_id + "@" + funcion_id + "@" + transaccion_id);
            osw.flush();

            isr.read(mensaje, 0, 255);

        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }
    }

    public int cancelarCompra(String show_id, String funcion_id, String butaca) {
        int resultado = 0;
        try {
            char[] mensaje = new char[255];

            osw.write("cancelarCompra@" + show_id + "@" + funcion_id + "@" + butaca + "@" + nombre);
            osw.flush();

            isr.read(mensaje, 0, 255);

            resultado = Integer.parseInt(new String(mensaje).trim());

        } catch (IOException e) {
            System.out.println(e);
        }

        return resultado;
    }

    public String[][] seleccionarAvisos() {
        String[][] avisos = null;

        try {
            char[] mensaje = new char[100000];

            osw.write("consultarBandejaEntrada@" + nombre);
            osw.flush();

            isr.read(mensaje, 0, 100000);

            String smensaje = new String(mensaje).trim();            
            String[] s_avisos = smensaje.split("@");
            avisos = new String[s_avisos.length][3];

            for (int i = 0; i < s_avisos.length; i++) {
                avisos[i] = s_avisos[i].split(",");
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return avisos;
    }

    public int reclamarAlteracion(String id) {
        int resultado = 0;
        try {
            char[] mensaje = new char[255];

            osw.write("reclamarAlteracion@" + id + "@" + nombre);
            osw.flush();

            isr.read(mensaje, 0, 255);

            resultado = Integer.parseInt(new String(mensaje).trim());

        } catch (IOException e) {
            System.out.println(e);
        }

        return resultado;
    }

}
