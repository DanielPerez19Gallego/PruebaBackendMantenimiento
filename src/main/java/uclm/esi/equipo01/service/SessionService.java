package uclm.esi.equipo01.service;

import org.springframework.stereotype.Service;

import uclm.esi.equipo01.http.Manager;
import uclm.esi.equipo01.model.Client;

@Service
public class SessionService {

	public long dameIdCliente(String email) {
		Client client = Manager.get().getClientRepository().findByEmail(email);
		if (client != null)
			return client.getId();
		return 0;
	}

}
