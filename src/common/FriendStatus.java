package common;

import java.io.Serializable;

/**
 * Fecha: 28-Enero-2016
 * 
 * Archivo BEAN para el intercambio de status.
 * 
 * @author Jose Antonio Pino Ocampo
 * @autor Juan Carlos Almeyda Cruz
 *
 */
public class FriendStatus implements Serializable {
	
	private static final long serialVersionUID = 6924714071045481202L;

	private String username;
	private int estado;
	 
	/* Constructor */
	public FriendStatus(String username, int estado){
		this.username = username;
		this.estado = estado;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
}
