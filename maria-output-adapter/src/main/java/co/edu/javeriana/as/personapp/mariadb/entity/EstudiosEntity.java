package co.edu.javeriana.as.personapp.mariadb.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author aasanchez
 */
@Entity
@Table(name="estudios", catalog = "persona_db", schema = "")
@NamedQueries({ @NamedQuery(name = "EstudiosEntity.findAll", query = "SELECT e FROM EstudiosEntity e"),
		@NamedQuery(name = "EstudiosEntity.findByIdProf", query = "SELECT e FROM EstudiosEntity e WHERE e.estudiosEntityPK.idProf = :idProf"),
		@NamedQuery(name = "EstudiosEntity.findByCcPer", query = "SELECT e FROM EstudiosEntity e WHERE e.estudiosEntityPK.ccPer = :ccPer"),
		@NamedQuery(name = "EstudiosEntity.findByFecha", query = "SELECT e FROM EstudiosEntity e WHERE e.fecha = :fecha"),
		@NamedQuery(name = "EstudiosEntity.findByUniver", query = "SELECT e FROM EstudiosEntity e WHERE e.univer = :univer") })
public class EstudiosEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected EstudiosEntityPK estudiosEntityPK;
	private LocalDate fecha;
	@Column(length = 50)
	private String univer;
	@JoinColumn(name = "cc_per", referencedColumnName = "cc", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private PersonaEntity persona;
	@JoinColumn(name = "id_prof", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ProfesionEntity profesion;

	public EstudiosEntity() {
	}

	public EstudiosEntity(EstudiosEntityPK estudiosEntityPK) {
		this.estudiosEntityPK = estudiosEntityPK;
	}

	public EstudiosEntity(int idProf, int ccPer) {
		this.estudiosEntityPK = new EstudiosEntityPK(idProf, ccPer);
	}

	public EstudiosEntityPK getEstudiosPK() {
		return estudiosEntityPK;
	}

	public void setEstudiosPK(EstudiosEntityPK estudiosEntityPK) {
		this.estudiosEntityPK = estudiosEntityPK;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getUniver() {
		return univer;
	}

	public void setUniver(String univer) {
		this.univer = univer;
	}

	public PersonaEntity getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntity personaEntity) {
		this.persona = personaEntity;
	}

	public ProfesionEntity getProfesion() {
		return profesion;
	}

	public void setProfesion(ProfesionEntity profesionEntity) {
		this.profesion = profesionEntity;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (estudiosEntityPK != null ? estudiosEntityPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof EstudiosEntity)) {
			return false;
		}
		EstudiosEntity other = (EstudiosEntity) object;
		if ((this.estudiosEntityPK == null && other.estudiosEntityPK != null)
				|| (this.estudiosEntityPK != null && !this.estudiosEntityPK.equals(other.estudiosEntityPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EstudiosEntity [estudiosEntityPK=" + estudiosEntityPK + ", fecha=" + fecha + ", univer=" + univer + "]";
	}

}
