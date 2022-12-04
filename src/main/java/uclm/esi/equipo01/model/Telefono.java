package uclm.esi.equipo01.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import uclm.esi.equipo01.http.Manager;

@Document(collection = "Telefono")
public class Telefono extends User{

	@Transient
	public static final int SEQUENCE_ID = Sequence.TELEFONO.getValue();
	
	private String numero;
	
	public Telefono() {
		super();
	}
	
	public Telefono(String email, String pwd, String name, String surname, String numero) {
		super(email, pwd, name, surname);
		super.setId(Manager.get().generateSequence(SEQUENCE_ID));
		this.setNumero(numero);
	}
	
	@Override
	public boolean isActiveAccount() {
		return true;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
