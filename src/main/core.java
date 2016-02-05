package main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dataTier.DataAccess;
import interfaces.cliente.UserLogin;
import interfaces.servidor.Principal;

public class core extends JFrame {

	private JPanel contentPane;
	private JTextField txtip;
	private JTextField txtport;
	private UserLogin frontEndLogIn;
	private Principal frontEnd;
	private JPanel panel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					core frame = new core();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public core() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 260);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBorder(
				new TitledBorder(null, "Datos de servidor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 414, 99);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblDireccionIp = new JLabel("Direccion IP:");
		lblDireccionIp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDireccionIp.setBounds(10, 23, 70, 14);
		panel.add(lblDireccionIp);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPort.setBounds(20, 48, 70, 14);
		panel.add(lblPort);

		txtip = new JTextField();
		txtip.setBounds(97, 20, 126, 20);
		panel.add(txtip);
		txtip.setColumns(10);

		txtport = new JTextField();
		txtport.setColumns(10);
		txtport.setBounds(97, 45, 126, 20);
		panel.add(txtport);

		JButton btnEntrar = new JButton("Fijar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrir();
			}
		});
		btnEntrar.setBounds(295, 65, 89, 23);
		panel.add(btnEntrar);

		JLabel lblNewLabel = new JLabel(
				"<html><center> <strong> Alumnos: <strong> <br> José Antonio Pino Ocampo <br> Juan Carlos Almeyda Cruz<br> Pablo Gaddiel Carrillo Guerrero<br> Otilio Guevara Dominguez </center></html>\"");
		lblNewLabel.setBounds(120, 121, 195, 110);
		contentPane.add(lblNewLabel);

		JButton btnCliente = new JButton("Cliente");
		btnCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frontEndLogIn = new UserLogin();
				frontEndLogIn.mostrar();
			}
		});
		btnCliente.setBounds(10, 155, 89, 23);
		contentPane.add(btnCliente);

		JButton btnServidor = new JButton("Servidor");
		btnServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataAccess.getInstance().limpiarConectados();
				frontEnd = new Principal();
				frontEnd.setResizable(false);
				frontEnd.setVisible(true);
			}
		});
		btnServidor.setBounds(325, 155, 89, 23);
		contentPane.add(btnServidor);
	}

	private void abrir() {
		Properties prop = new Properties();
		prop.setProperty("port", txtport.getText());
		prop.setProperty("ip", txtip.getText());
		try {
			prop.store(new FileOutputStream("ServerConfig.properties"), null);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		txtip.setEditable(false);
		txtport.setEditable(false);
	}
}
