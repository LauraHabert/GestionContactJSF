package daos;

public class Telephone {
	private int id;
	private String phoneKind;
	private String numTelephone;
	private Contact contact;
	
	
	public Telephone(int id, String phoneKind, String numTelephone,
			Contact contact) {
		this.id = id;
		this.phoneKind = phoneKind;
		this.numTelephone = numTelephone;
		this.contact = contact;
	}
	
	public Telephone(String phoneKind, String numTelephone) {
		this.id = 0;
		this.phoneKind = phoneKind;
		this.numTelephone = numTelephone;
		this.contact = null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneKind() {
		return phoneKind;
	}
	public void setPhoneKind(String phoneKind) {
		this.phoneKind = phoneKind;
	}
	public String getNumTelephone() {
		return numTelephone;
	}
	public void setNumTelephone(String numTelephone) {
		this.numTelephone = numTelephone;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return numTelephone+"\n"
			   +phoneKind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numTelephone == null) ? 0 : numTelephone.hashCode());
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
		Telephone other = (Telephone) obj;
		if (numTelephone == null) {
			if (other.numTelephone != null)
				return false;
		} else if (!numTelephone.equals(other.numTelephone))
			return false;
		return true;
	}
	
	
}
