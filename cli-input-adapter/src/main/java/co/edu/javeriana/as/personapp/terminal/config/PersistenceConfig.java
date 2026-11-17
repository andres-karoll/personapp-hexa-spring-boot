package co.edu.javeriana.as.personapp.terminal.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.common.setup.PersistenceRegistry;

/** Registra, por cada agregado, el puerto de salida de cada motor de persistencia. */
@Configuration
public class PersistenceConfig {

	@Bean
	public PersistenceRegistry<PersonOutputPort> personRegistry(@Qualifier("personOutputAdapterMaria") PersonOutputPort maria,
			@Qualifier("personOutputAdapterMongo") PersonOutputPort mongo) {
		return new PersistenceRegistry<PersonOutputPort>().register(DatabaseOption.MARIA, maria)
				.register(DatabaseOption.MONGO, mongo);
	}

	@Bean
	public PersistenceRegistry<ProfessionOutputPort> professionRegistry(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort maria,
			@Qualifier("professionOutputAdapterMongo") ProfessionOutputPort mongo) {
		return new PersistenceRegistry<ProfessionOutputPort>().register(DatabaseOption.MARIA, maria)
				.register(DatabaseOption.MONGO, mongo);
	}

	@Bean
	public PersistenceRegistry<PhoneOutputPort> phoneRegistry(@Qualifier("phoneOutputAdapterMaria") PhoneOutputPort maria,
			@Qualifier("phoneOutputAdapterMongo") PhoneOutputPort mongo) {
		return new PersistenceRegistry<PhoneOutputPort>().register(DatabaseOption.MARIA, maria)
				.register(DatabaseOption.MONGO, mongo);
	}

	@Bean
	public PersistenceRegistry<StudyOutputPort> studyRegistry(@Qualifier("studyOutputAdapterMaria") StudyOutputPort maria,
			@Qualifier("studyOutputAdapterMongo") StudyOutputPort mongo) {
		return new PersistenceRegistry<StudyOutputPort>().register(DatabaseOption.MARIA, maria)
				.register(DatabaseOption.MONGO, mongo);
	}
}
