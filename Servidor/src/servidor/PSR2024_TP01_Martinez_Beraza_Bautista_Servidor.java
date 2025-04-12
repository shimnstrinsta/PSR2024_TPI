package servidor;

import dato.Parametro;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PSR2024_TP01_Martinez_Beraza_Bautista_Servidor {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(5566);
        ServerSocket notificador = new ServerSocket(5567);
        ServerSocket eliminador = new ServerSocket(5568);
        Parametro parametro = new Parametro();

        while (true) {
            Socket sv = servidor.accept();

            System.out.println("\nHay uno intentando conectarse...\nConectados: " + Servidor.cantidadConectados() + "\nPermitidos: " + parametro.sesionesMaximasServidor() + "\n");

            if (Servidor.cantidadConectados() < parametro.sesionesMaximasServidor()) {                
                Socket nt = notificador.accept();
                Socket el = eliminador.accept(); 
                
                new Thread(new Servidor(sv, nt, el)).start();
            } else {
                try (OutputStreamWriter osw = new OutputStreamWriter(sv.getOutputStream())) {
                    osw.write("2");
                    osw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    sv.close();
                }
            }
        }
    }
}
