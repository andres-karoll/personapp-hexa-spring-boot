package co.edu.javeriana.as.personapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {
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
	@EqualsAndHashCode.Exclude
	private List<Phone> phoneNumbers;
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Study> studies;

	public Boolean isValidAge() {
		return this.age == null || this.age >= 0;
	}
}
