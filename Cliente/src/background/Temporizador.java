package background;

import interfaz.InterfazUsuario;
import javax.swing.JLabel;
import java.time.Duration;
import persona.Usuario;

public class Temporizador extends Thread {

    private int tiempoMax;
    private final JLabel lbl_tiempo;
    private final String id_ventana;
    private final String show_id;
    private final String funcion_id;
    private boolean termino = false;
    private final Usuario usuario;

    public Temporizador(int tiempoMax, JLabel lbl_tiempo,String id_ventana,String show_id,String funcion_id,Usuario usuario) {
        this.tiempoMax = tiempoMax;
        this.lbl_tiempo = lbl_tiempo;
        this.id_ventana = id_ventana;
        this.funcion_id = funcion_id;
        this.show_id = show_id;
        this.usuario = usuario;
    }


    @Override
    public void run() {
        tiempoMax--;
        int segundos = 60;
        while (!termino) {
            try {
                Thread.sleep(1000);
                segundos--;

                if (segundos == -1) {
                    segundos = 59;
                    tiempoMax--;
                }
                if (tiempoMax == 0 && segundos == 0) {
                    termino = true;
                    usuario.cancelarPago(show_id,funcion_id, id_ventana); // Si pasa el tiempo desocupar las butacas compradas
                    InterfazUsuario.terminarTiempo(id_ventana);                    
                }

                lbl_tiempo.setText(String.format("%02d", tiempoMax) + ":" + String.format("%02d", segundos));
            } catch (InterruptedException e) {                           
                Thread.currentThread().interrupt();                
                termino = true;
            }
        }
    }
}
