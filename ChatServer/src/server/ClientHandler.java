package server;

import java.io.*;
import java.net.*;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

import common.*;
import events.*;

public class ClientHandler extends Thread{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket client;
    private String IP;
    private Mensaje msg;
    private String user;
    private EventListenerList listenerList;
    private int estado;
  
    /* Constructores */
    public ClientHandler(Socket client, String user, ObjectInputStream in, ObjectOutputStream out)
    {
        this.client = client;
        this.in = in;
        this.out = out;
        this.user = user;
        estado = 1;
        
        IP = client.getInetAddress().toString();
        IP = IP.replace("/"," ");
        IP = IP.trim();
    }
    
    /* Run */
    @Override
    public void run()
    {
        try{
            client.setSoTimeout(10000); //10 seg
    		/* TODO evento de inicio sesion */
            dispatchEvent(new StatusChangedEvent(this,user,estado));
            /* Inicio escucha al cliente */
            while(client.isConnected())
            {
              msg = (Mensaje)in.readObject();		// se traba ac� hasta que hay mensaje
              
              //TODO: switch con cada constante de mensaje que ejecute un metodo privado.
              switch(msg.getId()){
              case Mensaje.ENVIAR_MENSAJE: break;
              case Mensaje.BUSCAR_USUARIO: break;
              case Mensaje.INVITAR_USUARIO: break;
              }
              
            }
        }
        
        catch (SocketTimeoutException e){
           /* TODO "connection lost"
            msg = new Mensaje("logout","",user);
            removeUser(out);
            serverFrame.removeUser(user);
            userList.remove(user);
            console.write(user + " disconnected.");
            msg.setMessage("disconnected.");
            serverFrame.writeMessage(msg);
            sayToAll(msg);
            try{
            client.close(); */
        } 
        
        catch (IOException ioe) { 
            	ioe.printStackTrace(System.err); 
        }    
        
        catch (Exception e) 
        { 
            System.err.println(e.getMessage());
            //removeUser(out);
        }
    }
    
    /* Metodos */
    
    
    /* Desconectar al cliente*/
    public void close(){
    	//TODO: almacenar la informaci�n, y desconectar al cliente del servidor.
    }

    /* Eventos */
    public void addEventListener(ClientEventListener e){
    	listenerList.add(ClientEventListener.class, e);
    }
    
    public void removeEventListener(ClientEventListener e){
    	listenerList.remove(ClientEventListener.class, e);
    }
    
    private void dispatchEvent(EventObject e){
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = 0; i < listeners.length; i = i+2) {
    		if (listeners[i] == ClientEventListener.class) {
    	        ((ClientEventListener) listeners[i+1]).statusChanged(e);
    		}
    	}
    }
    
    /* Getters & Setters */
    public String getUser()
    {
        return user;
    }
    
    public String getIP()
    {
        return IP;
    }
      
}
