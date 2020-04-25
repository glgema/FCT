package application;

import java.sql.Date;

public class Proveedor {
	private String cif;
	private String razonSocial;
	private int registroNotarial;
	private String seguroResponsabilidad;
	private float seguroImporte;
	private Date fechaHomologacion;
	
	public Proveedor() {}

	public Proveedor(String cif, String razon, int regNotarial, String segResponsabilidad, float segImporte,
			Date fecHomologacion) {
		super();
		this.cif = cif;
		this.razonSocial = razon;
		this.registroNotarial = regNotarial;
		this.seguroResponsabilidad = segResponsabilidad;
		this.seguroImporte = segImporte;
		this.fechaHomologacion = fecHomologacion;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getRazon() {
		return razonSocial;
	}

	public void setRazon(String razon) {
		this.razonSocial = razon;
	}

	public int getRegNotarial() {
		return registroNotarial;
	}

	public void setRegNotarial(int regNotarial) {
		this.registroNotarial = regNotarial;
	}

	public String getSegResponsabilidad() {
		return seguroResponsabilidad;
	}

	public void setSegResponsabilidad(String segResponsabilidad) {
		this.seguroResponsabilidad = segResponsabilidad;
	}

	public float getSegImporte() {
		return seguroImporte;
	}

	public void setSegImporte(float segImporte) {
		this.seguroImporte = segImporte;
	}

	public Date getFecHomologacion() {
		return fechaHomologacion;
	}

	public void setFecHomologacion(Date fecHomologacion) {
		this.fechaHomologacion = fecHomologacion;
	}
	
}