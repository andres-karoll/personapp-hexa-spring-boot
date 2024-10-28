package co.edu.javeriana.as.personapp.domain.pk;

import java.io.Serializable;

/**
 *
 * @author aasanchez
 */

public class EstudiosEntityPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int idProf;

	private int ccPer;

	public EstudiosEntityPK() {
	}

	public EstudiosEntityPK(int idProf, int ccPer) {
		this.idProf = idProf;
		this.ccPer = ccPer;
	}

	public int getIdProf() {
		return idProf;
	}

	public void setIdProf(int idProf) {
		this.idProf = idProf;
	}

	public int getCcPer() {
		return ccPer;
	}

	public void setCcPer(int ccPer) {
		this.ccPer = ccPer;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) idProf;
		hash += (int) ccPer;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof EstudiosEntityPK)) {
			return false;
		}
		EstudiosEntityPK other = (EstudiosEntityPK) object;
		if (this.idProf != other.idProf) {
			return false;
		}
		if (this.ccPer != other.ccPer) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EstudiosEntityPK [idProf=" + idProf + ", ccPer=" + ccPer + "]";
	}

}
