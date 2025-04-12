package persona;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Administrador extends Persona {

    public Administrador(Persona persona) {
        this.nombre = persona.getNombre();
        this.contrasenia = persona.getContrasenia();
        this.isr = persona.getIsr();
        this.osw = persona.getOsw();
        this.cliente = persona.getCliente();
        this.ip_host = persona.getIp_host();        
    }

    public int cargarSala(int filas, int columnas, int capMaxima) {
        int resultado;
        try {
            char[] mensaje = new char[255];
            osw.write("registrarSala@" + filas + "@" + columnas + "@" + capMaxima);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 0;
        }
        return resultado;
    }

    public String[][] seleccionarSalas() {
        LinkedList<String[]> salas = new LinkedList<>();
        try {
            char[] mensaje = new char[1000];

            osw.write("consultarSalas");
            osw.flush();

            isr.read(mensaje, 0, 1000);

            String smensaje = new String(mensaje).trim();

            LinkedList<String> s_salas = new LinkedList<>(Arrays.asList(smensaje.split("@")));
            for (String sala : s_salas) {
                salas.add(sala.split(","));
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        String[][] salasArray = new String[salas.size()][];

        for (int i = 0; i < salas.size(); i++) {
            salasArray[i] = salas.get(i);
        }
        return salasArray;
    }

    public int eliminarSala(int nroSala) {
        int resultado;
        try {
            char[] mensaje = new char[255];
            osw.write("eliminarSala@" + nroSala);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 0;
        }
        return resultado;
    }

    public int modificarSala(int nroSala, int filas, int columnas, int capMaxima) {
        int resultado;
        try {
            char[] mensaje = new char[255];
            osw.write("modificarSala@" + nroSala + "@" + filas + "@" + columnas + "@" + capMaxima);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 0;
        }
        return resultado;
    }

    public int cargarShow(String show, int duracion, String director, String estreno, String descripcion, int precio) {
        int resultado = 1;

        if (validarShow(show, director, descripcion)) {
            try {
                char[] mensaje = new char[255];
                osw.write("#registrarShow@" + show + "@" + duracion + "@" + director + "@" + estreno + "@" + descripcion + "@" + precio);
                osw.flush();

                isr.read(mensaje, 0, 255);

                String smensaje = new String(mensaje).trim();
                resultado = Integer.parseInt(smensaje);

            } catch (IOException e) {
                System.out.println(e);
                resultado = 2;
            }
        }

        return resultado;
    }

    private boolean validarShow(String show, String director, String descripcion) {
        boolean resultado = false;
        if ((show.length() >= 2 && show.length() <= 25) && (director.length() >= 4 && director.length() <= 30) && (descripcion.length() >= 20 && descripcion.length() <= 100)) {
            resultado = true;
        }
        return resultado;
    }

    public int modificarShow(int id, String show, int duracion, String director, String estreno, String descripcion, int precio, boolean cambioHorario, boolean cambioNombre) {
        int resultado = 1;

        if (validarShow(show, director, descripcion)) {
            try {
                char[] mensaje = new char[255];
                osw.write("editarShow@" + id + "@" + show + "@" + duracion + "@" + director + "@" + estreno + "@" + descripcion + "@" + precio + "@" + cambioHorario + "@" + cambioNombre);
                osw.flush();

                isr.read(mensaje, 0, 255);
                String smensaje = new String(mensaje).trim();
                resultado = Integer.parseInt(smensaje);

            } catch (IOException e) {
                System.out.println(e);
                resultado = 2;
            }
        }

        return resultado;
    }

    public int eliminarShow(String show_id) {
        int resultado = 0;
        try {
            char[] mensaje = new char[255];
            osw.write("#eliminarShow@" + show_id);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
        }
        return resultado;
    }

    public int cargarFuncion(String show_id, String fecha, String horario, String sala, String idioma, String proyeccion) {
        int resultado = 1;
       

        try {
            char[] mensaje = new char[255];
            osw.write("registrarFuncion@" + show_id + "@" + fecha + "@" + horario + "@" + sala + "@" + idioma + "@" + proyeccion);
            osw.flush();

            isr.read(mensaje, 0, 255);

            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 2;

        }

        return resultado;
    }

    public int modificarFuncion(String show_id, String funcion_id, String fecha, String horario, String sala, String idioma, String proyeccion) {
        int resultado;        

        try {
            char[] mensaje = new char[255];
            osw.write("#modificarFuncion@" + show_id + "@" + funcion_id + "@" + fecha + "@" + horario + "@" + sala + "@" + idioma + "@" + proyeccion);
            osw.flush();

            isr.read(mensaje, 0, 255);

            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 2;

        }

        return resultado;
    }

    public int eliminarFuncion(String show_id, String funcion_id) {
        int resultado = 0;
        try {
            char[] mensaje = new char[255];
            osw.write("#eliminarFuncion@" + show_id + "@" + funcion_id);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
        }
        return resultado;
    }

    public String[][] seleccionarUsuarios() {
        LinkedList<String[]> usuarios = new LinkedList<>();
        try {
            char[] mensaje = new char[1000];

            osw.write("consultarPerfiles");
            osw.flush();

            isr.read(mensaje, 0, 1000);

            String smensaje = new String(mensaje).trim();

            LinkedList<String> s_usuarios = new LinkedList<>(Arrays.asList(smensaje.split("@")));
            for (String sala : s_usuarios) {
                usuarios.add(sala.split(","));
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        String[][] usuariosArray = new String[usuarios.size()][];

        for (int i = 0; i < usuarios.size(); i++) {
            usuariosArray[i] = usuarios.get(i);
        }
        return usuariosArray;
    }

    public int[] seleccionarPrivilegios(String nombreUsuario) {
        int[] privilegios = null;
        try {
            char[] mensaje = new char[255];

            osw.write("consultarPrivilegios@" + nombreUsuario);
            osw.flush();

            isr.read(mensaje, 0, 255);

            String smensaje = new String(mensaje).trim();

            String[] s_privilegios = smensaje.split("@");
            privilegios = new int[s_privilegios.length];

            for (int i = 0; i < s_privilegios.length; i++) {
                privilegios[i] = Integer.parseInt(s_privilegios[i]);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return privilegios;
    }

    public boolean modificarPrivilegio(String nombreUsuario, int[] privilegios, boolean cambioRol) {
        boolean resultado = false;
        try {
            char[] mensaje = new char[255];

            osw.write("editarPrivilegio@" + nombreUsuario + "@" + privilegios[0] + "@" + privilegios[1] + "@" + privilegios[2] + "@" + privilegios[3] + "@" + cambioRol);
            osw.flush();

            isr.read(mensaje, 0, 255);

            String smensaje = new String(mensaje).trim();

            if (smensaje.equals("1")) {
                resultado = true;
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return resultado;
    }

    public int modificarParametro(int valor) {
        int resultado = 0;

        try {
            char[] mensaje = new char[255];
            osw.write("editarParametro@" + valor);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
        }
        return resultado;
    }

    public int seleccionarParametros() {
        int resultado;
        try {
            char[] mensaje = new char[255];
            osw.write("consultarParametrosServidor");
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 0;
        }
        return resultado;
    }

    public int eliminarUsuario(String usuario) {
        int resultado;
        try {
            char[] mensaje = new char[255];
            osw.write("#eliminarUsuario@" + usuario);
            osw.flush();

            isr.read(mensaje, 0, 255);
            String smensaje = new String(mensaje).trim();
            resultado = Integer.parseInt(smensaje);

        } catch (IOException e) {
            System.out.println(e);
            resultado = 0;
        }
        return resultado;
    }

    public String seleccionarFechaEstreno(String show_id) {
        String fecha = "";

        try {
            char[] mensaje = new char[255];

            osw.write("consultarFechaEstreno@" + show_id);
            osw.flush();

            isr.read(mensaje, 0, 255);

            fecha = new String(mensaje).trim();

        } catch (IOException e) {
            System.out.println(e);
        }

        return fecha;
    }

}
