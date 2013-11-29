package client;

import groups.Grupo;
import interfaces.cliente.ClienteConversacion;
import interfaces.cliente.ClienteInicial;
import interfaces.cliente.UserLogin;
import interfaces.grupos.ClienteModSalaDeChat;
import interfaces.tateti.InterfazTateti;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import common.FriendStatus;
import common.Mensaje;
import common.MensajeChat;
import common.MensajeConsulta;
import common.MensajeGrupo;
import common.MensajeInvitacion;
import common.MensajeMovimiento;
import common.MensajePartida;
import common.MensajePizarra;
import common.MensajeSolicitudGrupo;
import common.UserMetaData;

import dataTier.BanInfo;

public class ChatClient {

	// Config
	private int port;
	private String serverIP;

	// Negrada
	private BanInfo banInfo;

	// Conexion / Auxiliar
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;
	private Socket socket;
	private static ChatClient chatClientInstance;
	private Map<Integer, Object> mapMensajes;
	// alive
	// private Alive alive;

	// User
	private UserMetaData usuarioLogeado;
	private ArrayList<FriendStatus> amigos;

	// Front
	private UserLogin frontEndLogIn;
	private ClienteInicial frontEnd;
	private Map<String, ClienteConversacion> mapaConversaciones;
	private Map<String, ClienteModSalaDeChat> mapaGrupos;
	private Map<String, ClienteModSalaDeChat> mapaGruposParaLista;
	private Map<String, InterfazTateti> mapaTaTeTi;


