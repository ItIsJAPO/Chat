package common;

import java.io.Serializable;

/**
 * Fecha: 28-Enero-2016
 * 
 * Archivo BEAN para el intercambio de solicitudes.
 * 
 * @author Jose Antonio Pino Ocampo
 * @autor Juan Carlos Almeyda Cruz
 *
 */

public class MensajeConsulta implements Serializable{

	private static final long serialVersionUID = 8662775367502947227L;

	/* Atributos */
	private String solicitante;
	private String invitado;
	private boolean cantValida;
	private int idPizzaraSolicitante;

	/* Constructores */
	public MensajeConsulta() {
		this("","",false,0);
	}
	
	public MensajeConsulta(String sol, String inv, boolean valido,int id) {
		this.solicitante = sol;
		this.invitado = inv;
		this.cantValida = valido;
		this.idPizzaraSolicitante = id;
	}
	
	/* Metodos */
	
	/* Getters && Setters */
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

	public boolean isCantValida() {
		return cantValida;
	}

	public void setCantValida(boolean cantValida) {
		this.cantValida = cantValida;
	}

	public int getIdPizzaraSolicitante() {
		return idPizzaraSolicitante;
	}

	public void setIdPizzaraSolicitante(int idPizzaraSolicitante) {
		this.idPizzaraSolicitante = idPizzaraSolicitante;
	}
	
}
