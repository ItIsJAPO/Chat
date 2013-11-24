package interfaces.grupos;

import groups.Grupo;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import client.ChatClient;

import common.FriendStatus;
import common.MensajeGrupo;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JTable;

public class ClienteModCrearChatBroad extends JFrame {

	private static final long serialVersionUID = -6815140153895535755L;

	private JPanel contentPane;
	private JTextField txtNombreGrupo;

	/**
	 * Create the frame.
	 */
	public ClienteModCrearChatBroad() {
		setTitle("Creaci\u00F3n de Sala de Chat");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 489, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCrearGrupoDe = new JLabel("CREAR GRUPO DE CHAT BROADCAST");
		lblCrearGrupoDe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCrearGrupoDe.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrearGrupoDe.setBounds(10, 11, 461, 47);
		contentPane.add(lblCrearGrupoDe);
		
		final JList listAmigos = new JList();
		listAmigos.setBounds(20, 149, 181, 245);
		contentPane.add(listAmigos);
		listAmigos.setModel(this.obtenerListaAmigos());
		
		
		final JList listAmigosAgregados = new JList();
		listAmigosAgregados.setBounds(269, 149, 191, 245);
		contentPane.add(listAmigosAgregados);
		
		JButton btnAgregarAmigo = new JButton("->");
		btnAgregarAmigo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Obtengo el usuario que se seleccion�
				String nombreUsuario = listAmigos.getSelectedValue().toString();
				// Hago el cambio entre la lista de amigos y la lista de amigos agregados
				// Lo saco de la lista de amigos y lo pongo en la lista de amigos agregados
				listAmigosAgregados.setModel(this.agregarAmigo(nombreUsuario));
				listAmigos.setModel(this.removerAmigo(nombreUsuario));
			}
			private DefaultListModel<String> agregarAmigo(String nombreUsuario){
				DefaultListModel<String> modelAmigos = new DefaultListModel<String>();
				boolean existe = false;
				// Recorro toda la lista de amigos agregados para que quede cargada con todos los elemtos que ten�a
				for(int i = 0; i < listAmigosAgregados.getModel().getSize(); i++) {
					Object item = listAmigosAgregados.getModel().getElementAt(i);
					modelAmigos.addElement(item.toString());
					// Valido que el usuario no est� ya agregado
					if(item.toString() == nombreUsuario){
						existe = true;
					}
				}
				if(!existe){
					modelAmigos.addElement(nombreUsuario);	
				}
				return modelAmigos;
			}
			private DefaultListModel<String> removerAmigo(String nombreUsuario){
				DefaultListModel<String> modelAmigos = new DefaultListModel<String>();
				// Vuelvo a cargar la lista de amigos con todos los amigos menos el que se agreg�
				for(int i = 0; i < listAmigos.getModel().getSize(); i++) {
					Object item = listAmigos.getModel().getElementAt(i);
					if(item.toString() != nombreUsuario){
						modelAmigos.addElement(item.toString());
					}
				}
				return modelAmigos;
			}
		});
		btnAgregarAmigo.setBounds(211, 186, 48, 33);
		contentPane.add(btnAgregarAmigo);
		
		JButton btnRemoverAmigo = new JButton("<-");
		btnRemoverAmigo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Obtengo el usuario que se seleccion�
				String nombreUsuario = listAmigosAgregados.getSelectedValue().toString();
				// Hago el cambio entre la lista de amigos y la lista de amigos agregados
				// Lo saco de la lista de amigos y lo pongo en la lista de amigos agregados
				listAmigos.setModel(this.agregarAmigo(nombreUsuario));
				listAmigosAgregados.setModel(this.removerAmigo(nombreUsuario));
			}
			private DefaultListModel<String> agregarAmigo(String nombreUsuario){
				DefaultListModel<String> modelAmigos = new DefaultListModel<String>();
				boolean existe = false;
				// Recorro toda la lista de amigos agregados para que quede cargada con todos los elemtos que ten�a
				for(int i = 0; i < listAmigos.getModel().getSize(); i++) {
					Object item = listAmigos.getModel().getElementAt(i);
					modelAmigos.addElement(item.toString());
					// Valido que el usuario no est� ya agregado
					if(item.toString() == nombreUsuario){
						existe = true;
					}
				}
				if(!existe){
					modelAmigos.addElement(nombreUsuario);	
				}
				return modelAmigos;
			}
			private DefaultListModel<String> removerAmigo(String nombreUsuario){
				DefaultListModel<String> modelAmigos = new DefaultListModel<String>();
				// Vuelvo a cargar la lista de amigos con todos los amigos menos el que se agreg�
				for(int i = 0; i < listAmigosAgregados.getModel().getSize(); i++) {
					Object item = listAmigosAgregados.getModel().getElementAt(i);
					if(item.toString() != nombreUsuario){
						modelAmigos.addElement(item.toString());
					}
				}
				return modelAmigos;
			}
		});
		btnRemoverAmigo.setBounds(211, 230, 48, 33);
		contentPane.add(btnRemoverAmigo);
		
		JLabel lblNombreDelGrupo = new JLabel("NOMBRE DEL GRUPO: ");
		lblNombreDelGrupo.setBounds(10, 69, 191, 14);
		contentPane.add(lblNombreDelGrupo);
		
		txtNombreGrupo = new JTextField();
		txtNombreGrupo.setBounds(269, 66, 191, 20);
		contentPane.add(txtNombreGrupo);
		txtNombreGrupo.setColumns(10);
		
		JLabel lblSeleccionarAmigos = new JLabel("SELECCIONAR AMIGOS");
		lblSeleccionarAmigos.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeleccionarAmigos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSeleccionarAmigos.setBounds(10, 94, 461, 47);
		contentPane.add(lblSeleccionarAmigos);
		
		JButton btnCrearSala = new JButton("Crear Sala");
		btnCrearSala.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		btnCrearSala.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String nombre = txtNombreGrupo.getText();
				String emisor = ChatClient.getInstance().getUsuarioLogeado().getUser();
				List<String> usuarios = new ArrayList<String>();
				// Recorro toda la lista de los usuarios que se agregaron y los agrego a usuarios
				for(int i = 0; i < listAmigosAgregados.getModel().getSize(); i++) {
					Object item = listAmigosAgregados.getModel().getElementAt(i);
					usuarios.add(item.toString());
				}
				// Creo el grupo
				Grupo grupo = new Grupo(nombre, usuarios);
				// Creo el mensaje que se envia a todos los usuarios del grupo
				MensajeGrupo mensaje = new MensajeGrupo(grupo, emisor, "");
				// Valido que est� todo bien antes de mostrar la pantalla
				// Se valida que haya usuarios agregados y que tenga nombre el grupo
				if(!usuarios.isEmpty() && !nombre.isEmpty()){
					ClienteModSalaDeChat salaDeChat = new ClienteModSalaDeChat();
					salaDeChat.setVisible(true);	
				}
			}
		});
		btnCrearSala.setBounds(251, 419, 107, 23);
		contentPane.add(btnCrearSala);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		btnCancelar.setBounds(368, 419, 89, 23);
		contentPane.add(btnCancelar);
	}
	
	private DefaultListModel<String> obtenerListaAmigos() {
		DefaultListModel<String> modelAmigos = new DefaultListModel<String>();
		List<FriendStatus> amigos = ChatClient.getInstance().getAmigos();
		for (FriendStatus amigo : amigos) {
			if(amigo.getEstado() == 1)
				modelAmigos.addElement(amigo.getUsername());
		}
		return modelAmigos;
	}	
}
