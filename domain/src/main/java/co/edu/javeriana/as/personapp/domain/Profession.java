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
public class Profession {
	@NonNull
	private Integer identification;
	@NonNull
	private String name;
	private String description;
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Study> studies;
}
