package interfaz;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import persona.Persona;

public class Interfaz {

    private final JFrame ventana = new JFrame("Sistema de compra de tickets");

    protected final Font fnt_titulo = new Font("Arial", Font.BOLD, 20);
    protected final Font fnt_txt_normal = new Font("Arial", Font.BOLD, 13);
    protected final Font fnt_txt_pequeño = new Font("Arial", Font.BOLD, 11);
    
    protected final Color color_principal = Color.decode("#1967D2");
    protected final Color color_secundario = Color.decode("#F1F2F3");

    protected DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");    
        
    private Persona persona;
    JFormattedTextField ent_nro_tarjeta;
    JFormattedTextField ent_cod_seguridad;

    public void iniciar() {

        ventana.setSize(400, 500);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBackground(Color.white);

        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        iniciarSesionUI();
        ventana.setVisible(true);

        
        persona = new Persona();
    }
    
    // ----------------------------- Multi ----------------------------- //
    protected void actualizarTabla(DefaultTableModel modelo, String[][] filas) {
        modelo.setRowCount(0);
        for (String[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    protected void calcularSala(int fila, int columna, JLabel cap, JPanel grilla) {

        grilla.setLayout(new GridLayout(fila, columna, 5, 5));
        grilla.removeAll();
        int resultado = fila * columna;
        SwingUtilities.invokeLater(() -> cap.setText(String.valueOf(resultado)));
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                JLabel butaca = new JLabel();
                butaca.setPreferredSize(new Dimension(20, 20));
                butaca.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(2, 2, 2, 2)));
                grilla.add(butaca);
            }
        }

