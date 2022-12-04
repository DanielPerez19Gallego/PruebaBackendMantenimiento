package uclm.esi.equipo01.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.openjson.JSONObject;

import uclm.esi.equipo01.http.Manager;
import uclm.esi.equipo01.model.Admin;
import uclm.esi.equipo01.model.Client;
import uclm.esi.equipo01.model.Rider;
import uclm.esi.equipo01.model.Telefono;

@Service
public class SessionService {

	public ResponseEntity<String> dameRol(JSONObject jso) {
		String email = jso.getString("email");
		String pwd = jso.getString("pwd");
		Client client = Manager.get().getClientRepository().findByEmailAndPwd(email, pwd);
		if (client !=null)
			return new ResponseEntity<>("CLIENT", HttpStatus.OK);
		Rider rider = Manager.get().getRiderRepository().findByEmailAndPwd(email, pwd);
		if (rider !=null)
			return new ResponseEntity<>("RIDER", HttpStatus.OK);
		Admin admin = Manager.get().getAdminRepository().findByEmailAndPwd(email, pwd);
		if (admin !=null)
			return new ResponseEntity<>("ADMIN", HttpStatus.OK);
		
		return new ResponseEntity<>("NADA", HttpStatus.OK);

	}

	public ResponseEntity<String> dameIdCliente(String email) {
		Client client = Manager.get().getClientRepository().findByEmail(email);
		if (client != null)
			return new ResponseEntity<>(client.getId()+"", HttpStatus.OK);
		return new ResponseEntity<>("No existe ese email", HttpStatus.OK);
	}
	/*
	public void creaTel() {
		String pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex("Ho123456.");
		Telefono tele = new Telefono("atencion@telefono.com", pwd, "telefono", "atencion", "123456789");
		Manager.get().getTeleRepository().save(tele);
	}*/
}
