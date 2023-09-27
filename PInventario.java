package com.mycompany.muebleria;

import static com.mycompany.muebleria.PRegistroVenta.dtm2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class PInventario extends JPanel implements ActionListener {

    JLabel lblInventario = new JLabel("Inventario");
    JButton btnAdd = new JButton("Añadir ");
    JButton btnEdit = new JButton("Modificar");
    JButton btnDel = new JButton("Eliminar");

    public static DefaultTableModel dtm = new DefaultTableModel();
    String[] titulos = {"Código", "Nombre del Artículo", "Precio", "Cantidad"};

    public static JTable TInventario = new JTable();
    public JScrollPane scroll = new JScrollPane(TInventario);
    File file = new File("articulos.txt");

    //  DISEÑO DE BOTONES  Y TABLA
    static Color colorper = new Color(220, 179, 147);
    JTableHeader header = new JTableHeader();

    TableColumnModel columnModel = header.getColumnModel();
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

    public PInventario() {
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDel.addActionListener(this);
        for (int columna = 0; columna < 4; columna++) {
            dtm.addColumn(titulos[columna]);
        }
        TInventario.setModel(dtm);
        TInventario.setPreferredScrollableViewportSize(new Dimension(600, 400));

        header = TInventario.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        Distribucion();
        LeerlosDatos();
        
    }

    public void Distribucion() {
        this.setLayout(null);

        //Personalizar la etiqueta inventario
        lblInventario.setBounds(400, 30, 100, 25);
        lblInventario.setForeground(Color.BLACK);
        lblInventario.setFont(new Font("Roboto Thin", Font.PLAIN, 18));

        TInventario.setRowHeight(40);
        TInventario.setFont(new Font("Roboto Thin", Font.PLAIN, 12));
        TInventario.setBackground(new Color(248, 246, 241));

        //Centra los datos en las celdas
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columnIndex = 0; columnIndex < TInventario.getColumnCount(); columnIndex++) {
            TInventario.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }

        scroll.setBounds(30, 60, 800, 550);
        scroll.setBackground(colorper);

        btnAdd.setBounds(30, 630, 110, 50);
        btnAdd.setFont(new Font("Roboto Condensed", Font.PLAIN, 14));
        btnAdd.setBackground(colorper);
        btnAdd.setPreferredSize(new Dimension(110, 50));
        btnAdd.setOpaque(true);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setBorderPainted(false);
        btnAdd.setToolTipText("Registrar un nuevo artículo");
        
        btnEdit.setBounds(150, 630, 110, 50);
        btnEdit.setFont(new Font("Roboto Condensed", Font.PLAIN, 14));
        btnEdit.setBackground(colorper);
        btnEdit.setPreferredSize(new Dimension(110, 50));
        btnEdit.setOpaque(true);
        btnEdit.setContentAreaFilled(true);
        btnEdit.setBorderPainted(false);
        btnEdit.setToolTipText("Actualizar ó modificar algún registro");
        
        btnDel.setBounds(270, 630, 110, 50);
        btnDel.setFont(new Font("Roboto Condensed", Font.PLAIN, 14));
        btnDel.setBackground(colorper);
        btnDel.setPreferredSize(new Dimension(110, 50));
        btnDel.setOpaque(true);
        btnDel.setContentAreaFilled(true);
        btnDel.setBorderPainted(false);
        btnDel.setToolTipText("Clic para eliminar el artículo seleccionado");
        
        this.add(lblInventario);
        this.add(scroll);
        this.add(btnAdd);
        this.add(btnEdit);
        this.add(btnDel);
    }

    public void actionPerformed(ActionEvent ev) {

        Object objeto = ev.getSource();

        if (objeto == btnAdd) {

            String[] datosArt = new String[4];
            for (int i = 0; i < 4; i++) {
                datosArt[i] = JOptionPane.showInputDialog("Introduce " + TInventario.getColumnName(i));
                if (datosArt[i] == null || datosArt[i].isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes llenar los campos");
                    i--;
                }
            }

            ArrayList<String> DatosArticulos = new ArrayList<>();

            for (String dato : datosArt) {
                DatosArticulos.add(dato);
            }
            // Asegura que la tabla se actualice y muestre los datos recien agregados
            TInventario.repaint();

            //Escribir en el archivo
            try {
                FileWriter fw = new FileWriter("articulos.txt", true);
                fw.write(datosArt[0] + "-");
                fw.write(datosArt[1] + "-");
                fw.write(datosArt[2] + "-");
                fw.write(datosArt[3] + "\n");
                dtm.addRow(datosArt);
                UpdateFile();
                Modificacion();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (objeto == btnDel) {
            int selectedRow = TInventario.getSelectedRow();
            if (selectedRow != -1) {
                dtm.removeRow(selectedRow);
                UpdateFile();
                Modificacion();
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para eliminarla.");

            }

        } else if (objeto == btnEdit) {
            UpdateFile();
            Modificacion();

        }
    }

    //leer el archivo y mostrarlo en la tabla
    public void LeerlosDatos() {
        try {
            File file = new File("articulos.txt");
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while ((s = in.readLine()) != null) {
                String[] fila = s.split("-");
                dtm.addRow(fila);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo que actualizará el archivo, se utiliza para eliminar y modificar un registro 
    public void UpdateFile() {
        if (file.exists()) {
            file.delete();
        }

        try {
            File file = new File("articulos.txt");
            BufferedWriter esc = new BufferedWriter(new FileWriter("articulos.txt", true));
            for (int i = 0; i < this.TInventario.getRowCount(); i++) {
                esc.write(dtm.getValueAt(i, 0).toString() + "-");
                esc.write(dtm.getValueAt(i, 1).toString() + "-");
                esc.write(dtm.getValueAt(i, 2).toString() + "-");
                esc.write(dtm.getValueAt(i, 3).toString() + "\n");
                TInventario.clearSelection();
            }
            esc.close();
            JOptionPane.showMessageDialog(null, "¡Se han actualizado los datos!");
        } catch (Exception e) {
            System.out.println("Parece que no se selccionó ninguna fila");;
        }
    }

    //Actualiza las tablas una vez que se ha activado el evento de un botón
    public void Modificacion() {
        ArrayList<String[]> Listaproductos = new ArrayList<>();
        String[] productos = new String[3];
        for (int i = 0; i < PInventario.TInventario.getRowCount(); i++) {
            productos[0] = dtm.getValueAt(i, 0).toString();
            productos[1] = dtm.getValueAt(i, 1).toString();
            productos[2] = dtm.getValueAt(i, 2).toString();
            Listaproductos.add(new String[]{productos[0], productos[1], productos[2]});
        }
        try ( FileWriter FW = new FileWriter("productos.txt")) {
            // Recorrer el ArrayList de arreglos
            for (String[] datosArticulos : Listaproductos) {
                // Convertir el arreglo en una línea de texto
                StringBuilder linea = new StringBuilder();
                for (String elemento : datosArticulos) {
                    linea.append(elemento).append("-"); // Puedes personalizar el formato
                }
                linea.setLength(linea.length() - 1); // Eliminar la última coma y espacio
                linea.append("\n"); // Agregar un salto de línea al final

                // Escribir la línea en el archivo
                FW.write(linea.toString());
                dtm2.setRowCount(0);
            }
            System.out.println("¡¡Tablas actualizadas!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File file = new File("productos.txt");
            //if (file.exists()) file.delete();  
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while ((s = in.readLine()) != null) {
                String[] fila = s.split("-");
                dtm2.addRow(fila);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Personalización de la tabla
    static class HeaderRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Personalizar el renderizador del encabezado para centrar el texto
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(colorper);
            setFont(new Font("Roboto Condensed", Font.PLAIN, 14));
            setForeground(Color.BLACK);
            setPreferredSize(new Dimension(800, 40));

            return this;
        }
    }


}
