package server;

import interfaces.servidor.Principal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import groups.ClienteGrupo;
import groups.Grupo;
import common.Mensaje;
import common.MensajeGrupo;
import common.UserMetaData;
import dataTier.DataAccess;

public class ChatServer {

	private static ChatServer chatServerInstance;
	private HashMap<String, ClientHandler> handlerList;
	private HashMap<String, Grupo> grupoMap;
	private int port;
	private Principal frontEnd;

	/* Constructor */
	private ChatServer() {
		handlerList = new HashMap<String, ClientHandler>();
		grupoMap = new HashMap<String, Grupo>();
		/* Cargo properties */
		loadProperties();
		chatServerInstance = this;
	}
	public static ChatServer getInstance() {
		if (chatServerInstance == null)
			chatServerInstance = new ChatServer();
		return chatServerInstance;
	}

	/* Main */
	public static void main(String args[]) {
		ChatServer.getInstance().go();
	}
	private void go() {
		/* Lanzamiento de handler de manejo de informacion de GUI */
		frontEnd = new Principal();
		frontEnd.setResizable(false);
		frontEnd.setVisible(true);

		/* Espera de conexiones */
		new ConnectionAccepter(port).run();	
		
		// Los connection listener son lanzados por el Accepter
		// new ConnectionListener(port, handlerList).run();
	}


