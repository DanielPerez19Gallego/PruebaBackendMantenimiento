package uclm.esi.equipo01.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import uclm.esi.equipo01.model.Telefono;

public interface TelefonoRepository extends MongoRepository<Telefono, Long> {

	Telefono findByEmailAndPwd(String email, String pwd);

}