package dato;

import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;

public class Buzon {

    private static final Hashtable<String, OutputStreamWriter> lista_el = new Hashtable<>(); // Estos son todos a los que hay que mandar   
    private static final Hashtable<String, OutputStreamWriter> lista_nt = new Hashtable<>(); // Estos son todos a los que hay que mandar   
    private final OutputStreamWriter nt; // Este es el que esta mandando
    private final OutputStreamWriter el; // Este es el que esta mandando
    private final String id;

    public Buzon(OutputStreamWriter osw, String nombre, OutputStreamWriter el) {

        this.nt = osw;
        this.el = el;

        id = "#" + new Random().nextInt(100000) + "," + nombre;
        synchronized (lista_nt) {
            lista_nt.put(id, nt);
        }
        synchronized (lista_el) {
            lista_el.put(id, el);
        }
    }

    public void borrarNotificador() {
        synchronized (lista_nt) {
            Iterator<String> iterator = lista_nt.keySet().iterator();
            while (iterator.hasNext()) {
                String llave = iterator.next();
                if (lista_nt.get(llave) == nt) {
                    iterator.remove(); // Usa el método remove del iterador
                }
            }
        }

        synchronized (lista_el) {
            Iterator<String> iterator = lista_el.keySet().iterator();
            while (iterator.hasNext()) {
                String llave = iterator.next();
                if (lista_el.get(llave) == el) {
                    iterator.remove(); // Usa el método remove del iterador
                }
            }
        }
    }

    public void notificarNuevaEntrada(int show_id) {
        try {
            synchronized (lista_nt) {
                for (String usuario : lista_nt.keySet()) {
                    OutputStreamWriter osw = lista_nt.get(usuario);

                    if (nt != osw) {
                        osw.write("Tienes un nuevo mensaje en el buzón de entrada");
                        osw.flush();
                    }

                }
            }
            new Bandeja().registrarEntrada(String.valueOf(show_id), "Nuevo show disponible. No te lo pierdas!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notificarNuevaEntrada(LinkedList<String> usuariosAfectados) {
        try {
            synchronized (lista_nt) {
                for (String id_ : lista_nt.keySet()) {
                    String usuario = id_.split(",")[1];
                    OutputStreamWriter osw = lista_nt.get(id_);

                    if (nt != osw && usuariosAfectados.contains(usuario)) {
                        osw.write("Tienes un nuevo mensaje en el buzón de entrada");
                        osw.flush();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarEliminacion(String usuario) {
        synchronized (lista_el) {
            for (String id_ : lista_el.keySet()) {
                String nombre = id_.split(",")[1];
                if (nombre.equals(usuario)) {
                    try {
                        lista_el.get(id_).write("1");
                        lista_el.get(id_).flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

}
