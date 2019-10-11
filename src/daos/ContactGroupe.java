package daos;

public class ContactGroupe {
	private int groupeId;
	private String nomGroupe;
	private Contact[] contact;
	
	public ContactGroupe(int groupeId, String nomGroupe, Contact[] contact) {
		this.groupeId = groupeId;
		this.nomGroupe = nomGroupe;
		this.contact = contact;
	}
	
	public int getGroupeId() {
		return groupeId;
	}
	public void setGroupeId(int groupeId) {
		this.groupeId = groupeId;
	}
	public String getNomGroupe() {
		return nomGroupe;
	}
	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	public Contact[] getContact() {
		return contact;
	}

	public void setContact(Contact[] contact) {
		this.contact = contact;
	}
	
	public String toString()
	{
		return groupeId+"<br/>"+nomGroupe+"\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupeId;
		result = prime * result
				+ ((nomGroupe == null) ? 0 : nomGroupe.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactGroupe other = (ContactGroupe) obj;
		if (groupeId != other.groupeId)
			return false;
		if (nomGroupe == null) {
			if (other.nomGroupe != null)
				return false;
		} else if (!nomGroupe.equals(other.nomGroupe))
			return false;
		return true;
	}

}
