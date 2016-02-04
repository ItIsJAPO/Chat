package common;

import java.io.Serializable;
 
/**
 * Fecha: 28-Enero-2016
 * 
 * Archivo BEAN para el intercambio de mensaje.
 * 
 * @author Jose Antonio Pino Ocampo
 * @autor Juan Carlos Almeyda Cruz
 *
 */

public class MensajeChat implements Serializable {

	private static final long serialVersionUID = 4715850049574929960L;

	private String destinatario;
	private String texto;

	/* Constructores */
	public MensajeChat(String destinatario,String texto) {
		this.destinatario = destinatario;
		this.texto = texto;
	}
	public MensajeChat() {
		this("","");
	}

	/* Metodos */


	/* Getters & Setters */
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

}
