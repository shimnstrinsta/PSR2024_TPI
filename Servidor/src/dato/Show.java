package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Hashtable;

public class Show {

    private final static Object bloqueo = new Object();
    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String show_id) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(show_id)) {
                bloqueos.put(show_id, new Object());
            }
            return bloqueos.get(show_id);
        }
    }

    public String consultarShows() {
        String shows = "";

        synchronized (bloqueo) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
                String linea, contenido = "";

                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                shows = contenido.substring(0, contenido.length() - 1);

            } catch (IOException e) {
            }
        }
        return shows;
    }

    public String registrarShow(String show, int duracion, String director, String estreno, String descripcion, int precio, Buzon buzon) {
        String resultado = "2";
        int nr = 1;

        synchronized (bloqueo) {
            if (!buscarShow(show)) {
                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
                    String linea = bf.readLine();

                    while (linea != null) {
                        nr = Integer.parseInt(linea.split(",")[0]) + 1;
                        linea = bf.readLine();
                    }

                } catch (IOException e) {
                    System.out.println(e);
                }

                File carpeta = new File("./Datos/Shows/Funciones/Show " + nr);
                carpeta.mkdir();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Shows.txt", true))) {
                    bw.write(nr + "," + show + "," + duracion + "," + director + "," + estreno + "," + descripcion + "," + precio + "\n");
                } catch (IOException e) {
                    resultado = "2";
                    System.out.println(e);
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + nr + "/Funciones.txt", true))) {
                    bw.write("");
                    resultado = "0";
                } catch (IOException e) {
                    resultado = "2";
                    System.out.println(e);
                }
            }
        }       
        
        buzon.notificarNuevaEntrada(nr);
        
        return resultado;
    }

    private boolean buscarShow(String show) {
        boolean encontro = false;
        System.out.println("Estoy buscando");
        try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
            String linea = bf.readLine();

            while (linea != null && !encontro) {
                if (linea.split(",")[1].equals(show)) {
                    encontro = true;
                }
                linea = bf.readLine();
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("Resultado busqueda: " + encontro);
        return encontro;
    }

    public String editarShow(int id, String show, int duracion, String director, String estreno, String descripcion, int precio, boolean cambioHorario, boolean cambioNombre) {
        String resultado = "2", contenido = "";
        boolean interposicionHorario = false, interposicionNombre = false;

        System.out.println("Estoy modificando show");

        synchronized (bloqueo) {
            if (cambioHorario) {
                System.out.println("Se cambio el horario");
                Funcion funcion = new Funcion();
                interposicionHorario = funcion.validarInterposicion(String.valueOf(id), duracion);
            }
            if (cambioNombre) {
                interposicionNombre = buscarShow(show);
            }
            if (!interposicionNombre) {
                if (!interposicionHorario) {
                    try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
                        String linea = bf.readLine();

                        while (linea != null) {
                            if (id != Integer.parseInt(linea.split(",")[0])) {
                                contenido += linea + "\n";
                            } else {
                                contenido += id + "," + show + "," + duracion + "," + director + "," + estreno + "," + descripcion + "," + precio + "\n";
                            }
                            linea = bf.readLine();
                        }

                    } catch (IOException e) {
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Shows.txt"))) {
                        bw.write(contenido);
                        resultado = "0";
                    } catch (IOException e) {
                        resultado = "2";
                    }
                } else {
                    resultado = "3";
                }
            }

        }

        return resultado;
    }

    public int consultarDuracion(String show_id) {
        int duracion = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
            synchronized (bloqueo) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (show_id.equals(linea.split(",")[0])) {
                        duracion = Integer.parseInt(linea.split(",")[2]);
                    }
                }

            }
        } catch (IOException e) {
        }

        return duracion;
    }

    public String eliminarShow(String show_id,Buzon buzon) {
        String resultado = "0";

        synchronized (bloqueo) {
            if (new Funcion().vaciarFuncionesShow(show_id,buzon)) {
                String contenido = "";
                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
                    String linea = bf.readLine();

                    while (linea != null) {
                        if (!show_id.equals(linea.split(",")[0])) {
                            contenido += linea + "\n";
                        }
                        linea = bf.readLine();
                    }

                } catch (IOException e) {
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Shows.txt"))) {
                    bw.write(contenido);
                    resultado = "1";
                } catch (IOException e) {
                }

            }
        }
             
        return resultado;
    }

    public String consultarFechaEstreno(String show_id) {
        String fecha = "";

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
            synchronized (bloqueo) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (show_id.equals(linea.split(",")[0])) {
                        fecha = linea.split(",")[4];
                    }
                }

            }
        } catch (IOException e) {
        }

        return fecha;
    }

    public String consultarShow(String show_id) {
        String nombre = "";

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
            synchronized (bloqueo) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (show_id.equals(linea.split(",")[0])) {
                        nombre = linea.split(",")[1];
                    }
                }

            }
        } catch (IOException e) {
        }

        return nombre;
    }

    public String consultarPrecio(String show_id) {
        String nombre = "";

        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Shows.txt"))) {
            synchronized (bloqueo) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (show_id.equals(linea.split(",")[0])) {
                        nombre = linea.split(",")[6];
                    }
                }

            }
        } catch (IOException e) {
        }

        return nombre;
    }

}
