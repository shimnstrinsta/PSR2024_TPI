package interfaz;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import persona.Persona;
import persona.Usuario;
import javax.swing.text.MaskFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Random;

public class InterfazUsuario extends Interfaz {

    private final JFrame ventana = new JFrame("Sistema de compra de tickets");
    private static final Hashtable<String, JFrame> lista_ventana_funciones = new Hashtable<>();
    private final Usuario usuario;

    private JFormattedTextField ent_tarjetaField;
    private JFormattedTextField ent_cod_seguridad; // Aux

    // ---------------------------- Inicio ---------------------------- //
    public InterfazUsuario(Persona persona) {

        ventana.setSize(900, 700);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setBackground(Color.WHITE);
        ventana.setVisible(true);
        usuario = new Usuario(persona);

        
        JMenuBar menuBar = new JMenuBar();

        JMenuItem item_shows = new JMenuItem("Shows");
        JMenuItem item_tarjetas = new JMenuItem("Tarjetas");
        JMenuItem item_tickets = new JMenuItem("Compras");
        JMenuItem item_avisos = new JMenuItem("Avisos");

        JMenu men_usuario = new JMenu(usuario.getNombre());
        JMenuItem item_cerrar = new JMenuItem("Cerrar sesión");

        men_usuario.setForeground(Color.WHITE);
        item_shows.setForeground(Color.WHITE);
        item_tarjetas.setForeground(Color.WHITE);
        item_tickets.setForeground(Color.WHITE);
        item_avisos.setForeground(Color.WHITE);

        men_usuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_tarjetas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_shows.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_tickets.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_avisos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item_tarjetas.setMaximumSize(new Dimension(95, 90));
        item_shows.setMaximumSize(new Dimension(80, 90));
        item_avisos.setMaximumSize(new Dimension(600, 90));
        item_tickets.setMaximumSize(new Dimension(90, 90));

        menuBar.add(item_shows);
        menuBar.add(item_tarjetas);
        menuBar.add(item_tickets);
        menuBar.add(item_avisos);

        ImageIcon iconUser = new ImageIcon("./icons/user.png");
        ImageIcon iconShow = new ImageIcon("./icons/show.png");
        ImageIcon iconTarjeta = new ImageIcon("./icons/tarjeta.png");
        ImageIcon iconTicket = new ImageIcon("./icons/ticket.png");
        ImageIcon iconAviso = new ImageIcon("./icons/alert.png");

        men_usuario.setIcon(iconUser);
        item_tarjetas.setIcon(iconTarjeta);
        item_shows.setIcon(iconShow);
        item_tickets.setIcon(iconTicket);
        item_avisos.setIcon(iconAviso);

        men_usuario.setFont(fnt_txt_normal);
        item_tarjetas.setFont(fnt_txt_normal);
        item_shows.setFont(fnt_txt_normal);
        item_tickets.setFont(fnt_txt_normal);
        item_avisos.setFont(fnt_txt_normal);

        men_usuario.setBackground(color_principal);
        item_tarjetas.setBackground(color_principal);
        item_shows.setBackground(color_principal);
        item_tickets.setBackground(color_principal);
        item_avisos.setBackground(color_principal);
        men_usuario.add(item_cerrar);

        menuBar.add(men_usuario);
        menuBar.setBackground(color_principal);
        ventana.setJMenuBar(menuBar);

        consultarShowsUI();

        item_shows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarShowsUI();
            }
        });
        item_tarjetas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarTarjetasUI();
            }
        });
        item_tickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarComprasUI();
            }
        });
        item_avisos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarAvisosUI();
            }
        });
        item_cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion(ventana);
            }
        });

    }

    // ----------------------------- RF03 ----------------------------- //
    private void seleccionarButacas(JFrame ventana_funciones, JLabel lbl_tiempo, String[][] butacas, String show_id, String funcion_id, String id_transaccion) {
        ventana_funciones.getContentPane().removeAll();
        LinkedList<String> butacasComprar = new LinkedList<>();
        JLabel titulo = new JLabel("Butacas ");
        JLabel titulo_tiempo = new JLabel("Tiempo máximo ");

        JButton btn_seleccionar = new JButton("Seleccionar");

        btn_seleccionar.setFocusPainted(false);
        btn_seleccionar.setBackground(color_principal);
        btn_seleccionar.setForeground(Color.white);
        btn_seleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_seleccionar.setEnabled(false);

        JPanel contenedorGrilla = new JPanel(), grilla = new JPanel(new GridLayout());

        contenedorGrilla.setPreferredSize(new Dimension(500, 700));
        contenedorGrilla.setBorder(new CompoundBorder(new EmptyBorder(0, 10, 10, 10), new LineBorder(Color.BLACK, 1)));
        grilla.setBackground(Color.WHITE);
        contenedorGrilla.add(grilla);

        calcularSala(butacas, grilla, butacasComprar, btn_seleccionar);

        titulo.setFont(fnt_titulo);
        titulo_tiempo.setFont(fnt_titulo);
        lbl_tiempo.setFont(fnt_titulo);

        ventana_funciones.add(titulo);
        ventana_funciones.add(contenedorGrilla);
        ventana_funciones.add(btn_seleccionar);
        ventana_funciones.add(titulo_tiempo);
        ventana_funciones.add(lbl_tiempo);

        btn_seleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere comprar las butacas seleccioinadas?", "Confirmación", JOptionPane.YES_NO_OPTION) == 0) {
                    switch (usuario.seleccionarButacas(show_id, funcion_id, butacasComprar, id_transaccion)) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "Ha superado el límite de butacas por compra", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "Alguna de las butacas seleccionadas ya fue ocupada", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 2:
                            confirmarPago(ventana_funciones, lbl_tiempo, id_transaccion, show_id, funcion_id,butacasComprar.size());
                            //lista_ventana_funciones.remove(id_transaccion);
                            //JOptionPane.showMessageDialog(null, "Síii", "Tiempo", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                }
            }
        });
        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        titulo_tiempo.setBorder(new EmptyBorder(20, 0, 0, 0));

        grilla.setAlignmentY(Component.CENTER_ALIGNMENT);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo_tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_seleccionar.setAlignmentX(Component.CENTER_ALIGNMENT);

        ventana_funciones.revalidate();
        ventana_funciones.repaint();
    }

    private void agregarButacaCompra(JButton btn, String butaca, LinkedList<String> butacasComprar, JButton btn_seleccionar) {
        if (!butacasComprar.contains(butaca)) {
            btn.setBackground(Color.GRAY);
            butacasComprar.add(butaca);
        } else {
            btn.setBackground(Color.GREEN);
            butacasComprar.remove(butaca);
        }

        if (!butacasComprar.isEmpty()) {
            btn_seleccionar.setEnabled(true);
        } else {
            btn_seleccionar.setEnabled(false);
        }
    }

    private void confirmarPago(JFrame ventana_funciones, JLabel lbl_tiempo, String id_transaccion, String show_id, String funcion_id,int cantidad_butacas) {
        ventana_funciones.getContentPane().removeAll();

        String[][] tarjetas = usuario.seleccionarTarjetas();
        String[] nroTarjetas = new String[tarjetas.length];

        for (int i = 0; i < nroTarjetas.length; i++) {
            nroTarjetas[i] = tarjetas[i][0];
        }

        JLabel titulo = new JLabel("Pago");
        JLabel lbl_tarjeta = new JLabel("Seleccionar tarjeta para pagar: ");
        JLabel lbl_seguridad = new JLabel("Cd. Seguridad: " + tarjetas[0][2]);
        JLabel lbl_vencimiento = new JLabel("F. Vencimiento: " + tarjetas[0][1]);
        JLabel titulo_tiempo = new JLabel("Tiempo máximo ");
        JLabel lbl_butacas = new JLabel("Cantidad a comprar: "+cantidad_butacas);
        JComboBox cb_tarjeta = new JComboBox(nroTarjetas);
        JPanel contenedor_tarjeta = new JPanel(new BorderLayout()), tarjeta_info = new JPanel(), tarjeta_img = new JPanel();
        JButton btn_confirmar = new JButton("Confirmar pago");

        ImageIcon iconTarjeta = new ImageIcon("./icons/pay2.png");
        JLabel lbl_img = new JLabel();
        lbl_img.setIcon(iconTarjeta);

        btn_confirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_confirmar.setFocusPainted(false);
        btn_confirmar.setForeground(Color.WHITE);
        btn_confirmar.setBackground(color_principal);

        cb_tarjeta.setMaximumSize(new Dimension(150, 20));
        titulo_tiempo.setFont(fnt_titulo);
        titulo.setFont(fnt_titulo);
        lbl_tiempo.setFont(fnt_titulo);
        lbl_tarjeta.setFont(fnt_txt_normal);

        contenedor_tarjeta.add(tarjeta_info, BorderLayout.WEST);
        contenedor_tarjeta.add(tarjeta_img, BorderLayout.EAST);

        contenedor_tarjeta.setBorder(new EmptyBorder(20, 0, 0, 0));
        tarjeta_info.add(cb_tarjeta);
        tarjeta_info.add(lbl_vencimiento);
        tarjeta_info.add(lbl_seguridad);
        tarjeta_info.add(lbl_butacas);

        tarjeta_img.add(lbl_img);
        tarjeta_info.setBorder(new EmptyBorder(20, 20, 0, 0));
        lbl_vencimiento.setBorder(new EmptyBorder(20, 0, 20, 0));
        lbl_seguridad.setBorder(new EmptyBorder(0, 0, 20, 0));

        tarjeta_info.setLayout(new BoxLayout(tarjeta_info, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo_tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_img.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedor_tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);
        cb_tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl_vencimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl_seguridad.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);

        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        titulo_tiempo.setBorder(new EmptyBorder(20, 0, 0, 0));

        cb_tarjeta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) cb_tarjeta.getSelectedItem();

                boolean encontro = false;
                int i = 0;
                while (!encontro) {
                    if (tarjetas[i][0].equals(seleccion)) {
                        encontro = true;
                        lbl_vencimiento.setText("F. Vencimiento: " + tarjetas[i][1]);
                        lbl_seguridad.setText("Cd. Seguridad: " + tarjetas[i][2]);
                    }
                    i++;
                }
            }
        });

        btn_confirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere confirmar el pago?", "Confirmar", JOptionPane.YES_NO_OPTION) == 0) {

                    if (usuario.comprarButacas(show_id, funcion_id, (String) cb_tarjeta.getSelectedItem(), id_transaccion) == 0) {
                        JOptionPane.showMessageDialog(null, "No se pudo completar el pago", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ventana_funciones.dispose();
                        synchronized (lista_ventana_funciones) {
                            lista_ventana_funciones.remove(id_transaccion);
                        }
                    }
                }
            }
        });

        ventana_funciones.add(titulo);
        ventana_funciones.add(lbl_tarjeta);
        ventana_funciones.add(contenedor_tarjeta);
        ventana_funciones.add(btn_confirmar);
        ventana_funciones.add(titulo_tiempo);
        ventana_funciones.add(lbl_tiempo);
        ventana_funciones.revalidate();
        ventana_funciones.repaint();
    }

    // ----------------------------- RF05 ----------------------------- //
    private void consultarTarjetasUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Tarjetas");
        JButton btn_agregar = new JButton("Agregar"), btn_eliminar = new JButton("Eliminar");

        String[] columnas = {"Nro", "Fecha vencimiento", "Cod. Seguridad"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        DefaultTableModel modelo = new DefaultTableModel(usuario.seleccionarTarjetas(), columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        lbl_titulo.setFont(fnt_titulo);

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);
        tabla.setCellSelectionEnabled(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setOpaque(false);
        contenedor.add(lbl_titulo);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(scrollPane);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btns_frm);

        btn_eliminar.setBackground(color_principal);
        btn_eliminar.setForeground(Color.WHITE);
        btn_agregar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);

        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);
        btn_eliminar.setFocusPainted(false);
        btns_frm.add(btn_agregar);
        btns_frm.add(btn_eliminar);

        btn_eliminar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_eliminar.setEnabled(true);
            }
        });

        btn_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tarjeta = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar la tarjeta '" + tarjeta + "'?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    if (usuario.eliminarTarjeta(tarjeta) == '0') {
                        JOptionPane.showMessageDialog(null, "No puede haber menos de 1 tarjeta de crédito cargada", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    consultarTarjetasUI();
                }
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTarjetaUI(columnas);
            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF07 ----------------------------- //
    private void cargarTarjetaUI(String[] columnas) {
        JDialog dialog = new JDialog(ventana, "Agregar tarjeta", true); // `true` para modal
        JPanel contenedor = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_agregar = new JButton("Agregar");
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        contenedor.setLayout(new GridLayout(columnas.length + 1, 2, 10, 10));

        try {
            MaskFormatter m_nro = new MaskFormatter("################");
            m_nro.setPlaceholderCharacter('_');  // Placeholder que se verá cuando no se ha ingresado el dígito
            m_nro.setValidCharacters("0123456789");  // Solo permitir dígitos
            ent_tarjetaField = new JFormattedTextField(m_nro);
            ent_tarjetaField.setColumns(16);  // Espacio para 16 dígitos

            MaskFormatter m_cod = new MaskFormatter("###");
            m_cod.setValidCharacters("0123456789");
            ent_cod_seguridad = new JFormattedTextField(m_cod);
            ent_cod_seguridad.setColumns(3);

        } catch (Exception e) {
            ent_tarjetaField = null;
            ent_cod_seguridad = null;
            e.printStackTrace();
        }

        Calendar calendario = Calendar.getInstance();
        Date fechaActual = calendario.getTime();

        SpinnerDateModel timeModel = new SpinnerDateModel(fechaActual, fechaActual, null, Calendar.DAY_OF_MONTH);
        JSpinner ent_fecha_vencimiento = new JSpinner(timeModel);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha_vencimiento, "dd/MM/yyyy");
        ent_fecha_vencimiento.setEditor(dateEditor);

        contenedor.add(new JLabel(columnas[0]));  // Nro                              
        contenedor.add(ent_tarjetaField);

        contenedor.add(new JLabel(columnas[1]));  // Fecha vencimiento    
        contenedor.add(ent_fecha_vencimiento);

        contenedor.add(new JLabel(columnas[2]));  // Cod. Seguridad                              
        contenedor.add(ent_cod_seguridad);

        contenedor.add(btn_cancelar);
        contenedor.add(btn_agregar);

        btn_agregar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 20, 10));
        dialog.add(contenedor);

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nroTarjeta = (String) ent_tarjetaField.getValue();

                Date fechaSeleccionada = (Date) ent_fecha_vencimiento.getValue();
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                String vencimiento = formatoFecha.format(fechaSeleccionada);

                String seguridad = String.valueOf(ent_cod_seguridad.getValue());

                switch (usuario.cargarTarjeta(nroTarjeta, vencimiento, seguridad)) {
                    case 0:
                        dialog.dispose();
                        consultarTarjetasUI();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "La tarjeta ya está cargada", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Has superado el limite de tarjetas cargables", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(null, "El formato de los datos no es correcto", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }

            }
        });

        dialog.setVisible(true);
    }

    public static void terminarTiempo(String id_ventana) {
        synchronized (lista_ventana_funciones) {
            if (lista_ventana_funciones.containsKey(id_ventana)) {
                lista_ventana_funciones.get(id_ventana).dispose();
                JOptionPane.showMessageDialog(null, "Se acabó el tiempo máximo de compra", "Tiempo", JOptionPane.INFORMATION_MESSAGE);
                lista_ventana_funciones.remove(id_ventana);
            }
        }
    }

    // ----------------------------- RF21 ----------------------------- //
    private void consultarFunciones(String show_id, String show) {
        JFrame ventana_funciones = new JFrame(show);
        ventana_funciones.setLayout(new BoxLayout(ventana_funciones.getContentPane(), BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Funciones");
        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        titulo.setFont(fnt_titulo);
        String[] columnas = {"Fecha", "Horario", "Sala", "Idioma", "Proyección"};
        String[][] datos = usuario.seleccionarFunciones(show_id);

        String[][] funciones = new String[datos.length][5];

        try {
            for (int i = 0; i < funciones.length; i++) {
                for (int j = 0; j < 5; j++) { // Para sacar ids
                    funciones[i][j] = datos[i][j + 1];
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
        }

        DefaultTableModel modelo = new DefaultTableModel(funciones, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        tabla.setPreferredSize(new Dimension(300, 200));

        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);
        tabla.setCellSelectionEnabled(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btn_seleccionar = new JButton("Seleccionar");

        btn_seleccionar.setFocusPainted(false);
        btn_seleccionar.setBackground(color_principal);
        btn_seleccionar.setForeground(Color.white);
        btn_seleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_seleccionar.setEnabled(false);

        ventana_funciones.add(titulo);
        if (datos[0].length > 1) {
            ventana_funciones.add(scrollPane);
        } else {
            JLabel noFuncion = new JLabel("No hay funciones disponibles");
            noFuncion.setFont(fnt_titulo);
            noFuncion.setAlignmentX(Component.CENTER_ALIGNMENT);
            noFuncion.setBorder(new EmptyBorder(0, 0, 20, 0));
            ventana_funciones.add(noFuncion);
        }

        ventana_funciones.add(btn_seleccionar);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_seleccionar.setEnabled(true);
            }
        });

        btn_seleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String funcion_id = "", horario = (String) tabla.getValueAt(tabla.getSelectedRow(), 1);
                int i = 0;

                String[][] butacas = null;
                JLabel lbl_tiempo = new JLabel();
                int numeroRandom = new Random().nextInt(1000000);

                String idVentana = "#" + (lista_ventana_funciones.size() + 1) * numeroRandom * new Random().nextInt(1000000);
                lista_ventana_funciones.put(idVentana, ventana_funciones);
                try {
                    while (funcion_id.equals("") && i < datos.length) {
                        if (horario.equals(datos[i][2])) {
                            funcion_id = datos[i][0];
                        }
                        i++;
                    }
                    butacas = usuario.accederFuncion(show_id, funcion_id, lbl_tiempo, idVentana);
                } catch (ArrayIndexOutOfBoundsException | NullPointerException er) {

                }

                seleccionarButacas(ventana_funciones, lbl_tiempo, butacas, show_id, funcion_id, idVentana);
            }
        });

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_seleccionar.setAlignmentX(Component.CENTER_ALIGNMENT);
        ventana_funciones.setSize(500, 700);
        ventana_funciones.setLocationRelativeTo(null);
        ventana_funciones.setResizable(false);
        ventana_funciones.setBackground(Color.WHITE);
        ventana_funciones.setVisible(true);

    }

    protected void calcularSala(String[][] butacas, JPanel grilla, LinkedList<String> butacasComprar, JButton btn_seleccionar) {

        int i = 0, filas = 0, columnas;
        boolean aux = true;

        while (aux && i < butacas.length) {
            int filaComparar = Integer.parseInt(butacas[i][0]);
            if (filaComparar >= filas) {
                filas = filaComparar;
            } else {
                aux = false;
            }
            i++;
        }

        filas++;
        columnas = butacas.length / filas;

        grilla.setLayout(new GridLayout(filas, columnas, 5, 5));
        for (String[] butaca : butacas) {
            JButton btn_butaca = new JButton();
            btn_butaca.setPreferredSize(new Dimension(20, 20));

            if (butaca[2].equals("Disponible")) {
                btn_butaca.setBackground(Color.GREEN);
            } else {
                btn_butaca.setBackground(Color.RED);
                btn_butaca.setEnabled(false);
            }
            btn_butaca.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarButacaCompra(btn_butaca, (String) (butaca[0] + "," + butaca[1]), butacasComprar, btn_seleccionar);
                }
            });
            btn_butaca.setCursor(new Cursor(Cursor.HAND_CURSOR));
            grilla.add(btn_butaca);
        }

        grilla.revalidate();
        grilla.repaint();

    }

    // ----------------------------- RF22 ----------------------------- //
    private void consultarShowsUI() {
        ventana.getContentPane().removeAll();

        LinkedList<String[]> shows;
        shows = usuario.seleccionarShows();

        ventana.add(Box.createVerticalStrut(20));
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);        
        if (shows.get(0).length > 1) {

            for (String[] show : shows) {                
                JPanel panel = new JPanel(), extra = new JPanel(), info = new JPanel(new BorderLayout());
                extra.setLayout(new BoxLayout(extra, BoxLayout.Y_AXIS));
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel titleLabel = new JLabel(show[1]);
                titleLabel.setFont(fnt_titulo);
                titleLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                panel.add(titleLabel);

                JTextArea descriptionArea = new JTextArea(show[5]);
                descriptionArea.setLineWrap(true);
                descriptionArea.setWrapStyleWord(true);
                descriptionArea.setEditable(false);
                descriptionArea.setPreferredSize(new Dimension(350, 100));

                JLabel direc = new JLabel("Director: " + show[3]);
                extra.add(direc);
                direc.setBorder(new EmptyBorder(0, 0, 20, 0));
                extra.add(new JLabel("Duración: " + show[2] + "min"));

                JLabel fecha = new JLabel("Estreno: " + show[4]);
                extra.add(fecha);
                fecha.setBorder(new EmptyBorder(20, 0, 0, 0));

                JLabel precio = new JLabel("Entrada: $" + show[6]);
                extra.add(precio);
                precio.setBorder(new EmptyBorder(20, 0, 0, 0));

                info.add(descriptionArea, BorderLayout.WEST);
                info.add(extra, BorderLayout.EAST);
                panel.setBackground(Color.white);

                panel.add(info);

                JButton buyButton = new JButton("Comprar");

                buyButton.setFocusPainted(false);
                buyButton.setBackground(color_principal);
                buyButton.setForeground(Color.white);
                buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                panel.add(buyButton);
                panel.setBackground(color_secundario);

                panel.setMaximumSize(new Dimension(550, 750));
                panel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(0, 10, 0, 10)));
                info.setBorder(new EmptyBorder(0, 0, 10, 10));

                panel.setAlignmentX(Component.CENTER_ALIGNMENT);
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                info.setAlignmentX(Component.CENTER_ALIGNMENT);
                contenedor.add(panel);
                contenedor.add(Box.createVerticalStrut(20));

                buyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        consultarFunciones(show[0], show[1]);
                    }
                });
            }
            
            JScrollPane scrollPane = new JScrollPane(contenedor);   
            ventana.add(scrollPane);
        }
        else{
            JLabel no_shows = new JLabel("No hay shows disponibles");
            no_shows.setBorder(new EmptyBorder(30,0,0,0));
            no_shows.setFont(fnt_titulo);
            no_shows.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(no_shows);
            ventana.add(contenedor);
        }        
        
        
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF27 ----------------------------- //
    private void consultarComprasUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Compras");
        JButton btn_cancelar = new JButton("Cancelar compra");

        String[] columnas = {"Show", "Fecha", "Horario", "Sala", "Fila", "Columna", "Tarjeta", "Precio", "Estado"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        String[][] datos = usuario.seleccionarCompras();
        
        
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        lbl_titulo.setFont(fnt_titulo);

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);
        tabla.setCellSelectionEnabled(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setOpaque(false);
        contenedor.add(lbl_titulo);
        contenedor.add(Box.createVerticalStrut(20));

        if (datos[0].length > 1) {
            contenedor.add(scrollPane);
            contenedor.add(Box.createVerticalStrut(20));
            contenedor.add(btns_frm);
        } else {
            JLabel noCompras = new JLabel("No hay compras");
            noCompras.setFont(fnt_titulo);
            noCompras.setAlignmentX(Component.CENTER_ALIGNMENT);
            noCompras.setBorder(new EmptyBorder(20, 0, 20, 0));
            contenedor.add(noCompras);
        }

        btn_cancelar.setBackground(color_principal);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_cancelar.setFocusPainted(false);
        btns_frm.add(btn_cancelar);

        btn_cancelar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(700, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String estado = (String) tabla.getValueAt(tabla.getSelectedRow(), 8);
                if (estado.equals("VIGENTE")) {
                    btn_cancelar.setEnabled(true);
                } else {
                    btn_cancelar.setEnabled(false);
                }
            }
        });

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere cancelar la compra seleccionada?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {

                    String show = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    String fecha = (String) tabla.getValueAt(tabla.getSelectedRow(), 1);
                    String horario = (String) tabla.getValueAt(tabla.getSelectedRow(), 2);
                    String show_id = "", funcion_id = "", butaca;
                    boolean encontro = false;
                    int i = 0;

                    while (!encontro && i < datos.length) {
                        String[] compra = datos[i];
                        if (compra[0].equals(show) && compra[1].equals(fecha) && compra[2].equals(horario)) {
                            show_id = compra[9];
                            funcion_id = compra[10];
                            encontro = true;
                        }
                        i++;
                    }

                    butaca = (String) tabla.getValueAt(tabla.getSelectedRow(), 4) + "," + (String) tabla.getValueAt(tabla.getSelectedRow(), 5);

                    switch (usuario.cancelarCompra(show_id, funcion_id, butaca)) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "No se pudo cancelar el pago por una acción ajena al usuario", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "No se puede cancelar la compra porque ya está vencida", "Error", JOptionPane.ERROR_MESSAGE);
                            consultarComprasUI();
                            break;
                        case 2:
                            consultarComprasUI();
                            break;
                    }
                }
            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF28 ----------------------------- //
    private void consultarAvisosUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), pn_aviso = new JPanel(new BorderLayout());
        JLabel lbl_titulo = new JLabel("Avisos");
        JTextArea lbl_descripcion = new JTextArea("En este apartado se le avisará de alteraciones en sus funciones. Los cambios de horarios y fechas en la función le dará derecho a reclamar.");
        JButton btn_reclamar = new JButton("Reclamar");

        lbl_descripcion.setLineWrap(true);  // Activa el ajuste de línea
        lbl_descripcion.setWrapStyleWord(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] columnas = {"ID", "Show", "Acción"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        String[][] datos = usuario.seleccionarAvisos();

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        lbl_titulo.setFont(fnt_titulo);

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(false);
        tabla.setCellSelectionEnabled(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel columnModel = tabla.getColumnModel();

        // Configurar el tamaño de la primera columna (índice 0)
        TableColumn firstColumn = columnModel.getColumn(0);
        firstColumn.setPreferredWidth(120);  // Tamaño preferido        

        scrollPane.setOpaque(false);

        pn_aviso.add(lbl_descripcion, BorderLayout.WEST);

        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(lbl_titulo);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(pn_aviso);
        contenedor.add(Box.createVerticalStrut(20));

        if (datos[0].length > 1) {
            pn_aviso.add(scrollPane, BorderLayout.EAST);
            contenedor.add(btn_reclamar);
        } else {
            JLabel no_avisos = new JLabel("No hay avisos...");
            no_avisos.setBorder(new EmptyBorder(0, 30, 0, 0));
            no_avisos.setFont(fnt_titulo);
            no_avisos.setAlignmentX(Component.CENTER_ALIGNMENT);
            pn_aviso.add(no_avisos);

        }

        btn_reclamar.setBackground(color_principal);
        btn_reclamar.setForeground(Color.WHITE);

        btn_reclamar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_reclamar.setFocusPainted(false);

        btn_reclamar.setEnabled(false);

        lbl_descripcion.setFont(fnt_txt_normal);
        lbl_descripcion.setBorder(new LineBorder(Color.BLACK, 1));
        lbl_descripcion.setEditable(false);
        lbl_descripcion.setPreferredSize(new Dimension(200, 100));

        contenedor.setMaximumSize(new Dimension(700, 300));

        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn_reclamar.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                for (String[] aviso : datos) {
                    String id = ((String) tabla.getValueAt(tabla.getSelectedRow(), 0));
                    if (id.equals(aviso[0])) {
                        lbl_descripcion.setText(aviso[4]);
                        if (aviso[3].equals("SI")) {
                            btn_reclamar.setEnabled(true);
                        } else {
                            btn_reclamar.setEnabled(false);
                        }
                    }
                }

            }
        });

        btn_reclamar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere realizar un reclamo para la función?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    String id = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    switch (usuario.reclamarAlteracion(id)) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "No se pudo reclamar el pago", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "Ya se reclamo el pago", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "Se realizo el reclamo correspondiente", "Reclamo", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }

                }
            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

}
