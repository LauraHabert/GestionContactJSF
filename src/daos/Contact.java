package daos;

import java.util.Arrays;

public class Contact {
	
	private int id;
	private String nom;
	private String prenom;
	private String email;
	private Adresse adresse;
	private ContactGroupe[] groupe;
	private Telephone[] telephone;
	private String telephoneFormater;
	private String groupeFormater;
	private int numSiret;
	
	
	public Contact(int id)
	{
		this.id = id;
		this.nom = "";
		this.prenom = "";
		this.email = "";
		this.adresse = null;
		this.groupe = null;
		this.telephone = null;
		this.numSiret = -1;
	}
	public Contact(int id, String nom, String prenom, String email,Adresse adresse)
	{
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.adresse = adresse;
		this.groupe = null;
		this.telephone = null;
		this.numSiret = -1;
	}

	public Contact(int id, String nom, String prenom, String email,Adresse adresse, Telephone[] telephone)
	{
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.adresse = adresse;
		this.groupe = null;
		this.telephone = telephone;
		this.numSiret = -1;
	}
	
	public Contact(int id, String nom, String prenom, String email,Adresse adresse, Telephone[] telephone, ContactGroupe[] groupe)
	{
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.adresse = adresse;
		this.groupe = groupe;
		this.telephone = telephone;
		this.numSiret = -1;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactGroupe[] getGroupe() {
		return groupe;
	}

	public void setGroupe(ContactGroupe[] groupe) {
		this.groupe = groupe;
	}

	public Telephone[] getProfiles() {
		return telephone;
	}

	public void setProfiles(Telephone[] telephone) {
		this.telephone = telephone;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public String getAdresseFormater()
	{
		return adresse.toString();
	}
	
	public String getTelephoneFormater()
	{
		telephoneFormater="";
		if(telephone != null)
		{
			for(int i=0; i<telephone.length; i++)
			{
				if(i != 0)
					telephoneFormater += "\n";
				telephoneFormater += telephone[i].toString();
			}
		}
		return telephoneFormater;
	}
	public String getGroupeFormater()
	{
		groupeFormater ="";
		if(groupe != null)
		{
			for(int i=0; i<groupe.length; i++)
			{
				if(i != 0)
					groupeFormater += "\n";
				groupeFormater += groupe[i].toString();
			}
		}
		return groupeFormater;
	}
	public int getNumSiret() {
		return numSiret;
	}
	public void setNumSiret(int numSiret) {
		this.numSiret = numSiret;
	}
	
	public String getNumSiretFormater()
	{
		if(numSiret <0)
			return "";
		else
			return ""+numSiret;
	}
	@Override
	public String toString() {
		return "Contact [id=" + id + ", nom=" + nom + ", prenom=" + prenom
				+ ", email=" + email + ", adresse=" + adresse + ", groupe="
				+ Arrays.toString(groupe) + ", telephone="
				+ Arrays.toString(telephone) + "]";
	}
	
}
