package com.mycompany.muebleria;

import static com.mycompany.muebleria.PInventario.TInventario;
import static com.mycompany.muebleria.PInventario.dtm;
import static com.mycompany.muebleria.PVentas.currencyFormatter;
import static com.mycompany.muebleria.PVentas.dtm3;
import static com.mycompany.muebleria.PVentas.lblventasNetas;
import static com.mycompany.muebleria.PVentas.tablaVentas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class PRegistroVenta extends JPanel {

    JLabel lblRegistrarV = new JLabel("Registrar Venta");
    JLabel lblCliente = new JLabel("Nombre del Cliente: ");
    JTextField txtCliente = new JTextField();
    JSpinner jsCantidadV = new JSpinner();
    JLabel lblCantidad = new JLabel("Cantidad: ");
    JLabel lblMonto = new JLabel("");
    JButton btnRegVenta = new JButton("Registrar Venta");
    // TABLA
    static DefaultTableModel dtm2 = new DefaultTableModel();

    public static JTable TRegVentas = new JTable();
    public static JScrollPane scroll2 = new JScrollPane(TRegVentas);
    private SpinnerNumberModel spinnerModel;
    static Color colorper = new Color(220, 179, 147);
    File file = new File("productos.txt");
    
    //Border
    LineBorder lineBorder = new LineBorder(colorper, 1, true);
    
    //Personalización de la tabla
    JTableHeader header = TRegVentas.getTableHeader();
    TableColumnModel columnModel = header.getColumnModel();
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    int filaSeleccionada = TRegVentas.getSelectedRow();
    static int selRow = 0;
    
    
    public PRegistroVenta() {
        dtm2.addColumn("Codigo");
        dtm2.addColumn("Nombre del articulo");
        dtm2.addColumn("Precio");

        header.setDefaultRenderer(new HeaderRenderer());
        // Según la fila que se seleccione de la tabla consultará la cantidad que está almacenada en la tabla del inventario para limitar
        // la cantidad que se pueden comprar del artículo de la fila seleccionada.
        TRegVentas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int filaSeleccionada = TRegVentas.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        int Disponible = Integer.parseInt(TInventario.getValueAt(filaSeleccionada, 3).toString());
                        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, Disponible, 1);
                        jsCantidadV.setModel(spinnerModel);
                        int precio = Integer.parseInt(TRegVentas.getValueAt(filaSeleccionada, 2).toString());
                        int cantidadSeleccionada = Integer.parseInt(jsCantidadV.getValue().toString());
                        int pago = precio * cantidadSeleccionada;
                        lblMonto.setText("Monto de pago: " + pago);
                    }
                }
            }
        });
        jsCantidadV.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int filaSeleccionada = TRegVentas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    Monto(filaSeleccionada);
                }
            }
        });

        //Este botón registrará la venta en la tabla PVentas con algunos datos que habrán sido validados
        // como el nombre del cliente y/o si hay alguna fila selccionada.
        btnRegVenta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                
                int SelecCant = (int)jsCantidadV.getValue();
                if ((filaSeleccionada != -1) && !txtCliente.equals("")) {
                    if(SelecCant != 0){
                        int resp;
                        resp = JOptionPane.showConfirmDialog(null, "¿Deseas Continuar?", "Estás realizando una venta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (resp == 0) {
                            GuardarVenta();
                            VentasNetas();
                            JOptionPane.showMessageDialog(null, "Se ha registrado la compra");
                            txtCliente.setText("");
                            jsCantidadV.setEnabled(false);
                            TRegVentas.clearSelection();
                            lblMonto.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "¡Operación Cancelada!");
                            txtCliente.setText("");
                            lblMonto.setText("");
                            jsCantidadV.setEnabled(false);
                            TRegVentas.clearSelection();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "¡Artículo agotado ó cantidad no seleccionada!");
                        
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "¡Alerta! \n"+"Debes llenar todos los campos");
                }           
            }
        });
        Distribucion();
        LeerProducto();
        Validacion();
    }

    public void Distribucion() {
        this.setLayout(null);
        TRegVentas.setModel(dtm2);
        TRegVentas.setPreferredScrollableViewportSize(new Dimension(600, 400));
        
        //Personalizar la etiqueta registro de ventas
        lblRegistrarV.setBounds(380, 30, 180, 25);
        lblRegistrarV.setForeground(Color.BLACK);
        lblRegistrarV.setFont(new Font("Roboto Thin", Font.PLAIN, 18));

        TRegVentas.setRowHeight(40);
        TRegVentas.setFont(new Font("Roboto Thin", Font.PLAIN, 12));
        TRegVentas.setBackground(new Color(248, 246, 241));

        scroll2.setBounds(30, 60, 800, 450);
        scroll2.setBackground(colorper);

        // Centrar los datos en las celdas
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columnIndex = 0; columnIndex < TRegVentas.getColumnCount(); columnIndex++) {
            TRegVentas.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
        
        lblCliente.setBounds(30, 530, 150, 25);
        txtCliente.setBounds(190, 530, 200, 25);
        txtCliente.setBorder(lineBorder);

        lblCantidad.setBounds(410, 530, 80, 25);
        jsCantidadV.setBounds(500, 530, 120, 25);
        lblMonto.setBounds(630, 530, 180, 25);

        btnRegVenta.setBounds(30, 600, 150, 40);
        btnRegVenta.setFont(new Font("Roboto Condensed", Font.PLAIN, 14));
        btnRegVenta.setBackground(colorper);
        btnRegVenta.setPreferredSize(new Dimension(150, 40));
        btnRegVenta.setOpaque(true);
        btnRegVenta.setContentAreaFilled(true);
        btnRegVenta.setBorderPainted(false);
        btnRegVenta.setToolTipText("Clic para registrar una nueva venta");
        jsCantidadV.setEnabled(false);

        
        this.add(lblRegistrarV);
        this.add(scroll2);
        this.add(lblCliente);
        this.add(lblCantidad);
        this.add(txtCliente);
        this.add(jsCantidadV);
        this.add(lblMonto);
        this.add(btnRegVenta);
    }

    public void LeerProducto() {
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

    public void UpdateFileS() {
        if (file.exists()) {
            file.delete();
        }

        try {
            File file = new File("productos.txt");
            BufferedWriter esc = new BufferedWriter(new FileWriter("productos.txt", true));
            for (int i = 0; i < this.TRegVentas.getRowCount(); i++) {
                esc.write(dtm2.getValueAt(i, 0).toString() + "-");
                esc.write(dtm2.getValueAt(i, 1).toString() + "-");
                esc.write(dtm2.getValueAt(i, 2).toString() + "\n");
            }
            esc.close();
            //JOptionPane.showMessageDialog(null, "¡Tabla Actualizada!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Monto(int filaSeleccionada) {
        int precio = Integer.parseInt(TRegVentas.getValueAt(filaSeleccionada, 2).toString());
        int cantidadSeleccionada = Integer.parseInt(jsCantidadV.getValue().toString());
        lblMonto.setText("Monto de pago: " + (precio * cantidadSeleccionada));
    }

    public void GuardarVenta() {
        //Obtener lo datos de la compra que está realizando el cliente
        String Cliente = txtCliente.getText();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String fecha = now.format(formatter);
        int SelRow = TRegVentas.getSelectedRow();
        String articulo = dtm2.getValueAt(SelRow, 1).toString();
        int cantidadSeleccionada = (Integer) jsCantidadV.getValue();
        String cantidadS = jsCantidadV.getValue().toString();
        int precio = Integer.parseInt(TRegVentas.getValueAt(SelRow, 2).toString());
        String Monto = ("" + (precio * cantidadSeleccionada));
        String[] compra = {Cliente, articulo, cantidadS, Monto, fecha};

        try {
            FileWriter fw = new FileWriter("compra.txt", true);
            fw.write(compra[0] + "-");
            fw.write(compra[1] + "-");
            fw.write(compra[2] + "-");
            fw.write(compra[3] + "-");
            fw.write(compra[4] + "\n");
            dtm3.addRow(compra);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int cantidadI = Integer.parseInt(TInventario.getValueAt(SelRow, 3).toString());
        int UCant = cantidadI - cantidadSeleccionada;

        dtm.setValueAt(UCant, SelRow, 3);
        File fileA = new File("articulos.txt");
        fileA.delete();
        try {
            BufferedWriter esc = new BufferedWriter(new FileWriter("articulos.txt", true));
            for (int i = 0; i < TInventario.getRowCount(); i++) {
                esc.write(dtm.getValueAt(i, 0).toString() + "-");
                esc.write(dtm.getValueAt(i, 1).toString() + "-");
                esc.write(dtm.getValueAt(i, 2).toString() + "-");
                esc.write(dtm.getValueAt(i, 3).toString() + "\n");
            }
            esc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Validacion(){
        TRegVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = TRegVentas.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                jsCantidadV.setValue(0);
                selRow = lsm.getMinSelectionIndex();
                filaSeleccionada = TRegVentas.getSelectedRow();
                if (selRow >= 0) {
                    jsCantidadV.setEnabled(true);
                }
            }  
        });
    }
    
    //Actualiza el label de ventas netas de PVentas
    public void VentasNetas(){
        int venta = 0, totalVentas = 0;
               for(int i = 0; i< tablaVentas.getRowCount(); i++) {
                   venta = Integer.parseInt(tablaVentas.getValueAt(i, 3).toString());
                   totalVentas += venta;
               }
        String totalVentasString = currencyFormatter.format(totalVentas);
        lblventasNetas.setText("Ventas Netas: "+totalVentasString);
        lblventasNetas.setVisible(true);
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
