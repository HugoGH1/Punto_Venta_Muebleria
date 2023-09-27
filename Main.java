package com.mycompany.muebleria;
import java.awt.*;
import javax.swing.*;

public class Main extends JFrame{

    PInventario miInventario;
    PRegistroVenta registroVentas;
    PVentas ventas;
    Color colorfondo = new Color(250, 246, 233);
    Color colorper = new Color(220, 179, 147);
    public static JTabbedPane pestañas;
    
    
    public Main(){
        setTitle("Mueblería WOODS S.A de C.V");
        setSize(900,800);
        this.setBackground(colorfondo);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Componentes();
        
    }
    
    private void Componentes(){
        
        pestañas = new JTabbedPane();
        pestañas.setBounds(20, 30, 750, 500);
        pestañas.setBackground(colorper);
        pestañas.setVisible(true);
        
        miInventario = new PInventario();
        miInventario.setBackground(Color.WHITE);
        registroVentas = new PRegistroVenta();
        registroVentas.setBackground(Color.WHITE);
        ventas = new PVentas();
        ventas.setBackground(Color.WHITE);
        
        pestañas.add("Inventario", miInventario);
        pestañas.add("Registro de Ventas", registroVentas);
        pestañas.add("Ventas", ventas);
        
        add(pestañas);
        
    }

}