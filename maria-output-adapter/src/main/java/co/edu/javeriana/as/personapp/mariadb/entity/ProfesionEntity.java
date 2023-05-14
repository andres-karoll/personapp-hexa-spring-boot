package co.edu.javeriana.as.personapp.mariadb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aasanchez
 */
@Entity
@Table(name="profesion", catalog = "persona_db", schema = "")
@NamedQueries({ @NamedQuery(name = "ProfesionEntity.findAll", query = "SELECT p FROM ProfesionEntity p"),
		@NamedQuery(name = "ProfesionEntity.findById", query = "SELECT p FROM ProfesionEntity p WHERE p.id = :id"),
		@NamedQuery(name = "ProfesionEntity.findByNom", query = "SELECT p FROM ProfesionEntity p WHERE p.nom = :nom") })
public class ProfesionEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(nullable = false)
	private Integer id;
	@Basic(optional = false)
	@Column(nullable = false, length = 90)
	private String nom;
	@Lob
	@Column(length = 65535)
	private String des;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "profesion")
	private List<EstudiosEntity> estudios;

	public ProfesionEntity() {
	}

	public ProfesionEntity(Integer id) {
		this.id = id;
	}

	public ProfesionEntity(Integer id, String nom) {
		this.id = id;
		this.nom = nom;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public List<EstudiosEntity> getEstudios() {
		return estudios;
	}

	public void setEstudios(List<EstudiosEntity> estudiosList) {
		this.estudios = estudiosList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ProfesionEntity)) {
			return false;
		}
		ProfesionEntity other = (ProfesionEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ProfesionEntity [id=" + id + ", nom=" + nom + ", des=" + des + "]";
	}

}
