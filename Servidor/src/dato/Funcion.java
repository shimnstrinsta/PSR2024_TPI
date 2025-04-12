package dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Hashtable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;

public class Funcion {

    private final static Hashtable<String, Object> bloqueos = new Hashtable<>();

    private static final Object bloquear(String show_id) {
        synchronized (bloqueos) {
            if (!bloqueos.containsKey(show_id)) {
                bloqueos.put(show_id, new Object());
            }
            return bloqueos.get(show_id);
        }
    }

    private final static Hashtable<String, Object> bloqueosFunciones = new Hashtable<>();

    private static final Object bloquearFunciones(String show_id, String funcion_id) {
        synchronized (bloqueosFunciones) {
            if (!bloqueos.containsKey(show_id + "," + funcion_id)) {
                bloqueos.put(show_id + "," + funcion_id, new Object());
            }
            return bloqueos.get(show_id + "," + funcion_id);
        }
    }

    public void vaciarFuncionesSala(int nroSala) {
        File carpeta = new File("./Datos/Shows/Funciones");
        
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        for (File archivo : archivos) {
            synchronized (bloquear(archivo.getName())) {
                try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/" + archivo.getName() + ""))) {
                    String linea, contenido = "";
                    while ((linea = br.readLine()) != null) {
                        String[] funcion = linea.split(",");

                        if (funcion[2].equals(String.valueOf(nroSala))) {
                            contenido += funcion[0] + "," + funcion[1] + ",Sin definir," + funcion[3] + "," + funcion[4] + "\n";
                        } else {
                            contenido += linea + "\n";
                        }
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/" + archivo.getName() + ""))) {
                        bw.write(contenido);
                    } catch (IOException e) {
                    }

                } catch (IOException e) {
                }
            }
        }
    }

    public boolean buscarFunciones(int nroSala) {
        boolean resultado = false;

        File carpeta = new File("./Datos/Shows/Funciones");

        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        for (File archivo : archivos) {
            synchronized (bloquear(archivo.getName())) {
                try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + archivo.getName() + "/Funciones.txt"))) {
                    String linea;
                    while ((linea = br.readLine()) != null && !resultado) {
                        String[] funcion = linea.split(",");

                        if (funcion[2].equals(String.valueOf(nroSala))) {
                            resultado = true;
                        }
                    }

                } catch (IOException e) {
                }
            }
        }
        return resultado;
    }

    public boolean validarInterposicion(String idShow, int duracion) {
        boolean resultado = false;
        LinkedList<LocalTime> horasInicios = new LinkedList<>();
        LinkedList<String> salas = new LinkedList<>();
        LinkedList<String> fechas = new LinkedList<>();
        String linea;
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        synchronized (bloquear(idShow)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + idShow + "/Funciones.txt"))) {

                while ((linea = br.readLine()) != null && !resultado) {
                    try {
                        horasInicios.add((LocalTime.parse(linea.split(",")[2], formatoHora)));
                        salas.add(linea.split(",")[3]);
                        fechas.add(linea.split(",")[1]);
                    } catch (DateTimeParseException e) {
                    }

                }

            } catch (IOException e) {
            }

            int i = 0, j;

            while (i < horasInicios.size() && !resultado) {

                LocalTime horaInicio = horasInicios.get(i);
                LocalTime horaFin = horaInicio.plusMinutes(duracion);
                String salaInicial = salas.get(i);
                String fechaInicial = fechas.get(i);

                j = 0;
                while (j < horasInicios.size() && !resultado) {

                    String sala = salas.get(j);
                    LocalTime horaComparar = horasInicios.get(j);
                    String fecha = fechas.get(j);

                    if (salaInicial.equals(sala) && fecha.equals(fechaInicial)) {

                        if (horaInicio.isBefore(horaFin)) {
                            resultado = (horaComparar.isAfter(horaInicio) && horaComparar.isBefore(horaFin));
                        } else {
                            resultado = horaComparar.isAfter(horaInicio) || horaComparar.isBefore(horaFin);
                        }
                    }
                    
                    j++;
                }                
                i++;
            }
        }
        
        return resultado;
    }

    private boolean validarInterposicion(String idShow, String horario, String sala, String fecha) {
        boolean resultado = false;
        LinkedList<LocalTime> horasInicios = new LinkedList<>();
        LinkedList<String> salas = new LinkedList<>();
        LinkedList<String> fechas = new LinkedList<>();
        String linea;
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        

        int duracion = new Show().consultarDuracion(idShow);
        

        synchronized (bloquear(idShow)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + idShow + "/Funciones.txt"))) {

                while ((linea = br.readLine()) != null && !resultado) {
                    try {
                        horasInicios.add((LocalTime.parse(linea.split(",")[2], formatoHora)));
                        fechas.add(linea.split(",")[1]);
                        salas.add(linea.split(",")[3]);
                    } catch (DateTimeParseException e) {
                    }

                }

            } catch (IOException e) {
            }

            int j = 0;

            LocalTime horaInicio = LocalTime.parse(horario, formatoHora);
            LocalTime horaFin = horaInicio.plusMinutes(duracion);
            boolean inter1, inter2;

            while (j < horasInicios.size() && !resultado) {
                String salaComparar = salas.get(j);
                LocalTime horaComparar = horasInicios.get(j);
                String fechaComparar = fechas.get(j);
                

                if (sala.equals(salaComparar) && fecha.equals(fechaComparar)) {                    
                    
                    if (horaInicio.isBefore(horaFin)) {
                        inter1 = (horaComparar.isAfter(horaInicio) && horaComparar.isBefore(horaFin));
                        inter2 = (horaInicio.isAfter(horaComparar) && horaInicio.isBefore(horaComparar.plusMinutes(duracion)));
                    } else {
                        inter1 = horaComparar.isAfter(horaInicio) || horaComparar.isBefore(horaFin);
                        inter2 = horaInicio.isAfter(horaComparar) || horaInicio.isBefore(horaComparar.plusMinutes(duracion));
                    }
                    resultado = inter1 || inter2;
                }

                //System.out.println("Hora inicio " + horaInicio + " Hora fin " + horaFin + " Hora comparar " + horaComparar + " Resultado: " + resultado);
                j++;
            }           
        }
        
        return resultado;
    }

    private boolean validarInterposicion(String idShow, String funcion_id, String horario, String sala, String fecha) {
        boolean resultado = false;
        LinkedList<LocalTime> horasInicios = new LinkedList<>();
        LinkedList<String> salas = new LinkedList<>();
        LinkedList<String> fechas = new LinkedList<>();
        LinkedList<String> ids = new LinkedList<>();
        String linea;
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");        

        int duracion = new Show().consultarDuracion(idShow);        

        synchronized (bloquear(idShow)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + idShow + "/Funciones.txt"))) {

                while ((linea = br.readLine()) != null && !resultado) {
                    try {
                        horasInicios.add((LocalTime.parse(linea.split(",")[2], formatoHora)));
                        fechas.add(linea.split(",")[1]);
                        salas.add(linea.split(",")[3]);
                        ids.add(linea.split(",")[0]);
                    } catch (DateTimeParseException e) {
                    }

                }

            } catch (IOException e) {
            }

            int j = 0;

            LocalTime horaInicio = LocalTime.parse(horario, formatoHora);
            LocalTime horaFin = horaInicio.plusMinutes(duracion);
            boolean inter1, inter2;

            while (j < horasInicios.size() && !resultado) {
                String salaComparar = salas.get(j);
                LocalTime horaComparar = horasInicios.get(j);
                String fechaComparar = fechas.get(j);
                String idComparar = ids.get(j);
                

                if (sala.equals(salaComparar) && fecha.equals(fechaComparar) && !funcion_id.equals(idComparar)) {


                    if (horaInicio.isBefore(horaFin)) {
                        inter1 = (horaComparar.isAfter(horaInicio) && horaComparar.isBefore(horaFin));
                        inter2 = (horaInicio.isAfter(horaComparar) && horaInicio.isBefore(horaComparar.plusMinutes(duracion)));
                    } else {
                        inter1 = horaComparar.isAfter(horaInicio) || horaComparar.isBefore(horaFin);
                        inter2 = horaInicio.isAfter(horaComparar) || horaInicio.isBefore(horaComparar.plusMinutes(duracion));
                    }
                    resultado = inter1 || inter2;
                }

                //System.out.println("Hora inicio " + horaInicio + " Hora fin " + horaFin + " Hora comparar " + horaComparar + " Resultado: " + resultado);
                j++;
            }
            
        }        
        return resultado;
    }

    public boolean vaciarFuncionesShow(String show_id, Buzon buzon) {
        boolean resultado = true;

        File carpeta = new File("./Datos/Shows/Funciones/Show " + show_id);
        File archivo = new File("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt");

        for (File funcion : carpeta.listFiles(File::isDirectory)) {            
            eliminarFuncion(show_id, funcion.getName().replaceAll("Funcion ", ""), buzon);
        }

        synchronized (bloquear(show_id)) {

            if (archivo.exists() && !archivo.delete()) {
                resultado = false;
            }

            if (carpeta.exists() && !carpeta.delete()) {
                resultado = false;
            }

        }
        return resultado;
    }

    public String eliminarFuncion(String show_id, String funcion_id, Buzon buzon) {
        String resultado = "0";        
        synchronized (bloquearFunciones(show_id, funcion_id)) { // Eliminar los archivos de funciones             
            if (new Butaca().liberarButaca(show_id, funcion_id)) {                
                new Pago().cancelarCompra(show_id, funcion_id, buzon);
                File carpeta = new File("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + funcion_id);
                carpeta.delete();
            }
        }

        synchronized (bloquear(show_id)) { // Eliminar del texto de funciones
            String contenido = "";
            try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {
                String linea = bf.readLine();

                while (linea != null) {
                    if (!funcion_id.equals(linea.split(",")[0])) {
                        contenido += linea + "\n";
                    }
                    linea = bf.readLine();
                }

            } catch (IOException e) {
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {
                bw.write(contenido);
                resultado = "1";
            } catch (IOException e) {
            }
        }

        return resultado;
    }

    public String consultarFunciones(String show_id) {
        String funciones = "0";
        synchronized (bloquear(show_id)) {

            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {

                String linea, contenido = "";
                while ((linea = br.readLine()) != null) {
                    contenido += linea + "@";
                }

                if(!contenido.isEmpty()){
                    funciones = contenido.substring(0, contenido.length() - 1);
                }
                

            } catch (IOException e) {
            }
        }

        return funciones;
    }

    public String registrarFuncion(String show_id, String fecha, String horario, String sala, String idioma, String proyeccion) {
        String resultado = "1";
        int nr = 1;
        

        synchronized (bloquear(show_id)) {
            if (!validarInterposicion(show_id, horario, sala, fecha)) {

                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {
                    String linea = bf.readLine();

                    while (linea != null) {
                        nr = Integer.parseInt(linea.split(",")[0]) + 1;
                        linea = bf.readLine();
                    }

                } catch (IOException e) {
                    
                }

                File carpeta = new File("./Datos/Shows/Funciones/Show " + show_id + "/Funcion " + nr);
                carpeta.mkdir();

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt", true))) {
                    bw.write(nr + "," + fecha + "," + horario + "," + sala + "," + idioma + "," + proyeccion + "\n");
                } catch (IOException e) {
                    System.out.println(e);
                }

                if (new Pago().crearArchivoPago(show_id, nr) && new Butaca().crearButacas(show_id, String.valueOf(nr), sala)) {
                    resultado = "0";
                }
            }
        }        

        return resultado;
    }

    public String[] consultarHorario(String show_id, String funcion_id) {
        String[] horario = new String[2];
        boolean encontro = false;

        synchronized (bloquear(show_id)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null && !encontro) {
                    String[] palabras = linea.split(",");

                    if (palabras[0].equals(funcion_id)) {
                        encontro = true;
                        horario[0] = palabras[1];
                        horario[1] = palabras[2];
                    }

                }

            } catch (IOException e) {
            }
        }

        return horario;
    }

    public String consultarSala(String show_id, String funcion_id) {
        String sala = "0";
        boolean encontro = false;

        synchronized (bloquear(show_id)) {
            try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {

                String linea;
                while ((linea = br.readLine()) != null && !encontro) {
                    String[] palabras = linea.split(",");

                    if (palabras[0].equals(funcion_id)) {
                        encontro = true;
                        sala = palabras[3];
                    }

                }

            } catch (IOException e) {
            }
        }

        return sala;

    }

    public String modificarFuncion(String show_id, String funcion_id, String fecha, String horario, String sala, String idioma, String proyeccion, Buzon buzon) {
        String resultado = "1", descripcion = "Se modifico";
        

        synchronized (bloquear(show_id)) {

            if (!validarInterposicion(show_id, funcion_id, horario, sala, fecha)) {
                String linea, contenido = "";

                try (BufferedReader bf = new BufferedReader(new FileReader("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {

                    while ((linea = bf.readLine()) != null) {
                        String[] partes = linea.split(",");

                        if (funcion_id.equals(partes[0])) {
                            contenido += funcion_id + "," + fecha + "," + horario + "," + sala + "," + idioma + "," + proyeccion + "\n";
                            // Describir que se cambio

                            if (!fecha.equals(partes[1])) {
                                descripcion += " la fecha - ";
                            }
                            if (!horario.equals(partes[2])) {
                                descripcion += " el horario -";
                            }
                            if (!sala.equals(partes[3])) {
                                descripcion += " la sala -";
                            }
                            if (!idioma.equals(partes[4])) {
                                descripcion += " el idioma -";
                            }
                            if (!proyeccion.equals(partes[5])) {
                                descripcion += " la proyección -";
                            }
                            
                            if(!descripcion.isEmpty()){
                                descripcion = descripcion.substring(0, descripcion.length() - 1);
                            }
                            
                            descripcion += " de la función.";
                        } else {
                            contenido += linea + "\n";
                        }

                    }

                } catch (IOException e) {
                    System.out.println(e);
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Datos/Shows/Funciones/Show " + show_id + "/Funciones.txt"))) {
                    bw.write(contenido);
                    resultado = "0";
                } catch (IOException e) {
                    resultado = "2";
                }

                new Pago().notificarModificacionFuncion(show_id, funcion_id, descripcion, buzon);
            }
        }        

        return resultado;
    }

}