	//-----------------
	// Metodos privados
	//-----------------
	private void loadProperties() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("ServerConfig.properties"));
			port = Integer.valueOf(prop.getProperty("port"));
		} catch (FileNotFoundException e1) {
			// Properties no existe => creo uno
			prop.setProperty("port", "16016");
			prop.setProperty("ip", "localhost");
			try {
				prop.store(new FileOutputStream("ServerConfig.properties"), null);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			port = 16016;
		} catch (IOException e3) {
			e3.printStackTrace();
		}
	}


	//-----------------
	// Metodos publicos para las pantallas del cliente
	//-----------------
	public List<UserMetaData> obtenerUsuarios() {
		return DataAccess.getInstance().getAllUsers();
	}

	public void enviarAlerta(String textoAlerta, String usuarioDestino) {
		//Se debe tomar q si el usuarioDestino es null entonces es una alerta general
		if(usuarioDestino == null) {
			this.logearEvento("Server :: Alerta general: " + textoAlerta);
			for(Map.Entry<String, ClientHandler> entry : this.handlerList.entrySet()) {
				ClientHandler client = (ClientHandler)entry.getValue();
				client.enviarAlerta(textoAlerta);
			}
		} else {
			this.logearEvento("Server :: Alerta para " + usuarioDestino + ": " + textoAlerta);
			this.handlerList.get(usuarioDestino).enviarAlerta(textoAlerta);
		}
	}

	public UserMetaData obtenerInfoUsuario(String nombreUsuario) {
		return DataAccess.getInstance().getUserByUsername(nombreUsuario);
	}

	public List<String> obtenerHistorialLoginUsuario(String nombreUsuario) {
		return DataAccess.getInstance().getLoginHistory(nombreUsuario);
	}

	public void blanquearClave(String nombreUsuario) {
		DataAccess.getInstance().blanquearClave(nombreUsuario, nombreUsuario);
		this.logearEvento("Server :: Se blanqueo la clave para el usuario: " + nombreUsuario);
	}

	public void penalizar(String nombreUsuario, int horas, String motivo) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, horas);
		DataAccess.getInstance().penalizar(nombreUsuario, motivo, c.getTime().getTime());
		this.logearEvento("Server :: Se penalizo al usuario: " + nombreUsuario + " por: " + horas + " hs");
	}

	public void despenalizar(String nombreUsuario) {
		if(DataAccess.getInstance().checkBan(nombreUsuario) != null) {
			DataAccess.getInstance().despenalizar(nombreUsuario);
			this.logearEvento("Server :: Se despenalizo al usuario: " + nombreUsuario);
		} else {
			this.logearEvento("Server :: El usuario " + nombreUsuario + " no se encuentra penalizado");
		}
	}

	public void desconectarUsuario(String nombreUsuario) {
		this.logearEvento("Server :: Se desconecta al usuario " + nombreUsuario);
		this.handlerList.get(nombreUsuario).cerrarSesion();
		actualizarUsuarios();
	}	

	public boolean cerrarServer() {
		this.logearEvento("Server :: Se cierra el servidor");
		try {
			for(Map.Entry<String, ClientHandler> entry : this.handlerList.entrySet()) {
				this.handlerList.get(entry.getKey()).cerrarSesion();
			}
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	public void logearEvento(String mensaje) {
		this.frontEnd.logearEvento(mensaje);
	}

	public void actualizarUsuarios() {
		this.frontEnd.actualizarListaUsuarios();
	}

	//-----------------
	// Getters & Setters
	//-----------------	
	public HashMap<String, ClientHandler> getHandlerList() {
		return handlerList;
	}


	// Inicio: GRUPOS
	public void crearGrupo(MensajeGrupo mensajeGrupo) {
		Grupo grupo = mensajeGrupo.getGrupo();
		grupoMap.put(grupo.getNombre(), grupo);
		for (ClienteGrupo usuario : grupo.getUsuarios()) {
			ClientHandler handler = handlerList.get(usuario.getNombre());
			handler.enviarMensajeChat(Mensaje.MENSAJE_GRUPAL, grupo.getNombre(), grupo.getModerador() + " ha creado la sala de chat.");
		}
	}

	public void enviarMensajeGrupo(MensajeGrupo mensajeGrupo) {
		Grupo grupo = grupoMap.get(mensajeGrupo.getNombreGrupo());
		grupoMap.put(grupo.getNombre(), grupo);
		for (ClienteGrupo usuario : grupo.getUsuarios()) {
			ClientHandler handler = handlerList.get(usuario.getNombre());
			handler.enviarMensajeChat(Mensaje.MENSAJE_GRUPAL, grupo.getNombre(), mensajeGrupo.getEmisor() + " dice: " + mensajeGrupo.getMensaje());
		}
		if (!mensajeGrupo.getEmisor().equals(grupo.getModerador())) {
			ClientHandler handler = handlerList.get(grupo.getModerador());
			handler.enviarMensajeChat(Mensaje.MENSAJE_GRUPAL_MODERADOR, grupo.getNombre(), mensajeGrupo.getEmisor() + " dice: " + mensajeGrupo.getMensaje());
		}
	}

	public void enviarMensajeUsuarioEnGrupo(MensajeGrupo mensaje) {
		Grupo grupo = grupoMap.get(mensaje.getNombreGrupo());

		if (mensaje.getCodigoMensaje() == Mensaje.DISCONNECT_GRUPO) {
			for (ClienteGrupo usuario : grupo.getUsuarios()) {
				// Desconecto//
				if (usuario.getNombre().equals(mensaje.getDestinatarioIndividual())) {
					ClientHandler handler = handlerList.get(usuario.getNombre());
					
					List<ClienteGrupo> usuariosEnGrupo = grupo.getUsuarios();
					usuariosEnGrupo.get(usuariosEnGrupo.indexOf(usuario)).setOnline(false);
					handler.enviarMensajeChat(Mensaje.CERRAR_GRUPO, grupo.getNombre(), mensaje.getEmisor() + " te ha desconectado del Chat.");
					grupo.getUsuarios().remove(usuario);
				}
			}
		} else if (mensaje.getCodigoMensaje() == Mensaje.BANNED_GRUPO) {
			for (ClienteGrupo usuario : grupo.getUsuarios()) {
				
				if (usuario.getNombre().equals(mensaje.getDestinatarioIndividual())) {
					ClientHandler handler = handlerList.get(usuario.getNombre());
					
					List<ClienteGrupo> usuariosEnGrupo = grupo.getUsuarios();
					usuariosEnGrupo.get(usuariosEnGrupo.indexOf(usuario)).setOnline(false);
					usuariosEnGrupo.get(usuariosEnGrupo.indexOf(usuario)).setBaneado(true);
					
					handler.enviarMensajeChat(Mensaje.CERRAR_GRUPO, grupo.getNombre(), mensaje.getEmisor() + " te ha banneado y te ha desconectado del Chat.");
					
				}
			}
		}
	}

	public void cerrarGrupo(MensajeGrupo mensajeGrupo) {
		Grupo grupo = grupoMap.get(mensajeGrupo.getNombreGrupo());
		grupoMap.put(grupo.getNombre(), grupo);
		for (ClienteGrupo usuario : grupo.getUsuarios()) {
			ClientHandler handler = handlerList.get(usuario.getNombre());
			handler.enviarMensajeChat(Mensaje.CERRAR_GRUPO, grupo.getNombre(), mensajeGrupo.getEmisor() + mensajeGrupo.getMensaje());
		}
		grupoMap.remove(grupo.getNombre());
	}

	public void actualizarGrupos(String userName) {
		List<String> grupo = new ArrayList<String>();
		Iterator<Entry<String, Grupo>> it = grupoMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Grupo> group = (Map.Entry<String, Grupo>) it.next();
			grupo.add(((Grupo)group.getValue()).getNombre());
			ClientHandler handler = handlerList.get(userName);
			//handler.enviarMensajeChat(Mensaje.OBTENER_GRUPOS, emisor, texto)
			//TODO: Enviar mensaje al cliente de cuales son los grupos online.
		}
	}

	//TODO pedir grupos//
	//TODO controlar ingreso a grupo
	// Fin: GRUPOS

	//Inicio: TATETI
	
	public HashMap<String,Integer> obtenerPuntacionPorUsuario(String nombreUsuario)
	{
		try
		{
		ResultSet rs = DataAccess.getInstance().getPuntajeByUser(obtenerInfoUsuario(nombreUsuario));
		HashMap<String,Integer> map = new HashMap();
		rs.next();
		map.put("Ganados", rs.getInt("GANADOS"));
		map.put("Empatados", rs.getInt("EMPATADOS"));
		map.put("Perdidos", rs.getInt("PERDIDOS"));
		return map;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public String[][] obtenerPuntuaciones()
	{
		String[][] matPuntuaciones = null;
		try{
		ResultSet rs = DataAccess.getInstance().getPuntajes();
		matPuntuaciones = new String[rs.getFetchSize()][4];
		int cont = 0;
		while(rs.next())
		{
			matPuntuaciones[cont][0] = rs.getString(0);
			matPuntuaciones[cont][1] = rs.getString(1);
			matPuntuaciones[cont][2] = rs.getString(2);
			matPuntuaciones[cont][3] = rs.getString(3);
			cont++;
		}
		return matPuntuaciones;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return matPuntuaciones;
		}
	}
	//Fin: TATETI
}
