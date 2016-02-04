package common;

import java.io.Serializable;

/**
 * Fecha: 28-Enero-2016
 * 
 * Archivo BEAN para el intercambio de datos de invitaciones.
 * 
 * @author Jose Antonio Pino Ocampo
 * @autor Juan Carlos Almeyda Cruz
 *
 */

public class MensajeInvitacion implements Serializable {

	private static final long serialVersionUID = 47158500574929960L;

	private String solicitante;
	private String invitado;

	/* Constructores */
	public MensajeInvitacion(String solicitante,String invitado) {
		this.solicitante = solicitante;
		this.invitado = invitado;
	}
	public MensajeInvitacion() {
		this("","");
	}

	/* Metodos */


	/* Getters & Setters */
	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	public String getInvitado() {
		return invitado;
	}
	public void setInvitado(String invitado) {
		this.invitado = invitado;
	}
}
