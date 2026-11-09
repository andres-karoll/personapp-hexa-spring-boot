package co.edu.javeriana.as.personapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Phone {
	@NonNull
	private String number;
	@NonNull
	private String company;
	@NonNull
	private Person owner;
}