        grilla.revalidate();
        grilla.repaint();

    }

    protected void cerrarSesion(JFrame ventana) {
        if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cerrar sesión?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
            System.exit(0);
        }
    }

    // ----------------------------- RF01 ----------------------------- //
    private void iniciarSesionUI() {
        ventana.getContentPane().removeAll();
        ventana.revalidate();
        ventana.repaint();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposición vertical

        JLabel lbl_titulo = new JLabel("Iniciar sesión");
        JLabel lbl_nombre = new JLabel("Nombre");
        JTextField ent_nombre = new JTextField(20); // Limitar el ancho del JTextField
        JLabel lbl_contrasenia = new JLabel("Contraseña");
        JPasswordField ent_contrasenia = new JPasswordField(5); // Limitar el ancho del JTextField
        JButton btn_iniciar_sesion = new JButton("Iniciar sesión");
        JLabel lbl_crear_cuenta = new JLabel("¿No tiene cuenta?");
        JButton btn_crear_cuenta = new JButton("Crear cuenta");

        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_contrasenia.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_contrasenia.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_iniciar_sesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_crear_cuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_crear_cuenta.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn_crear_cuenta.setBorderPainted(false);       // No pintar el borde
        btn_crear_cuenta.setContentAreaFilled(false);   // No rellenar el área del contenido
        btn_crear_cuenta.setFocusPainted(false);        // No pintar el borde de enfoque
        btn_crear_cuenta.setOpaque(false);              // Hacer el botón transparente

        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_titulo);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_nombre);
        panel.add(ent_nombre);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_contrasenia);
        panel.add(ent_contrasenia);
        panel.add(Box.createVerticalStrut(30)); // Añadir espacio vertical
        panel.add(btn_iniciar_sesion);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_crear_cuenta);
        panel.add(btn_crear_cuenta);

        panel.setBackground(Color.WHITE);
        panel.setMinimumSize(new Dimension(100, 200));
        ent_nombre.setMaximumSize(new Dimension(200, 25));
        ent_contrasenia.setMaximumSize(new Dimension(200, 25));

        lbl_titulo.setFont(fnt_titulo);
        lbl_nombre.setFont(fnt_txt_normal);
        lbl_contrasenia.setFont(fnt_txt_normal);
        ent_nombre.setFont(fnt_txt_normal);
        ent_contrasenia.setFont(fnt_txt_normal);
        lbl_crear_cuenta.setFont(fnt_txt_pequeño);
        btn_crear_cuenta.setFont(fnt_txt_pequeño);

        btn_iniciar_sesion.setBackground(color_principal);
        btn_iniciar_sesion.setForeground(Color.white);
        btn_crear_cuenta.setForeground(color_principal);
        btn_crear_cuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_iniciar_sesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ventana.add(panel);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        btn_crear_cuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearCuentaUI();
            }
        });

        btn_iniciar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion(ent_nombre.getText(), ent_contrasenia.getText());
            }
        });

    }

    private void iniciarSesion(String nombre, String contrasenia) {

        int respuesta = persona.iniciarSesion(nombre, contrasenia);

        switch (respuesta) {
            case 0:
                JOptionPane.showMessageDialog(null, "El formato de los datos no es válido", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "El servidor no acepta más conexiones", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "No hay ningún perfil que coincida con esas credenciales", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 4:
                ventana.dispose();
                new InterfazUsuario(persona);
                break;
            case 5:
                ventana.dispose();
                new InterfazAdmin(persona);
                break;
        }

    }

    // ----------------------------- RF06 ----------------------------- //
    private void crearCuentaUI() {

        ventana.getContentPane().removeAll();
        ventana.revalidate();
        ventana.repaint();
        ventana.setBackground(Color.white);

        JPanel panel = new JPanel(), pn_tarjeta = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposición vertical

        JLabel lbl_titulo = new JLabel("Crear cuenta");
        JLabel lbl_nombre = new JLabel("Nombre");
        JTextField ent_nombre = new JTextField();
        JLabel lbl_contrasenia = new JLabel("Contraseña");
        JPasswordField ent_contrasenia = new JPasswordField();
        JButton btn_iniciar_sesion = new JButton("Iniciar sesión");
        JButton btn_crear_cuenta = new JButton("Crear cuenta");

        JLabel lbl_tarjeta = new JLabel("Tarjeta");
        JLabel lbl_nro_tarjeta = new JLabel("Nro de tarjeta");
        JLabel lbl_cd_seguridad = new JLabel("Cd. de seguridad");
        JLabel lbl_f_vencimiento = new JLabel("Fecha vencimiento");

        ent_nro_tarjeta = null;
        ent_cod_seguridad = null;
        try {
            MaskFormatter m_nro = new MaskFormatter("################");
            m_nro.setPlaceholderCharacter('_');  // Placeholder que se verá cuando no se ha ingresado el dígito
            m_nro.setValidCharacters("0123456789");  // Solo permitir dígitos
            ent_nro_tarjeta = new JFormattedTextField(m_nro);
            ent_nro_tarjeta.setColumns(16);  // Espacio para 16 dígitos

            MaskFormatter m_cod = new MaskFormatter("###");
            m_cod.setValidCharacters("0123456789");
            ent_cod_seguridad = new JFormattedTextField(m_cod);
            ent_cod_seguridad.setColumns(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner ent_fecha_vencimiento = new JSpinner(timeModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha_vencimiento, "dd/MM/yyyy");
        ent_fecha_vencimiento.setEditor(dateEditor);

        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_contrasenia.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_contrasenia.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_iniciar_sesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_crear_cuenta.setAlignmentX(Component.CENTER_ALIGNMENT);

        lbl_tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_nro_tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_nro_tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_f_vencimiento.setAlignmentX(Component.CENTER_ALIGNMENT);
        ent_fecha_vencimiento.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_cd_seguridad.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn_iniciar_sesion.setBorderPainted(false);       // No pintar el borde
        btn_iniciar_sesion.setContentAreaFilled(false);   // No rellenar el área del contenido
        btn_iniciar_sesion.setFocusPainted(false);        // No pintar el borde de enfoque
        btn_iniciar_sesion.setOpaque(false);              // Hacer el botón transparente

        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_titulo);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_nombre);
        panel.add(ent_nombre);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(lbl_contrasenia);
        panel.add(ent_contrasenia);
        panel.add(Box.createVerticalStrut(30)); // Añadir espacio vertical
        panel.add(lbl_tarjeta);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(pn_tarjeta);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical
        panel.add(btn_crear_cuenta);
        panel.add(Box.createVerticalStrut(20)); // Añadir espacio vertical        
        panel.add(btn_iniciar_sesion);

        pn_tarjeta.add(lbl_nro_tarjeta);
        pn_tarjeta.add(ent_nro_tarjeta);
        pn_tarjeta.add(lbl_cd_seguridad);
        pn_tarjeta.add(ent_cod_seguridad);
        pn_tarjeta.add(lbl_f_vencimiento);
        pn_tarjeta.add(ent_fecha_vencimiento);

        pn_tarjeta.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        panel.setMinimumSize(new Dimension(100, 200));
        ent_nombre.setMaximumSize(new Dimension(200, 25));
        ent_contrasenia.setMaximumSize(new Dimension(200, 25));

        lbl_titulo.setFont(fnt_titulo);
        lbl_nombre.setFont(fnt_txt_normal);
        lbl_contrasenia.setFont(fnt_txt_normal);
        ent_nombre.setFont(fnt_txt_normal);
        ent_contrasenia.setFont(fnt_txt_normal);
        lbl_tarjeta.setFont(fnt_titulo);
        lbl_nro_tarjeta.setFont(fnt_txt_normal);
        lbl_cd_seguridad.setFont(fnt_txt_normal);
        lbl_f_vencimiento.setFont(fnt_txt_normal);
        ent_nro_tarjeta.setFont(fnt_txt_normal);
        ent_cod_seguridad.setFont(fnt_txt_normal);
        ent_fecha_vencimiento.setFont(fnt_txt_normal);

        btn_iniciar_sesion.setFont(fnt_txt_pequeño);

        btn_crear_cuenta.setBackground(color_principal);
        btn_crear_cuenta.setForeground(Color.white);
        btn_iniciar_sesion.setForeground(color_principal);
        btn_crear_cuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_iniciar_sesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn_iniciar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesionUI();
            }
        });

        btn_crear_cuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String estreno = sdf.format(ent_fecha_vencimiento.getValue());
                crearCuenta((String) ent_nombre.getText(), (String) ent_contrasenia.getText(), (String) ent_nro_tarjeta.getValue(), estreno, (String) ent_cod_seguridad.getValue());
            }
        });

        ventana.add(panel);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

    }

    private void crearCuenta(String nombre, String contrasenia, String nroTarjeta, String vencimiento, String seguridad) {

        int respuesta = persona.crearCuenta(nombre, contrasenia, nroTarjeta, vencimiento, seguridad);

        switch (respuesta) {
            case 0:
                JOptionPane.showMessageDialog(null, "El formato de los datos no es válido", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "El servidor no acepta más conexiones\nIntente de nuevo más tarde", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Ya hay un perfil que coincide con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case 4:
                ventana.dispose();
                new InterfazUsuario(persona);
                break;
        }

    }

}
