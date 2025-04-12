package background;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Eliminador extends Thread {

    @Override
    public void run() {
        try {
            InetAddress ip_host = InetAddress.getLocalHost();

            Socket cliente = new Socket(ip_host.getHostAddress(), 5568);

            InputStreamReader isr = new InputStreamReader(cliente.getInputStream());
            while (true) {
                char[] mensaje = new char[255];

                isr.read(mensaje, 0, 255);
                JOptionPane.showMessageDialog(null, "Su usuario ha sido eliminado", "Aviso", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
