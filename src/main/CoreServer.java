package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class CoreServer extends JDialog {
	private JPanel contentPane;
	private JTextField txtip;
	private JTextField txtport;
	private JPanel panel;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CoreServer dialog = new CoreServer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CoreServer() {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 292);
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
			@Override
			public void actionPerformed(ActionEvent arg0) {
				abrir();
			}
		});
		btnEntrar.setBounds(295, 65, 89, 23);
		panel.add(btnEntrar);

		JLabel lblNewLabel = new JLabel(
				"<html><center> <strong> Alumnos: <strong> <br> José Antonio Pino Ocampo <br> Juan Carlos Almeyda Cruz<br> Pablo Gaddiel Carrillo Guerrero<br> Otilio Guevara Dominguez </center></html>\"");
		lblNewLabel.setBounds(117, 143, 195, 110);
		contentPane.add(lblNewLabel);

		JButton btnServer = new JButton("Server");
		btnServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		btnServer.setBounds(156, 121, 89, 23);
		contentPane.add(btnServer);
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
