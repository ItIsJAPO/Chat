package common;

import java.io.Serializable;

/**
 * Fecha: 28-Enero-2016
 * 
 * @author Jose Antonio Pino Ocampo
 * @autor Juan Carlos Almeyda Cruz
 *
 */

public class MensajeRespuestaInvitacion implements Serializable {
	
	private static final long serialVersionUID = 47158500504929960L;

	private String solicitante;
	private String invitado;
	private boolean acepto;

	/* Constructores */
	public MensajeRespuestaInvitacion(String solicitante,String invitado,boolean acepto) {
		this.solicitante = solicitante;
		this.invitado = invitado;
		this.acepto = acepto;
	}
	public MensajeRespuestaInvitacion() {
		this("","",false);
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
	public boolean isAcepto() {
		return acepto;
	}
	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}
}
