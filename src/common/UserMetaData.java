package common;
import java.util.*;


public class UserMetaData implements java.io.Serializable {

	private String user;
	private String password;
	private String apyn;
	private String mail;
	private Date fechaAlta;
	private Date fechaNacimiento;
	private int conectado;
	
	public UserMetaData()
	{
		this("","","","",Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),0);
	}
	
	public UserMetaData(String nickName,String passWord, String apyn, String mail, Date fechaAlta, Date fechaNacimiento, int conectado)
	{
		this.user = nickName;
		this.password = passWord;
		this.apyn = apyn;
		this.mail = mail;
		this.fechaAlta = fechaAlta;
		this.fechaNacimiento = fechaNacimiento;
		this.conectado = conectado;
	}
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApyn() {
		return apyn;
	}
	public void setApyn(String apyn) {
		this.apyn = apyn;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public int getConectado() {
		return conectado;
	}
	public void setConectado(int conectado) {
		this.conectado = conectado;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [user=" + user + ", password=" + password + ", apyn="
				+ apyn + ", mail=" + mail + ", fechaAlta=" + fechaAlta
				+ ", fechaNacimiento=" + fechaNacimiento + ", conectado="
				+ conectado + "]";
	}
	
}
