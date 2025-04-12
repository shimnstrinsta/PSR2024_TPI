package background;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Notificador extends Thread {

    @Override
    public void run() {
        try {            
            InetAddress ip_host = InetAddress.getLocalHost();

            Socket cliente = new Socket(ip_host.getHostAddress(), 5567);

            InputStreamReader isr = new InputStreamReader(cliente.getInputStream());
            while (true) {

                char[] mensaje = new char[255];

                isr.read(mensaje, 0, 255);
                String smensaje = new String(mensaje).trim();
                JOptionPane.showMessageDialog(null, smensaje, "Información", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (IOException e) {
            System.out.println(e);
            
        }
    }
}
