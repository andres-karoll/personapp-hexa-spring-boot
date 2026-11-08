package co.edu.javeriana.as.personapp.common.setup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;

class PersistenceRegistryTest {

	@Test
	void resolvesPortRegisteredForEachEngineIgnoringCase() throws Exception {
		Object maria = new Object();
		Object mongo = new Object();
		PersistenceRegistry<Object> registry = new PersistenceRegistry<Object>()
				.register(DatabaseOption.MARIA, maria).register(DatabaseOption.MONGO, mongo);

		assertThat(registry.resolve("maria")).isSameAs(maria);
		assertThat(registry.resolve("MONGO")).isSameAs(mongo);
	}

	@Test
	void rejectsUnknownEngine() {
		PersistenceRegistry<Object> registry = new PersistenceRegistry<>();

		assertThatThrownBy(() -> registry.resolve("oracle")).isInstanceOf(InvalidOptionException.class);
	}

	@Test
	void rejectsEngineWithoutRegisteredAdapter() {
		PersistenceRegistry<Object> registry = new PersistenceRegistry<Object>()
				.register(DatabaseOption.MARIA, new Object());

		assertThatThrownBy(() -> registry.resolve("mongo")).isInstanceOf(InvalidOptionException.class);
	}

	@Test
	void displayNamesAreUsedInResponses() {
		assertThat(DatabaseOption.displayNameOf("MARIA")).isEqualTo("MariaDB");
		assertThat(DatabaseOption.displayNameOf("mongo")).isEqualTo("MongoDB");
		assertThat(DatabaseOption.displayNameOf("x")).isEqualTo("x");
	}
}
