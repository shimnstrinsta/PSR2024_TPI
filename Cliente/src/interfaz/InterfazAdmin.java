package interfaz;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.ParseException;
import persona.Administrador;
import persona.Persona;
import java.util.LinkedList;

public class InterfazAdmin extends Interfaz {

    private final JFrame ventana = new JFrame("Sistema de compra de tickets");
    private final Administrador admin;

    private final String[] idiomas = {"Subtitulada", "Doblada"};
    private final String[] proyecciones = {"2D", "3D", "4D"};

    // ---------------------------- Inicio ---------------------------- //
    public InterfazAdmin(Persona persona) {
        admin = new Administrador(persona);
        ventana.setSize(900, 700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setBackground(Color.WHITE);
        ventana.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        JMenuItem item_shows = new JMenuItem("Shows");
        JMenuItem item_salas = new JMenuItem("Salas");
        JMenuItem item_usuarios = new JMenuItem("Usuarios");
        JMenuItem item_parametros = new JMenuItem("Servidor");

        JMenu men_usuario = new JMenu(admin.getNombre());
        JMenuItem item_cerrar = new JMenuItem("Cerrar sesión");

        men_usuario.setForeground(Color.WHITE);
        item_shows.setForeground(Color.WHITE);
        item_salas.setForeground(Color.WHITE);
        item_usuarios.setForeground(Color.WHITE);
        item_parametros.setForeground(Color.WHITE);

        men_usuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_shows.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_salas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_usuarios.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item_parametros.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item_salas.setMaximumSize(new Dimension(85, 90));
        item_usuarios.setMaximumSize(new Dimension(95, 90));
        item_parametros.setMaximumSize(new Dimension(600, 90));
        item_shows.setMaximumSize(new Dimension(80, 90));

        menuBar.add(item_shows);
        menuBar.add(item_salas);
        menuBar.add(item_usuarios);
        menuBar.add(item_parametros);

        ImageIcon iconShow = new ImageIcon("./icons/show.png");
        ImageIcon iconSalas = new ImageIcon("./icons/room.png");
        ImageIcon iconUsuarios = new ImageIcon("./icons/user.png");
        ImageIcon iconPararmetros = new ImageIcon("./icons/param.png");
        ImageIcon iconAdmin = new ImageIcon("./icons/admin.png");

        item_shows.setIcon(iconShow);
        item_salas.setIcon(iconSalas);
        item_parametros.setIcon(iconPararmetros);
        item_usuarios.setIcon(iconUsuarios);
        men_usuario.setIcon(iconAdmin);

        item_shows.setFont(fnt_txt_normal);
        item_salas.setFont(fnt_txt_normal);
        item_parametros.setFont(fnt_txt_normal);
        item_usuarios.setFont(fnt_txt_normal);
        men_usuario.setFont(fnt_txt_normal);
        item_cerrar.setFont(fnt_txt_normal);

        item_shows.setBackground(color_principal);
        item_salas.setBackground(color_principal);
        item_parametros.setBackground(color_principal);
        item_usuarios.setBackground(color_principal);
        men_usuario.setBackground(color_principal);

        menuBar.setBackground(color_principal);
        men_usuario.add(item_cerrar);

        menuBar.add(men_usuario);

        ventana.setJMenuBar(menuBar);

        item_shows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarShowsUI();
            }
        });

        item_usuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarPerfilesUI();
            }
        });
        item_salas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarSalasUI();
            }
        });
        item_parametros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarParametrosUI();
            }
        });
        item_cerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion(ventana);
            }
        });

        consultarShowsUI();
    }

    // ----------------------------- RF09 ----------------------------- //
    private void agregarSalaUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel con_mod = new JPanel(), contenedor = new JPanel(), frm_datos = new JPanel(new BorderLayout()), frm_tbl = new JPanel(), frm_mod = new JPanel();
        //frm_mod.setLayout(new BoxLayout(frm_mod,BoxLayout.Y_AXIS));
        frm_mod.setLayout(new GridLayout(4, 2, 10, 50));
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        JLabel lbl_titulo = new JLabel("Agregar sala");
        JButton btn_agregar = new JButton("Agregar");

        String[] campos = {"Filas", "Columnas", "Capacidad máxima"};
        SpinnerNumberModel filaModel = new SpinnerNumberModel(1, 1, 18, 1); // Inicial: 5, Mínimo: 0, Máximo: 10, Incremento: 1
        SpinnerNumberModel columnaModel = new SpinnerNumberModel(1, 1, 18, 1); // Inicial: 5, Mínimo: 0, Máximo: 10, Incremento: 1
        JSpinner sfila = new JSpinner(filaModel);
        JSpinner scolumna = new JSpinner(columnaModel);
        JLabel cap_maxima = new JLabel("1");

        JPanel grillaButacas = new JPanel();

        sfila.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calcularSala((int) sfila.getValue(), (int) scolumna.getValue(), cap_maxima, grillaButacas);
            }
        });
        scolumna.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calcularSala((int) sfila.getValue(), (int) scolumna.getValue(), cap_maxima, grillaButacas);
            }
        });

        frm_mod.add(new JLabel(campos[0]));
        frm_mod.add(sfila);
        frm_mod.add(new JLabel(campos[1]));
        frm_mod.add(scolumna);
        frm_mod.add(new JLabel(campos[2]));
        frm_mod.add(cap_maxima);

        sfila.setPreferredSize(new Dimension(30, 20));
        scolumna.setPreferredSize(new Dimension(30, 20));

        con_mod.setBorder(new EmptyBorder(10, 30, 0, 0));
        frm_mod.add(btn_agregar);
        con_mod.add(frm_mod);
        frm_mod.setMaximumSize(new Dimension(20, 200));

        lbl_titulo.setFont(fnt_titulo);
        frm_tbl.setPreferredSize(new Dimension(600, 500));

        frm_tbl.add(grillaButacas);

        frm_tbl.setBackground(Color.decode("#FFFFFF"));
        frm_tbl.setBorder(new LineBorder(Color.BLACK, 1));

        frm_datos.add(frm_tbl, BorderLayout.CENTER);
        frm_datos.add(con_mod, BorderLayout.EAST);

        contenedor.add(lbl_titulo);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(frm_datos);

        btn_agregar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);

        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);

        contenedor.setMaximumSize(new Dimension(800, 500));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        grillaButacas.setAlignmentX(Component.CENTER_ALIGNMENT);
        grillaButacas.setAlignmentY(Component.CENTER_ALIGNMENT);

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int resultado = admin.cargarSala((int) sfila.getValue(), (int) scolumna.getValue(), Integer.parseInt(cap_maxima.getText()));
                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar la sala", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    consultarSalasUI();
                }

            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();
    }

    // ----------------------------- RF10 ----------------------------- //    
    private void modificarSalaUI(String numero, int fila, int columna) {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel con_mod = new JPanel(), contenedor = new JPanel(), frm_datos = new JPanel(new BorderLayout()), frm_tbl = new JPanel(), frm_mod = new JPanel();
        //frm_mod.setLayout(new BoxLayout(frm_mod,BoxLayout.Y_AXIS));
        frm_mod.setLayout(new GridLayout(4, 2, 10, 50));
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        JLabel lbl_titulo = new JLabel("Sala " + numero);
        JButton btn_modificar = new JButton("Modificar");

        String[] campos = {"Filas", "Columnas", "Capacidad máxima"};
        SpinnerNumberModel filaModel = new SpinnerNumberModel(1, 1, 18, 1); // Inicial: 5, Mínimo: 0, Máximo: 10, Incremento: 1
        SpinnerNumberModel columnaModel = new SpinnerNumberModel(1, 1, 18, 1); // Inicial: 5, Mínimo: 0, Máximo: 10, Incremento: 1
        JSpinner sfila = new JSpinner(filaModel);
        JSpinner scolumna = new JSpinner(columnaModel);
        JLabel cap_maxima = new JLabel();

        JPanel grillaButacas = new JPanel();

        sfila.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calcularSala((int) sfila.getValue(), (int) scolumna.getValue(), cap_maxima, grillaButacas);
            }
        });
        scolumna.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calcularSala((int) sfila.getValue(), (int) scolumna.getValue(), cap_maxima, grillaButacas);
            }
        });

        sfila.setValue(fila);
        scolumna.setValue(columna);

        frm_mod.add(new JLabel(campos[0]));
        frm_mod.add(sfila);
        frm_mod.add(new JLabel(campos[1]));
        frm_mod.add(scolumna);
        frm_mod.add(new JLabel(campos[2]));
        frm_mod.add(cap_maxima);

        sfila.setPreferredSize(new Dimension(30, 20));
        scolumna.setPreferredSize(new Dimension(30, 20));

        con_mod.setBorder(new EmptyBorder(10, 30, 0, 0));
        frm_mod.add(btn_modificar);
        con_mod.add(frm_mod);
        frm_mod.setMaximumSize(new Dimension(20, 200));

        lbl_titulo.setFont(fnt_titulo);
        frm_tbl.setPreferredSize(new Dimension(600, 500));

        frm_tbl.add(grillaButacas);

        frm_tbl.setBackground(Color.decode("#FFFFFF"));
        frm_tbl.setBorder(new LineBorder(Color.BLACK, 1));

        frm_datos.add(frm_tbl, BorderLayout.CENTER);
        frm_datos.add(con_mod, BorderLayout.EAST);

        contenedor.add(lbl_titulo);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(frm_datos);

        btn_modificar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);

        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setFocusPainted(false);

        contenedor.setMaximumSize(new Dimension(800, 500));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        grillaButacas.setAlignmentX(Component.CENTER_ALIGNMENT);
        grillaButacas.setAlignmentY(Component.CENTER_ALIGNMENT);

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (admin.modificarSala(Integer.parseInt(numero), (int) sfila.getValue(), (int) scolumna.getValue(), Integer.parseInt((String) cap_maxima.getText())) == 0) {
                    JOptionPane.showMessageDialog(null, "La sala tiene asignada funciones, no es posible modificarla", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    consultarSalasUI();
                }

            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();
    }

    // ----------------------------- RF12 ----------------------------- //  
    private void cargarShowUI(String[] columnas) {
        JDialog dialog = new JDialog(ventana, "Agregar show", true);
        JPanel contenedor = new JPanel(), btns = new JPanel(), a = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_agregar = new JButton("Agregar");
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        contenedor.setLayout(new GridLayout(columnas.length, 2, 10, 10));

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        SpinnerNumberModel duracionModel = new SpinnerNumberModel(60, 60, 1000, 1);
        SpinnerNumberModel precioModel = new SpinnerNumberModel(1, 1, 100000, 1);

        JTextField ent_show = new JTextField();
        contenedor.add(new JLabel(columnas[1]));
        contenedor.add(ent_show);

        JSpinner ent_duracion = new JSpinner(duracionModel);
        contenedor.add(new JLabel(columnas[2] + "(min)"));
        contenedor.add(ent_duracion);

        JTextField ent_direc = new JTextField();
        contenedor.add(new JLabel(columnas[3]));
        contenedor.add(ent_direc);

        JSpinner ent_precio = new JSpinner(precioModel);
        contenedor.add(new JLabel(columnas[6] + "$"));
        contenedor.add(ent_precio);

        JSpinner ent_fecha_estreno = new JSpinner(timeModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha_estreno, "dd/MM/yyyy");
        ent_fecha_estreno.setEditor(dateEditor);
        ent_fecha_estreno.setValue(new Date());
        contenedor.add(new JLabel(columnas[4]));
        contenedor.add(ent_fecha_estreno);

        JTextArea descripcion = new JTextArea();
        descripcion.setLineWrap(true);  // Activa el ajuste de línea
        descripcion.setWrapStyleWord(true);

        contenedor.add(new JLabel(columnas[5]));

        descripcion.setPreferredSize(new Dimension(370, 150));
        a.add(descripcion);

        dialog.add(contenedor);
        dialog.add(a);
        dialog.add(btns);

        descripcion.setBorder(new LineBorder(Color.decode("#7A8A99"), 1));

        btns.add(btn_cancelar);
        btns.add(btn_agregar);

        btn_agregar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 5, 10));

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String estreno = sdf.format(ent_fecha_estreno.getValue());

                switch (admin.cargarShow((String) ent_show.getText(), (int) ent_duracion.getValue(), ent_direc.getText(), estreno, descripcion.getText(), (int) ent_precio.getValue())) {
                    case 0:
                        dialog.dispose();
                        consultarShowsUI();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Los datos no cumplen el formato", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "El nombre de show ya está en uso", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }

            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF13 ----------------------------- //    
    private void modificarShowUI(String[] columnas, String[] show) {
        JDialog dialog = new JDialog(ventana, "Modificar show", true);
        JPanel contenedor = new JPanel(), btns = new JPanel(), a = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_modificar = new JButton("Modificar");
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        contenedor.setLayout(new GridLayout(columnas.length, 2, 10, 10));

        SpinnerNumberModel duracionModel = new SpinnerNumberModel(60, 60, 1000, 1);
        SpinnerNumberModel precioModel = new SpinnerNumberModel(1, 1, 1000000, 1);

        JTextField ent_show = new JTextField();
        ent_show.setText(show[1]);
        contenedor.add(new JLabel(columnas[1]));
        contenedor.add(ent_show);

        JSpinner ent_duracion = new JSpinner(duracionModel);
        ent_duracion.setValue(Integer.parseInt(show[2]));
        contenedor.add(new JLabel(columnas[2] + "(min)"));
        contenedor.add(ent_duracion);

        JTextField ent_direc = new JTextField();
        contenedor.add(new JLabel(columnas[3]));
        ent_direc.setText(show[3]);
        contenedor.add(ent_direc);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = dateFormat.parse(show[4]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SpinnerDateModel timeModel = new SpinnerDateModel(fecha, null, null, Calendar.DAY_OF_MONTH);
        JSpinner ent_fecha_estreno = new JSpinner(timeModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha_estreno, "dd/MM/yyyy");
        ent_fecha_estreno.setEditor(dateEditor);
        contenedor.add(new JLabel(columnas[4]));
        contenedor.add(ent_fecha_estreno);

        JSpinner ent_precio = new JSpinner(precioModel);
        contenedor.add(new JLabel(columnas[6] + "$"));
        ent_precio.setValue(Integer.parseInt(show[6]));
        contenedor.add(ent_precio);

        JTextArea descripcion = new JTextArea();
        descripcion.setText(show[5]);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);

        contenedor.add(new JLabel(columnas[5]));

        descripcion.setPreferredSize(new Dimension(370, 150));
        a.add(descripcion);

        dialog.add(contenedor);
        dialog.add(a);
        dialog.add(btns);

        //a.setBorder(new EmptyBorder(0,10,0,10));
        descripcion.setBorder(new LineBorder(Color.decode("#7A8A99"), 1));

        btns.add(btn_cancelar);
        btns.add(btn_modificar);

        btn_modificar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 5, 10));

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String estreno = sdf.format(ent_fecha_estreno.getValue());
                boolean cambioHorario = false, cambioNombre = false;

                if (Integer.parseInt(show[2]) != (int) ent_duracion.getValue()) {
                    cambioHorario = true;
                }

                if (!show[1].equals((String) ent_show.getText())) {
                    cambioNombre = true;
                }

                switch (admin.modificarShow(Integer.parseInt(show[0]), (String) ent_show.getText(), (int) ent_duracion.getValue(), ent_direc.getText(), estreno, descripcion.getText(), (int) ent_precio.getValue(), cambioHorario, cambioNombre)) {
                    case 0:
                        dialog.dispose();
                        consultarShowsUI();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Los datos no cumplen el formato", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "El nombre de show ya está cargado en otro show", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(null, "La nueva duración interpone los horarios de las funciones", "Error", JOptionPane.ERROR_MESSAGE);
                        break;

                }

            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF15 ----------------------------- // 
    private void agregarFuncionUI(String[] columnas, String show_id, String show) {
        JDialog dialog = new JDialog(ventana, "Agregar función", true);
        JPanel contenedor = new JPanel(), btns = new JPanel(), a = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_agregar = new JButton("Agregar");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        String[][] salas = admin.seleccionarSalas();
        String[] nroSalas = new String[salas.length];

        String fecha_minima = admin.seleccionarFechaEstreno(show_id);

        for (int i = 0; i < salas.length; i++) {
            nroSalas[i] = salas[i][0];
        }

        Date fechaMinimaDate = null;
        try {
            fechaMinimaDate = new SimpleDateFormat("dd/MM/yyyy").parse(fecha_minima);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");

        SpinnerDateModel dateModel = new SpinnerDateModel(fechaMinimaDate, fechaMinimaDate, null, Calendar.DAY_OF_MONTH);
        JSpinner ent_fecha = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha, "dd/MM/yyyy");
        ent_fecha.setEditor(dateEditor);

        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        JComboBox cb_proyecciones = new JComboBox(proyecciones);
        JComboBox cb_idiomas = new JComboBox(idiomas);
        JComboBox cb_salas = new JComboBox(nroSalas);

        contenedor.add(new JLabel(columnas[1]));
        contenedor.add(ent_fecha);

        contenedor.add(new JLabel(columnas[2]));
        contenedor.add(timeSpinner);

        contenedor.add(new JLabel(columnas[3]));
        contenedor.add(cb_salas);

        contenedor.add(new JLabel(columnas[4]));
        contenedor.add(cb_idiomas);

        contenedor.add(new JLabel(columnas[5]));
        contenedor.add(cb_proyecciones);

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        contenedor.setLayout(new GridLayout(columnas.length, 2, 10, 10));

        dialog.add(contenedor);
        dialog.add(btns);

        btns.add(btn_cancelar);
        btns.add(btn_agregar);

        btn_agregar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 10, 10));

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date horaSeleccionada = (Date) timeSpinner.getValue();
                Date fechaSeleccionada = (Date) ent_fecha.getValue();

                String fecha = new SimpleDateFormat("dd/MM/yyyy").format(fechaSeleccionada);
                String horario = new SimpleDateFormat("HH:mm").format(horaSeleccionada);

                String sala = (String) cb_salas.getSelectedItem(), idioma = (String) cb_idiomas.getSelectedItem(), proyeccion = (String) cb_proyecciones.getSelectedItem();

                switch (admin.cargarFuncion(show_id, fecha, horario, sala, idioma, proyeccion)) {
                    case 0:
                        dialog.dispose();
                        consultarFuncionesUI(show, show_id);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "La función se interpone con otra de la misma sala", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF16 ----------------------------- //
    private void modificarFuncionUI(String[] columnas, String[] funcion, String show_id, String show) {
        JDialog dialog = new JDialog(ventana, "Modificar show", true);
        JPanel contenedor = new JPanel(), btns = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_modificar = new JButton("Modificar");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        String[][] salas = admin.seleccionarSalas(); // Borrar
        String[] nroSalas = new String[salas.length];

        for (int i = 0; i < salas.length; i++) {
            nroSalas[i] = salas[i][0];
        }

        String fecha_minima = admin.seleccionarFechaEstreno(show_id);
        Date fechaMinimaDate = null;
        try {
            fechaMinimaDate = new SimpleDateFormat("dd/MM/yyyy").parse(fecha_minima);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(timeModel);

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        SpinnerDateModel dateModel = new SpinnerDateModel(fechaMinimaDate, fechaMinimaDate, null, Calendar.DAY_OF_MONTH);
        JSpinner ent_fecha = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(ent_fecha, "dd/MM/yyyy");
        ent_fecha.setEditor(dateEditor);

        JComboBox cb_proyecciones = new JComboBox(proyecciones);
        cb_proyecciones.setSelectedItem(funcion[4]);
        JComboBox cb_idiomas = new JComboBox(idiomas);
        cb_idiomas.setSelectedItem(funcion[3]);
        JComboBox cb_salas = new JComboBox(nroSalas);
        cb_salas.setSelectedItem(funcion[2]);

        contenedor.add(new JLabel(columnas[1]));
        contenedor.add(ent_fecha);

        contenedor.add(new JLabel(columnas[2]));
        contenedor.add(timeSpinner);

        contenedor.add(new JLabel(columnas[3]));
        contenedor.add(cb_salas);

        contenedor.add(new JLabel(columnas[4]));
        contenedor.add(cb_idiomas);

        contenedor.add(new JLabel(columnas[5]));
        contenedor.add(cb_proyecciones);

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        contenedor.setLayout(new GridLayout(columnas.length, 2, 10, 10));

        dialog.add(contenedor);
        dialog.add(btns);

        btns.add(btn_cancelar);
        btns.add(btn_modificar);

        btn_modificar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 20, 10));

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date horaSeleccionada = (Date) timeSpinner.getValue();
                Date fechaSeleccionada = (Date) ent_fecha.getValue();

                String fecha = new SimpleDateFormat("dd/MM/yyyy").format(fechaSeleccionada);
                String horario = new SimpleDateFormat("HH:mm").format(horaSeleccionada);

                String sala = (String) cb_salas.getSelectedItem(), idioma = (String) cb_idiomas.getSelectedItem(), proyeccion = (String) cb_proyecciones.getSelectedItem();

                switch (admin.modificarFuncion(show_id, funcion[0], fecha, horario, sala, idioma, proyeccion)) {
                    case 0:
                        dialog.dispose();
                        consultarFuncionesUI(show, show_id);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "La función se interpone con otra de la misma sala", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "No se pudo realizar la operación", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF18 ----------------------------- //
    private void modificarParametrosUI(String[] param) {
        JDialog dialog = new JDialog(ventana, "Modificar parametro", true); // `true` para modal
        JPanel contenedor = new JPanel(), btns = new JPanel();

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_modificar = new JButton("Modificar");
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        contenedor.setLayout(new GridLayout(param.length, 2, 10, 10));

        JLabel lbl_col = new JLabel(param[0]);
        SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, 1000, 1); // Valor inicial: 5, Mínimo: 0, Máximo: 10, Paso: 1
        JSpinner ent_val = new JSpinner(numberModel);
        ent_val.setValue(Integer.parseInt(param[1]));

        contenedor.add(lbl_col);
        contenedor.add(ent_val);

        dialog.add(contenedor);
        dialog.add(btns);

        btns.add(btn_cancelar);
        btns.add(btn_modificar);

        btn_modificar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 20, 10));

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch (admin.modificarParametro((int) ent_val.getValue())) {
                    case 1:
                        dialog.dispose();
                        consultarParametrosUI();
                        break;
                    case 0:
                        JOptionPane.showMessageDialog(null, "No se pudo guardar la nueva configuración", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF19 ----------------------------- //
    private void editarPrivilegiosUI(String usuario) {
        JDialog dialog = new JDialog(ventana, "Privilegios", true);
        JPanel contenedor = new JPanel();

        String[] columnas = {"Limite de compra al mismo tiempo", "Tiempo máximo de compra", "Limite de tarjetas cargables"};
        JLabel lbl_adm = new JLabel("Administrador");

        int[] valores_usuario = admin.seleccionarPrivilegios(usuario);

        JButton btn_cancelar = new JButton("Cancelar");
        JButton btn_aceptar = new JButton("Aceptar");
        JCheckBox cb_adm = new JCheckBox();

        if (valores_usuario[3] == 1) {
            cb_adm.setSelected(true);
        }

        dialog.setSize(500, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setBackground(Color.WHITE);

        contenedor.setLayout(new GridLayout(columnas.length + 2, 2, 2, 2));

        JSpinner[] ent_cols = new JSpinner[columnas.length];

        for (int i = 0; i < columnas.length; i++) {

            SpinnerNumberModel modelo = new SpinnerNumberModel(1, 1, 1000, 1);
            ent_cols[i] = new JSpinner(modelo);
            ent_cols[i].setValue(valores_usuario[i]);

            if (valores_usuario[3] != 1) {
                contenedor.add(new JLabel(columnas[i]));
                contenedor.add(ent_cols[i]);
            }

        }

        contenedor.add(lbl_adm);
        contenedor.add(cb_adm);
        contenedor.add(btn_cancelar);
        contenedor.add(btn_aceptar);

        btn_aceptar.setBackground(color_principal);
        btn_cancelar.setBackground(color_principal);
        btn_aceptar.setForeground(Color.WHITE);
        btn_cancelar.setForeground(Color.WHITE);

        btn_cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_aceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contenedor.setBorder(new EmptyBorder(20, 10, 20, 10));
        dialog.add(contenedor);

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        btn_aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere guardar los privilegios del usuario?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {

                    int adm_ch = 0;
                    boolean cambioRol = false;

                    if (cb_adm.isSelected()) {
                        adm_ch = 1;
                    }

                    if (adm_ch != valores_usuario[3]) {
                        cambioRol = true;
                    }

                    int[] privilegios = {(int) ent_cols[0].getValue(), (int) ent_cols[1].getValue(), (int) ent_cols[2].getValue(), adm_ch};

                    if (!admin.modificarPrivilegio(usuario, privilegios, cambioRol)) {
                        JOptionPane.showMessageDialog(null, "Los datos no cumplen el formato", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        dialog.dispose();
                    }

                }

            }
        });

        dialog.setVisible(true);
    }

    // ----------------------------- RF20 ----------------------------- //
    private void consultarSalasUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Salas");
        JButton btn_eliminar = new JButton("Eliminar");
        JButton btn_modificar = new JButton("Modificar");
        JButton btn_agregar = new JButton("Agregar");

        String[] columnas = {"Sala", "Filas", "Columnas", "Capacidad max"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));

        String[][] datos = admin.seleccionarSalas();

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
        } else {
            JLabel no_salas = new JLabel("No hay salas cargadas");
            no_salas.setBorder(new EmptyBorder(30, 0, 0, 0));
            no_salas.setFont(fnt_titulo);
            no_salas.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(no_salas);
        }
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btns_frm);

        btn_eliminar.setBackground(color_principal);
        btn_eliminar.setForeground(Color.WHITE);
        btn_agregar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_modificar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);

        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setFocusPainted(false);
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);
        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setFocusPainted(false);

        btns_frm.add(btn_eliminar);
        btns_frm.add(btn_modificar);
        btns_frm.add(btn_agregar);

        btn_eliminar.setEnabled(false);
        btn_modificar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_eliminar.setEnabled(true);
                btn_modificar.setEnabled(true);
            }
        });

        btn_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar la sala seleccionada?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    if (admin.eliminarSala(Integer.parseInt((String) tabla.getValueAt(tabla.getSelectedRow(), 0))) == 0) {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar la sala", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        consultarSalasUI();
                    }

                }
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarSalaUI();
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = Integer.parseInt((String) tabla.getValueAt(tabla.getSelectedRow(), 1));
                int columna = Integer.parseInt((String) tabla.getValueAt(tabla.getSelectedRow(), 2));
                String numero = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                modificarSalaUI(numero, fila, columna);

            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF21 ----------------------------- //
    private void consultarFuncionesUI(String show, String show_id) {

        // String[] funciones = admin.consultarFunciones(show);
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));
        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel(show);
        JButton btn_eliminar = new JButton("Eliminar");
        JButton btn_modificar = new JButton("Modificar");
        JButton btn_agregar = new JButton("Agregar");

        String[] columnas = {"ID", "Fecha", "Horario", "Sala", "Idioma", "Proyección"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        String[][] datos = admin.seleccionarFunciones(show_id);

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
        } else {
            JLabel no_funciones = new JLabel("No hay funciones cargadas");
            no_funciones.setBorder(new EmptyBorder(30, 0, 0, 0));
            no_funciones.setFont(fnt_titulo);
            no_funciones.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(no_funciones);
        }
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btns_frm);

        btn_eliminar.setBackground(color_principal);
        btn_eliminar.setForeground(Color.WHITE);
        btn_agregar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_modificar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);

        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setFocusPainted(false);
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);
        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setFocusPainted(false);

        btns_frm.add(btn_eliminar);
        btns_frm.add(btn_modificar);
        btns_frm.add(btn_agregar);

        btn_eliminar.setEnabled(false);
        btn_modificar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_eliminar.setEnabled(true);
                btn_modificar.setEnabled(true);
            }
        });

        btn_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar la función seleccionada?", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    String funcion_id = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
                    if (admin.eliminarFuncion(show_id, funcion_id) == 0) {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar la función correctamente", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        consultarFuncionesUI(show, show_id);
                    }

                }
            }
        });

        btn_agregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarFuncionUI(columnas, show_id, show);
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] funcion = new String[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    funcion[i] = (String) tabla.getValueAt(tabla.getSelectedRow(), i);
                }

                modificarFuncionUI(columnas, funcion, show_id, show);

            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF22 ----------------------------- //  
    private void consultarShowsUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Shows");
        JButton btn_funciones = new JButton("Funciones");
        JButton btn_agregar = new JButton("Agregar");
        JButton btn_editar = new JButton("Editar");
        JButton btn_eliminar = new JButton("Eliminar");

        String[] columnas = {"ID", "Show", "Duración", "Director", "Fecha estreno", "Descripción", "Precio"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        LinkedList<String[]> datos = admin.seleccionarShows();

        String[][] showsArray = new String[datos.size()][];

        for (int i = 0; i < datos.size(); i++) {
            showsArray[i] = datos.get(i);
        }

        DefaultTableModel modelo = new DefaultTableModel(showsArray, columnas) {
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

        if (showsArray[0].length > 1) {
            contenedor.add(scrollPane);
        } else {
            JLabel no_shows = new JLabel("No hay shows disponibles");
            no_shows.setBorder(new EmptyBorder(30, 0, 0, 0));
            no_shows.setFont(fnt_titulo);
            no_shows.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(no_shows);
        }

        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btns_frm);

        btn_agregar.setBackground(color_principal);
        btn_agregar.setForeground(Color.WHITE);
        btn_editar.setBackground(color_principal);
        btn_editar.setForeground(Color.WHITE);
        btn_eliminar.setBackground(color_principal);
        btn_eliminar.setForeground(Color.WHITE);
        btn_funciones.setBackground(color_principal);
        btn_funciones.setForeground(Color.WHITE);

        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setFocusPainted(false);
        btn_editar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_editar.setFocusPainted(false);
        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);
        btn_funciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_funciones.setFocusPainted(false);

        btn_agregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_agregar.setFocusPainted(false);
        btn_editar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_editar.setFocusPainted(false);
        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setFocusPainted(false);
        btn_funciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_funciones.setFocusPainted(false);

        btns_frm.add(btn_funciones);
        btns_frm.add(btn_eliminar);
        btns_frm.add(btn_editar);
        btns_frm.add(btn_agregar);

        btn_eliminar.setEnabled(false);
        btn_editar.setEnabled(false);
        btn_funciones.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_eliminar.setEnabled(true);
                btn_editar.setEnabled(true);
                btn_funciones.setEnabled(true);
            }
        });

        btn_funciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarFuncionesUI((String) tabla.getValueAt(tabla.getSelectedRow(), 1), (String) tabla.getValueAt(tabla.getSelectedRow(), 0));
            }
        });

        btn_editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] show = new String[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    show[i] = (String) tabla.getValueAt(tabla.getSelectedRow(), i);
                }

                modificarShowUI(columnas, show);
            }
        });

        btn_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el show seleccionado?\nTambién eliminará todas sus funciones", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    if (admin.eliminarShow((String) tabla.getValueAt(tabla.getSelectedRow(), 0)) == 0) {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el show", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        consultarShowsUI();
                    }

                }
            }
        }
        );

        btn_agregar.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                cargarShowUI(columnas);
            }
        }
        );

        ventana.add(contenedor);

        ventana.revalidate();

        ventana.repaint();

    }

    // ----------------------------- RF23 ----------------------------- //
    private void consultarPerfilesUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Usuarios");
        JButton btn_privilegios = new JButton("Privilegios");
        JButton btn_eliminar = new JButton("Eliminar");

        String[] columnas = {"ID", "Usuario", "Contraseña"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        String[][] datos = admin.seleccionarUsuarios();

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
        } else {
            JLabel no_perfiles = new JLabel("No hay perfiles cargados");
            no_perfiles.setBorder(new EmptyBorder(30, 0, 0, 0));
            no_perfiles.setFont(fnt_titulo);
            no_perfiles.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedor.add(no_perfiles);
        }
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btns_frm);

        btn_eliminar.setBackground(color_principal);
        btn_eliminar.setForeground(Color.WHITE);

        btn_eliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_eliminar.setFocusPainted(false);

        btn_privilegios.setBackground(color_principal);
        btn_privilegios.setForeground(Color.WHITE);

        btn_privilegios.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_privilegios.setFocusPainted(false);
        btns_frm.add(btn_privilegios);
        btns_frm.add(btn_eliminar);

        btn_privilegios.setEnabled(false);
        btn_eliminar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_privilegios.setEnabled(true);
                btn_eliminar.setEnabled(true);
            }
        });

        btn_privilegios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarPrivilegiosUI((String) tabla.getValueAt(tabla.getSelectedRow(), 1));
            }
        });

        btn_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "¿Está seguro que quiee eliminar al usuario?\nTambién eliminará todas sus compras", "Precaución", JOptionPane.YES_NO_OPTION) == 0) {
                    if (admin.eliminarUsuario((String) tabla.getValueAt(tabla.getSelectedRow(), 1)) == 0) {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        consultarPerfilesUI();
                    }
                }
            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();

    }

    // ----------------------------- RF23 ----------------------------- //
    private void consultarParametrosUI() {
        ventana.getContentPane().removeAll();
        ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.Y_AXIS));
        ventana.add(Box.createVerticalStrut(20));

        JPanel contenedor = new JPanel(), btns_frm = new JPanel();
        JLabel lbl_titulo = new JLabel("Parametros");
        JButton btn_modificar = new JButton("Modificar");

        String[] columnas = {"Parametro", "Valor"};
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        String[][] datos = {{"Sesiones maximas simultaneamente", String.valueOf(admin.seleccionarParametros())}};

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        lbl_titulo.setFont(fnt_titulo);

        JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        TableColumnModel columnModel = tabla.getColumnModel();
        TableColumn firstNameColumn = columnModel.getColumn(0);
        firstNameColumn.setPreferredWidth(300);
//        firstNameColumn.setMinWidth(100);        
//        firstNameColumn.setMaxWidth(200);        

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

        btn_modificar.setForeground(Color.WHITE);
        btn_modificar.setBackground(color_principal);
        btn_modificar.setForeground(Color.WHITE);

        btn_modificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_modificar.setFocusPainted(false);
        btns_frm.add(btn_modificar);

        btn_modificar.setEnabled(false);

        contenedor.setMaximumSize(new Dimension(500, 300));
        contenedor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btn_modificar.setEnabled(true);
            }
        });

        btn_modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] param = new String[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    param[i] = (String) tabla.getValueAt(tabla.getSelectedRow(), i);
                }
                modificarParametrosUI(param);
                consultarParametrosUI();
            }
        });

        ventana.add(contenedor);
        ventana.revalidate();
        ventana.repaint();
    }
}
