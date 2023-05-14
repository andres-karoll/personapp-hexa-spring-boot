package co.edu.javeriana.as.personapp.mariadb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author aasanchez
 */
@Entity
@Table(name="telefono", catalog = "persona_db", schema = "")
@NamedQueries({ @NamedQuery(name = "TelefonoEntity.findAll", query = "SELECT t FROM TelefonoEntity t"),
		@NamedQuery(name = "TelefonoEntity.findByNum", query = "SELECT t FROM TelefonoEntity t WHERE t.num = :num"),
		@NamedQuery(name = "TelefonoEntity.findByOper", query = "SELECT t FROM TelefonoEntity t WHERE t.oper = :oper") })
public class TelefonoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(nullable = false, length = 15)
	private String num;
	@Basic(optional = false)
	@Column(nullable = false, length = 45)
	private String oper;
	@JoinColumn(name = "duenio", referencedColumnName = "cc", nullable = false)
	@ManyToOne(optional = false)
	private PersonaEntity duenio;

	public TelefonoEntity() {
	}

	public TelefonoEntity(String num) {
		this.num = num;
	}

	public TelefonoEntity(String num, String oper) {
		this.num = num;
		this.oper = oper;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public PersonaEntity getDuenio() {
		return duenio;
	}

	public void setDuenio(PersonaEntity duenio) {
		this.duenio = duenio;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (num != null ? num.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TelefonoEntity)) {
			return false;
		}
		TelefonoEntity other = (TelefonoEntity) object;
		if ((this.num == null && other.num != null) || (this.num != null && !this.num.equals(other.num))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TelefonoEntity [num=" + num + ", oper=" + oper + "]";
	}

}
