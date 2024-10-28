package co.edu.javeriana.as.personapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {

	//Aqui hice un cambio - Si ven este mensaje borrenlo. Gracias jeje
	@NonNull
	private Integer identification;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private Gender gender;
	private Integer age;
	@ToString.Exclude
	private List<Phone> phoneNumbers;
	@ToString.Exclude
	private List<Study> studies;

	public Integer getIdentification() {
		return identification;
	}

	public void setIdentification(Integer identification) {
		this.identification = identification;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public List<Phone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}

	public Boolean isValidAge() {
		return this.age >= 0;
	}

	public Person(@NonNull Integer identification, @NonNull String firstName, @NonNull String lastName, @NonNull Gender gender, Integer age) {
		this.identification = identification;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
	}
}
