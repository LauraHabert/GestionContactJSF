package services;

import java.util.ArrayList;

import daos.Contact;
import daos.ContactDAO;
import daos.Entreprise;

public class ContactService {
	
	public ContactService()
	{
		
	}

	public int createContact(Contact c, Entreprise entreprise)
	{
		ContactDAO daoC = new ContactDAO();
		return daoC.createContact(c,entreprise);
	}

	public ArrayList<Contact> researchContact(String nom, String prenom) {
		ContactDAO daoC = new ContactDAO();
		return daoC.researchContact(nom,prenom);
		
	}

	public boolean updateContact(Contact contact) {
		ContactDAO daoC = new ContactDAO();
		return daoC.updateContact(contact);
		
	}
	public boolean updateEntreprise(int idContact, int numSiret)
	{
		ContactDAO daoC = new ContactDAO();
		return daoC.updateEntreprise(idContact, numSiret);		
	}
	public boolean deleteContact(int id) {
		ContactDAO daoC = new ContactDAO();
		return daoC.deleteContact(id);
	}
	
	public ArrayList<Entreprise> getAllEntreprise()
	{
		ContactDAO daoC = new ContactDAO();
		return daoC.getAllEntreprise();	
	}
}
