package persona;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Arrays;
import background.Notificador;
import background.Eliminador;

public class Persona {

    protected String nombre;
    protected String contrasenia;
    protected InputStreamReader isr;
    protected OutputStreamWriter osw;
    protected Socket cliente;
    protected InetAddress ip_host;

    private boolean conexion = false;

    public String getNombre() {
        return nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public InputStreamReader getIsr() {
        return isr;
    }

    public OutputStreamWriter getOsw() {
        return osw;
    }

    public Socket getCliente() {
        return cliente;
    }

    public InetAddress getIp_host() {
        return ip_host;
    }

    public int iniciarSesion(String nombre, String contrasenia) {
        int resultado = 0;

        if (validarCredenciales(nombre, contrasenia)) {
            this.nombre = nombre;
            this.contrasenia = contrasenia;
            try {
                char[] mensaje = new char[255];

                if (!conexion) {
                    ip_host = InetAddress.getLocalHost();

                    cliente = new Socket(ip_host.getHostAddress(), 5566);

                    isr = new InputStreamReader(cliente.getInputStream());
                    osw = new OutputStreamWriter(cliente.getOutputStream());
                    new Thread(new Notificador()).start();
                    new Thread(new Eliminador()).start();
                    conexion = true;
                }

                osw.write("buscarPerfil@" + nombre + "@" + contrasenia);
                osw.flush();

                isr.read(mensaje, 0, 255);
                String smensaje = new String(mensaje).trim();
                resultado = Integer.parseInt(smensaje);

            } catch (IOException e) {
                System.out.println(e);
                resultado = 1;
            }

        }

        return resultado;
    }

    protected boolean validarCredenciales(String nombre, String contrasenia) {
        boolean resultado = false;

        if ((nombre.length() >= 4 && nombre.length() <= 18) && (contrasenia.length() > 8 && contrasenia.length() <= 20)) {
            resultado = true;
        }

        return resultado;
    }

    public LinkedList<String[]> seleccionarShows() {
        LinkedList<String[]> shows = new LinkedList<>();
        try {
            char[] mensaje = new char[1000];

            osw.write("consultarShows");
            osw.flush();

            isr.read(mensaje, 0, 1000);

            String smensaje = new String(mensaje).trim();

            LinkedList<String> s_shows = new LinkedList<>(Arrays.asList(smensaje.split("@")));            
            for (String show : s_shows) {
                shows.add(show.split(","));
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        return shows;
    }

    public String[][] seleccionarFunciones(String show_id) {
        LinkedList<String[]> funciones = new LinkedList<>();
        try {
            char[] mensaje = new char[1000];

            osw.write("consultarFunciones@" + show_id);
            osw.flush();

            isr.read(mensaje, 0, 1000);

            String smensaje = new String(mensaje).trim();            
            LinkedList<String> s_funciones = new LinkedList<>(Arrays.asList(smensaje.split("@")));
            for (String funcion : s_funciones) {
                funciones.add(funcion.split(","));
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        String[][] funcionesArray = new String[funciones.size()][];

        for (int i = 0; i < funciones.size(); i++) {
            funcionesArray[i] = funciones.get(i);
        }
        return funcionesArray;
    }

    protected boolean validarTarjeta(String nroTarjeta, String seguridad) {
        boolean resultado = false;

        if (nroTarjeta != null && seguridad != null && !nroTarjeta.equals("null") && !seguridad.equals("null")) {
            resultado = true;
        }

        return resultado;
    }

    public int crearCuenta(String nombre, String contrasenia, String nroTarjeta, String vencimiento, String seguridad) {
        int resultado = 0;

        if (validarCredenciales(nombre, contrasenia) && validarTarjeta(nroTarjeta, seguridad)) {
            this.nombre = nombre;
            this.contrasenia = contrasenia;
            try {
                char[] mensaje = new char[255];

                if (!conexion) {
                    ip_host = InetAddress.getLocalHost();

                    cliente = new Socket(ip_host.getHostAddress(), 5566);

                    isr = new InputStreamReader(cliente.getInputStream());
                    osw = new OutputStreamWriter(cliente.getOutputStream());
                    new Thread(new Notificador()).start();
                    new Thread(new Eliminador()).start();
                    conexion = true;
                }

                osw.write("registrarPerfil@" + nombre + "@" + contrasenia+"@"+nroTarjeta+"@"+vencimiento+"@"+seguridad);
                osw.flush();

                isr.read(mensaje, 0, 255);
                String smensaje = new String(mensaje).trim();
                resultado = Integer.parseInt(smensaje);

            } catch (IOException e) {
                System.out.println(e);
                resultado = 1;
            }

        }

        return resultado;
    }
    
}
