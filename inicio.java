//package Muebleria;
package com.mycompany.muebleria;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.JOptionPane;

public class inicio{

    static String Username;
    static String Password;
    JFrame frame = new JFrame("Iniciar sesión en Muebleria");
    // LOGIN
    public inicio(){
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ComponentesDis();
        frame.setVisible(true);
        

    }
    
    public void ComponentesDis(){
        frame.setLayout(null);
        //frame.setBackground(color.White);
        
        JLabel lblMuebleria = new JLabel("Muebleria SA de CV");
        lblMuebleria.setBounds(150, 50, 150, 25);
        frame.add(lblMuebleria);

        JLabel lbluser = new JLabel("Usuario:");
        lbluser.setBounds(50,95,100,25);
        frame.add(lbluser);

        JLabel lblpassw = new JLabel("Contraseña:");
        lblpassw.setBounds(50,135,80,25);
        frame.add(lblpassw);

        JTextField txtuser = new JTextField();
        txtuser.setBounds(170,95,150,25);
        frame.add(txtuser);

        JPasswordField txtpass = new JPasswordField();
        txtpass.setBounds(170,135,150,25);
        frame.add(txtpass);

        JButton btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setBounds(50,200,150,25);
        frame.add(btnIniciar);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(220,200,80,25);
        frame.add(btnSalir);
        
        
        btnIniciar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
               Username = txtuser.getText();
               Password = txtpass.getText();
               if((!Username.isEmpty()) || (!Password.isEmpty())){
                    JOptionPane.showMessageDialog(null, "Bienvenid@ "+Username);
                    /*Mueb obje = new Mueb();
                    obje.show();*/
                    frame.setVisible(false);
               }    
                else JOptionPane.showMessageDialog(null, "Campos Vacíos!! Ingrese Usuario y Contraseña"); 
            }
        });
        
        btnSalir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
               System.exit(0);
            }
        });
    }
    
    public static void main(String[] args) {
        inicio obInicio = new inicio();
    }
}