	/* Constructor */
	private ChatClient() {
		try {
			/* Cargo properties */
			loadProperties();
			socket = new Socket(this.serverIP, this.port);
			salida = new ObjectOutputStream(socket.getOutputStream());
			entrada = new ObjectInputStream(socket.getInputStream());
			new ListenFromServer().start();

			banInfo = new BanInfo(0, "");
			chatClientInstance = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ChatClient getInstance() {
		if (chatClientInstance == null)
			chatClientInstance = new ChatClient();
		return chatClientInstance;
	}

	public static void main(String args[]) {
		ChatClient.getInstance().go();
	}

	private void go() {
		/* Inicializo GUI de login */
		frontEndLogIn = new UserLogin();
		frontEndLogIn.mostrar();

		/* Lanzo Alive */
		// alive.start //se inicia cuando el login es satisfactorio
	}

	// -----------------
	// Metodos privados
	// -----------------
	private void loadProperties() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("ServerConfig.properties"));
			port = Integer.valueOf(prop.getProperty("port"));
			serverIP = prop.getProperty("ip");
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
			serverIP = "localhost";
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	private void enviarAlServer(Mensaje msg) {
		try {
			salida.writeObject(msg);
		} catch (IOException e) {
			System.err.println("Error tratando de escribir en el servidor: " + e);
		}
	}

	private void mostrarAlerta(String txtAlerta) {
		this.frontEnd.mostrarAlerta(txtAlerta);
	}

	private UserMetaData obtenerUsuario(UserMetaData usuarioVacio) {
		Mensaje msg = new Mensaje(Mensaje.OBTENER_USUARIO, usuarioVacio.getUser());
		try {
			enviarAlServer(msg);
			synchronized(mapMensajes) {
				mapMensajes.wait();
				return (UserMetaData)mapMensajes.remove(Mensaje.OBTENER_USUARIO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuarioVacio;
	}

	// -----------------
	// Metodos publicos para las pantallas del cliente
	// -----------------
	@SuppressWarnings("unchecked")
	public ClienteInicial login(UserMetaData userData) {
		// metodo de verificacion contra la base para realizar el login
		Mensaje msg = new Mensaje(Mensaje.LOG_IN, userData);
		try {
			this.enviarAlServer(msg);
			synchronized(mapMensajes){
				mapMensajes.wait();
				msg = (Mensaje)mapMensajes.remove(Mensaje.LOG_IN);
			}
			if (msg.getId()==Mensaje.ACCEPTED) {
				// alive.start??
				amigos = (ArrayList<FriendStatus>)msg.getCuerpo();
				usuarioLogeado = obtenerUsuario(userData);
	
				this.mapaConversaciones = new HashMap<String ,ClienteConversacion>();
				this.mapaGrupos = new HashMap<String, ClienteModSalaDeChat>();
				this.mapaTaTeTi = new HashMap<String, InterfazTateti>();
				this.frontEnd = new ClienteInicial();
				return frontEnd;
			} else if(msg.getId()==Mensaje.BANNED) {
				banInfo = (BanInfo)msg.getCuerpo();
				return null;
			} else if(msg.getId()==Mensaje.USUARIO_CONECTADO) {
				//TODO devuelve un mensaje de error explicando que el usuario esta conectado
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean verificarNombreUsuario(String nombre) {
		Mensaje msg = new Mensaje(Mensaje.VERIFICAR_USUARIO, nombre);
		try {
			enviarAlServer(msg);
			synchronized(mapMensajes){
				mapMensajes.wait();
				return (Boolean)mapMensajes.remove(Mensaje.VERIFICAR_USUARIO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void altaNuevoUsuario(UserMetaData user) {
		Mensaje msg = new Mensaje(Mensaje.ALTA_USUARIO, user);
		enviarAlServer(msg);
	}

	public void modificarUsuario(String nombre, String email, String tel, String pass) {
		usuarioLogeado.setApyn(nombre);
		usuarioLogeado.setMail(email);
		usuarioLogeado.setTelefono(tel);
		usuarioLogeado.setPassword(pass);
		Mensaje msg = new Mensaje(Mensaje.MODIFICACION_USUARIO, usuarioLogeado);
		enviarAlServer(msg);
	}

	@SuppressWarnings("unchecked")
	public List<String> buscarAmigoPorTexto(String texto) {
		Mensaje msg = new Mensaje(Mensaje.BUSCAR_USUARIO, texto);
		try {
			enviarAlServer(msg);
			synchronized(mapMensajes){
				mapMensajes.wait();
				return (List<String>)mapMensajes.remove(Mensaje.BUSCAR_USUARIO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public void enviarMensajeChat(String amigo, String texto) {
		Mensaje msg = new Mensaje(Mensaje.ENVIAR_MENSAJE, new MensajeChat(amigo, texto));
		enviarAlServer(msg);
	}

	public void invitarAmigo(String contacto) {
		Mensaje msg = new Mensaje(Mensaje.INVITAR_USUARIO, new MensajeInvitacion(usuarioLogeado.getUser(), contacto));
		enviarAlServer(msg);
	}

	public void aceptacionInvitacionAmigo(MensajeInvitacion msgInvitacion) {
		Mensaje msg = new Mensaje(Mensaje.ACEPTACION_INVITACION_AMIGO, msgInvitacion);
		enviarAlServer(msg);

		frontEnd.friendStatusChanged(msgInvitacion.getSolicitante(), 1);
	}

	public void close() {
		enviarAlServer(new Mensaje(Mensaje.CERRAR_SESION, null));
		try {
			entrada.close();
			salida.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	// Thread de escucha de mensajes del server
	class ListenFromServer extends Thread {
		public void run() {
			mapMensajes = new HashMap<Integer, Object>();
			while (true) {
				try {
					// Aca se debe se filtrar segun tipo de mensaje recibido
					Mensaje msg = (Mensaje) entrada.readObject();
					if (msg.getId() == Mensaje.ALERTA) {
						mostrarAlerta((String)msg.getCuerpo());
					} else if(msg.getId() == Mensaje.MENSAJE_INDIVIDUAL) {
						MensajeChat msgChat = (MensajeChat)msg.getCuerpo();		
						frontEnd.getNuevaConversacion(msgChat.getDestinatario(), msgChat.getTexto());
					} else if(msg.getId() == Mensaje.INVITAR_USUARIO) {
						MensajeInvitacion msgInvitacion = (MensajeInvitacion)msg.getCuerpo();		
						frontEnd.mostrarPopUpInvitacion(msgInvitacion);
					} else if(msg.getId() == Mensaje.CAMBIO_ESTADO) {
						frontEnd.friendStatusChanged(((FriendStatus)msg.getCuerpo()).getUsername(), ((FriendStatus)msg.getCuerpo()).getEstado());
					} else if(msg.getId() == Mensaje.INVITACION_JUEGO) {
						frontEnd.mostrarPopUpInvitacionJuego(msg);
					} else if(msg.getId() == Mensaje.INICIO_PARTIDA) {
						MensajePartida mp = (MensajePartida)msg.getCuerpo();
						mp.setPartida(frontEnd.mostrarTateti(msg));
						enviarAlServer(msg);
					} else if(msg.getId() == Mensaje.ENVIO_PARTIDA) {
						MensajePartida mp = (MensajePartida)msg.getCuerpo();
						mp.setPartida(frontEnd.mostrarTateti(msg));
						enviarAlServer(msg);
					} else if(msg.getId() == Mensaje.CANTIDAD_PARTIDAS_VALIDA) {
						enviarCantidadPartidas(msg);
					} else if(msg.getId() == Mensaje.ENVIO_PIZARRA) {
						enviarPizarra(msg);
					} else if(msg.getId() == Mensaje.ACTUALIZACION_PIZARRA) {
						//TODO Diego.
					} else if(msg.getId() == Mensaje.ENVIAR_MENSAJE_TATETI) {
						MensajeChat msgChat = (MensajeChat)msg.getCuerpo();
						frontEnd.getNuevaConversacionTateti(msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.MENSAJE_GRUPAL) {
						MensajeChat msgChat = (MensajeChat) msg.getCuerpo();
						frontEnd.getNuevaConversacion(Mensaje.MENSAJE_GRUPAL, msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.MENSAJE_GRUPAL_MODERADOR) {
						MensajeChat msgChat = (MensajeChat) msg.getCuerpo();
						frontEnd.setNuevoMensajeGrupo(msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.CERRAR_GRUPO) {
						MensajeChat msgChat = (MensajeChat) msg.getCuerpo();
						frontEnd.cerrarGrupo(msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.BANNED_GRUPO) {
						MensajeChat msgChat = (MensajeChat) msg.getCuerpo();
						frontEnd.cerrarGrupo(msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.DESBANEAR_GRUPO) {
						MensajeChat msgChat = (MensajeChat) msg.getCuerpo();
						frontEnd.desbannearUsuarioGrupo(msgChat.getDestinatario(), msgChat.getTexto());
					} else if (msg.getId() == Mensaje.SOLICITAR_UNION_GRUPO) {
						MensajeSolicitudGrupo msgInvitacion = (MensajeSolicitudGrupo) msg.getCuerpo();
						frontEnd.mostrarPopUpSolicitudGrupo(msgInvitacion);
					} else if(msg.getId()==Mensaje.AGREGAR_AMIGO_FRIENDLIST) {
						amigos.add(new FriendStatus((String)msg.getCuerpo(),1));
					} else if(msg.getId()==Mensaje.PUNTUACION) {
						frontEnd.mostrarPuntuacion((HashMap<String,Integer>)msg.getCuerpo());
					} else {
						synchronized(mapMensajes){
							mapMensajes.put(msg.getId(), msg.getCuerpo());
							mapMensajes.notify();
						}
					}
				} catch (IOException e) {
					System.out.println("El servidor ha finalizado la conexion.");
					System.exit(1);
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}

	// Getters
	public UserMetaData getUsuarioLogeado() {
		return usuarioLogeado;
	}
	public ArrayList<FriendStatus> getAmigos() {
		return amigos;
	}
	public Map<String, ClienteConversacion> getMapaConversaciones() {
		return mapaConversaciones;
	}
	public Map<String, InterfazTateti> getMapaTateti() {
		return mapaTaTeTi;
	}
	public Map<String, ClienteModSalaDeChat> getMapaGrupos() {
		return mapaGrupos;
	}
	public BanInfo getBanInfo() {
		return this.banInfo;
	}


	// Inicio: TATETI
	// TODO Diego
	public void invitarAmigoAJugar(String contacto) {
		Mensaje msg = new Mensaje(Mensaje.INVITACION_JUEGO, new MensajeInvitacion(usuarioLogeado.getUser(), contacto));
		enviarAlServer(msg);
	}

	public void aceptacionInvitacionJuego(Mensaje msg) {
		enviarAlServer(msg);
	}

	public void enviarPizarra(Mensaje msg) {
		MensajePizarra mp = (MensajePizarra)msg.getCuerpo();
		//String aux = mp.getJugador1();					// En este caso como estoy enviando pizarra creada por el invitado a jugar al usuario que inicio partida el nombre de los jugadores esta cambiado
		//mp.setJugador1(mp.getJugador2());
		//mp.setJugador2(aux);
		enviarAlServer(new Mensaje(Mensaje.RESPUESTA_PIZARRA,mp));		
	}

	public void actualizarPizarra(Mensaje msg) {
		MensajeMovimiento mm = (MensajeMovimiento)msg.getCuerpo();
		enviarAlServer(new Mensaje(Mensaje.RESPUESTA_ACTUALIZACION_PIZARRA,mm));
	}

	public void enviarMovimiento(MensajeMovimiento msg) {
		enviarAlServer(new Mensaje(Mensaje.MOVIMIENTO,msg));
	}

	public void enviarCantidadPartidas(Mensaje msg) {
		MensajeConsulta mc = (MensajeConsulta)msg.getCuerpo();
		//msg.setId(Mensaje.RESPUESTA_CONSULTA_PARTIDAS);
		//enviarAlServer(new Mensaje(Mensaje.RESPUESTA_CONSULTA_PARTIDAS,msg));
		enviarAlServer(new Mensaje(Mensaje.RESPUESTA_CONSULTA_PARTIDAS,mc));
	}

	public void enviarMensajeChatTaTeTi(String amigo, String texto) {
		Mensaje msg = new Mensaje(Mensaje.ENVIAR_MENSAJE_TATETI, new MensajeChat(amigo,texto));
		enviarAlServer(msg);
	}
	
	public void obtenerPuntuacion() {
		Mensaje msg = new Mensaje(Mensaje.PEDIR_PUNTUACION,this.usuarioLogeado.getUser());
		enviarAlServer(msg);
	}
	// Fin: TATETI


	// Inicio: GRUPOS
	public void crearGrupo(Grupo grupo) {
		grupo.setModerador(usuarioLogeado.getUser());
		Mensaje msg = new Mensaje(Mensaje.CREAR_GRUPO, new MensajeGrupo(grupo, usuarioLogeado.getUser(), ""));
		enviarAlServer(msg);
	}

	public void cerrarGrupo(String grupo, String mensaje) {
		Mensaje msg = new Mensaje(Mensaje.CERRAR_GRUPO, new MensajeGrupo(grupo, this.usuarioLogeado.getUser(), mensaje));
		enviarAlServer(msg);
	}

	public void enviarMensajeGrupo(String grupo, String mensaje) {

		Mensaje msg = new Mensaje(Mensaje.MENSAJE_GRUPAL, new MensajeGrupo(grupo, this.usuarioLogeado.getUser(), mensaje));
		enviarAlServer(msg);
	}

	// Envio mensaje a un usuario para o banearlo o desconectarlo
	public void enviarMensajeUsuarioDeGrupo(String grupo, String UsuarioDestino, int cod) {

		Mensaje msg = null;
		if (cod == Mensaje.DISCONNECT_GRUPO) {
			msg = new Mensaje(Mensaje.MENSAJE_USUARIO_GRUPO, new MensajeGrupo(grupo, this.usuarioLogeado.getUser(), UsuarioDestino, cod));
		} else if (cod == Mensaje.BANNED_GRUPO) {
			// TODO Setear Bann en la lista de clientes//
			msg = new Mensaje(Mensaje.MENSAJE_USUARIO_GRUPO, new MensajeGrupo(grupo, this.usuarioLogeado.getUser(), UsuarioDestino, cod));
		} else {
			// TODO Setear Bann en la lista de clientes//
			msg = new Mensaje(Mensaje.MENSAJE_USUARIO_GRUPO, new MensajeGrupo(grupo, this.usuarioLogeado.getUser(), UsuarioDestino, cod));
		}
		enviarAlServer(msg);
	}

	@SuppressWarnings("unchecked")
	public List<String> actualizarGrupos() {
		
		Mensaje msg = new Mensaje(Mensaje.OBTENER_GRUPOS, this.usuarioLogeado.getUser());
		try {
			enviarAlServer(msg);
			synchronized (mapMensajes) {
				mapMensajes.wait();
				return (List<String>) mapMensajes.remove(Mensaje.OBTENER_GRUPOS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public void solicitarUnionGrupo(String grupo) {
		Mensaje msg = new Mensaje(Mensaje.SOLICITAR_UNION_GRUPO, new MensajeSolicitudGrupo(grupo, this.usuarioLogeado.getUser()));
		try {
			enviarAlServer(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void aceptacionSolicitudUnionGrupo(MensajeSolicitudGrupo mensajeSolicitud) {
		//entra aca una vez q el moderador acepto la solicitud de adhesion
		//TODO continuar con la aceptacion de adhesion...
	}
	// Fin: GRUPOS

}
