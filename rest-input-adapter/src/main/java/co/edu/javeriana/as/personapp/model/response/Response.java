package co.edu.javeriana.as.personapp.model.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Response {
	
	private String status;
	private String description;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDateTime date;

	public Response(String status, String description, LocalDateTime date) {
		super();
		this.status = status;
		this.description = description;
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	

}
