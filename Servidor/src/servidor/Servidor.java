package servidor;

import java.net.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import dato.*;

public class Servidor extends Thread {

    private final Socket clientSocket;
    private String usuario;
    private final InputStreamReader isr;
    private final OutputStreamWriter osw;
    private final OutputStreamWriter nt;
    private final OutputStreamWriter el;
    private Buzon buzon;
    private static int cantidadConectados = 0;

    public Servidor(Socket sv, Socket notificador, Socket eliminador) throws IOException {
        clientSocket = sv;

        isr = new InputStreamReader(clientSocket.getInputStream());

        BufferedOutputStream bufferedStream = new BufferedOutputStream(clientSocket.getOutputStream(), 4096);
        BufferedOutputStream bfnt = new BufferedOutputStream(notificador.getOutputStream(), 4096);
        BufferedOutputStream bfel = new BufferedOutputStream(eliminador.getOutputStream(), 4096);
        
        osw = new OutputStreamWriter(bufferedStream, "UTF-8");
        nt = new OutputStreamWriter(bfnt, "UTF-8");
        el = new OutputStreamWriter(bfel, "UTF-8");
    }

    public static synchronized int cantidadConectados() {
        return cantidadConectados;
    }

    public synchronized void conectarCliente() {
        cantidadConectados++;
    }

    private synchronized void desconectarConexion() {
        cantidadConectados--;
        buzon.borrarNotificador();
    }

    private static String buscarClase(String metodo) {
        String clase = "";
        try (BufferedReader br = new BufferedReader(new FileReader("./Datos/Metodos.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String palabras[] = linea.split(",");
                if (palabras[0].equals(metodo)) {
                    clase = palabras[1];
                }
            }
        } catch (IOException e) {
        }
        return clase;
    }

    private String manejarPeticion(String request) {
        try {
            boolean notificar = false;
            if (request.charAt(0) == '#') {
                request = request.replaceAll("#", "");
                notificar = true;
            }

            String[] parts = request.split("@");
            String methodName = parts[0];
            String className = buscarClase(methodName);

            String[] methodArgs = new String[parts.length - 1];
            System.arraycopy(parts, 1, methodArgs, 0, parts.length - 1);

            //System.out.println("Metodo: " + methodName + " Clase: " + className);
            Class<?> clazz = Class.forName("dato." + className);

            Method[] methods = clazz.getDeclaredMethods();
            Method method = null;

            int largo = methodArgs.length;
            if (notificar) {
                largo++;
            }

            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.getParameterCount() == largo) {
                    method = m;
                    break;
                }
            }

            if (method == null) {
                throw new NoSuchMethodException("No se encontró un método con el nombre especificado y el número de parámetros en la clase.");
            }

            Object[] convertedArgs = new Object[largo];

            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < largo; i++) {
                if (parameterTypes[i] == int.class) {
                    convertedArgs[i] = Integer.parseInt(methodArgs[i]);
                } else if (parameterTypes[i] == String.class) {
                    convertedArgs[i] = methodArgs[i];
                } else if (parameterTypes[i] == boolean.class) {
                    convertedArgs[i] = Boolean.valueOf(methodArgs[i]);
                } else if (parameterTypes[i] == Buzon.class) {
                    convertedArgs[i] = buzon;
                }

            }

            Object instance = clazz.getConstructor().newInstance();
            Object result = method.invoke(instance, convertedArgs);

            if (methodName.equals("buscarPerfil") || methodName.equals("registrarPerfil") ) {
                usuario = methodArgs[0];                          
                if (result.equals("4") || result.equals("5")) { // Si se esta creando la cuenta o iniciando sesion se crea el buzon
                    buzon = new Buzon(nt, usuario, el);                    
                }
            }

            return (String) result;

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //System.out.println("Error aaaaaaaaaaaaaaa " + e);
            System.out.println(e.getCause());
            return "1";
        }
    }

    @Override
    public void run() {
        conectarCliente();
        try {
            while (true) {
                char[] mensaje = new char[255];
                isr.read(mensaje, 0, 255);
                String msg = new String(mensaje).trim();
                System.out.println("Estoy recibiendo..." + msg);
                String respuesta;

                respuesta = manejarPeticion(msg);

                osw.write(respuesta);
                osw.flush();
            }

        } catch (IOException | StringIndexOutOfBoundsException e) {
            desconectarConexion();
        } finally {
            try {
                clientSocket.close();
                isr.close();
                osw.close();
            } catch (IOException e) {
            }
        }
    }

}
