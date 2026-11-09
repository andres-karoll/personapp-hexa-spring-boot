package co.edu.javeriana.as.personapp.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;

@ExtendWith(MockitoExtension.class)
class PhoneUseCaseTest {

	private static final String NUMBER = "3001234567";

	@Mock
	private PhoneOutputPort phonePersintence;

	private PhoneUseCase phoneUseCase;

	private Phone phone;

	@BeforeEach
	void setUp() {
		phoneUseCase = new PhoneUseCase(phonePersintence);
		phone = new Phone(NUMBER, "Claro", new Person(123, "Ana", "Perez", Gender.FEMALE));
	}

	@Test
	void createSavesPhone() {
		when(phonePersintence.save(phone)).thenReturn(phone);

		assertSame(phone, phoneUseCase.create(phone));
	}

	@Test
	void editAssignsNumberAndSaves() throws NoExistException {
		Phone changes = new Phone("3109999999", "Movistar", phone.getOwner());
		when(phonePersintence.findById(NUMBER)).thenReturn(phone);
		when(phonePersintence.save(changes)).thenReturn(changes);

		Phone edited = phoneUseCase.edit(NUMBER, changes);

		assertEquals(NUMBER, edited.getNumber());
		verify(phonePersintence).save(changes);
	}

	@Test
	void editThrowsWhenPhoneDoesNotExist() {
		when(phonePersintence.findById(NUMBER)).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.edit(NUMBER, phone));
		verify(phonePersintence, never()).save(any());
	}

	@Test
	void dropDeletesExistingPhone() throws NoExistException {
		when(phonePersintence.findById(NUMBER)).thenReturn(phone);
		when(phonePersintence.delete(NUMBER)).thenReturn(true);

		assertTrue(phoneUseCase.drop(NUMBER));
	}

	@Test
	void dropThrowsWhenPhoneDoesNotExist() {
		when(phonePersintence.findById(NUMBER)).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.drop(NUMBER));
		verify(phonePersintence, never()).delete(any());
	}

	@Test
	void findAllAndCountUseOutputPort() {
		when(phonePersintence.find()).thenReturn(List.of(phone));

		assertEquals(List.of(phone), phoneUseCase.findAll());
		assertEquals(1, phoneUseCase.count());
	}

	@Test
	void findOneReturnsExistingPhone() throws NoExistException {
		when(phonePersintence.findById(NUMBER)).thenReturn(phone);

		assertSame(phone, phoneUseCase.findOne(NUMBER));
	}

	@Test
	void findOneThrowsWhenPhoneDoesNotExist() {
		when(phonePersintence.findById(NUMBER)).thenReturn(null);

		assertThrows(NoExistException.class, () -> phoneUseCase.findOne(NUMBER));
	}
}
