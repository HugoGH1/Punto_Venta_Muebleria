
package com.mycompany.muebleria;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class PVentas extends JPanel{
    
    JLabel lblVentas = new JLabel("Ventas");
    JLabel lblNumVentas = new JLabel("");
    public static DefaultTableModel dtm3 = new DefaultTableModel();
    public static JTable tablaVentas = new JTable();
    JScrollPane scroll3 = new JScrollPane(tablaVentas);
    static Color colorper = new Color(220, 179, 147);
    JTableHeader header = tablaVentas.getTableHeader();
    public static JLabel lblventasNetas = new JLabel("");
    
    TableColumnModel columnModel = header.getColumnModel();
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es","MX","MX"));
    
    
    public PVentas(){
        
        dtm3.addColumn("Nombre del Cliente");
        dtm3.addColumn("Nombre del articulo");
        dtm3.addColumn("Cantidad");
        dtm3.addColumn("Monto");
        dtm3.addColumn("Fecha / Hora");

        header.setDefaultRenderer(new HeaderRenderer());
        MostrarTabla();
        Distribucion3();
    }
    
    public void Distribucion3(){
        this.setLayout(null);
        tablaVentas.setModel(dtm3);
        tablaVentas.setPreferredScrollableViewportSize(new Dimension(600,400));
        
        lblVentas.setBounds(400, 30, 100, 25);
        lblVentas.setForeground(Color.BLACK);
        lblVentas.setFont(new Font("Roboto Thin", Font.PLAIN, 18));
        
        tablaVentas.setRowHeight(40);
        tablaVentas.setFont(new Font("Roboto Thin", Font.PLAIN, 12));
        tablaVentas.setBackground(new Color(248, 246, 241));
        
        lblventasNetas.setBounds(30, 560, 250, 25);
        lblventasNetas.setForeground(Color.BLACK);
        lblventasNetas.setFont(new Font("Roboto Thin", Font.PLAIN, 16));
        
        //asaucedo@itdurango.edu.mx  GRUPO 4YZ COMPARTIR COMO EDITOR

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int columnIndex = 0; columnIndex < tablaVentas.getColumnCount(); columnIndex++) {
            tablaVentas.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
         
        scroll3.setBounds(30, 60, 800, 400);
        scroll3.setBackground(colorper);
        
        int NumVenta = tablaVentas.getRowCount();
        int venta = 0, totalVentas = 0;
               for(int i = 0; i < tablaVentas.getRowCount(); i++) {
                   venta = Integer.parseInt(tablaVentas.getValueAt(i, 3).toString());
                   totalVentas += venta;
               }
        String totalVentasString = currencyFormatter.format(totalVentas);
        lblventasNetas.setText("Ventas Netas: "+totalVentasString);
        lblventasNetas.setVisible(true);
        
        lblNumVentas.setText("Número de ventas realizadas: "+NumVenta);
        lblNumVentas.setBounds(30,520,300,25);
        lblNumVentas.setFont(new Font("Roboto Thin", Font.PLAIN, 16));
        lblNumVentas.setForeground(Color.BLACK);
        
        this.add(lblVentas);
        this.add(lblventasNetas);
        this.add(lblNumVentas);
        this.add(lblVentas);
        this.add(scroll3);
    }
    
    // Lectura de la tabla
    public void MostrarTabla(){
        try {
            File file = new File("compra.txt");
            //if (file.exists()) file.delete();  
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s;
            while ((s = in.readLine()) != null) {
                String[] fila = s.split("-");
                dtm3.addRow(fila);
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