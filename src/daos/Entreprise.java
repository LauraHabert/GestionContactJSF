package daos;

import java.util.ArrayList;
import java.util.Iterator;

public class Entreprise {
	private int numSiret;
	private ArrayList<Contact> listeContact;

	public Entreprise(int numSiret) {
		this.numSiret = numSiret;
		this.listeContact = new ArrayList<Contact>();
	}

	public Entreprise(int numSiret,ArrayList<Contact> listeContact) {
		this.numSiret = numSiret;
		this.listeContact = listeContact;
	}
	
	public int getNumSiret() {
		return numSiret;
	}

	public void setNumSiret(int numSiret) {
		this.numSiret = numSiret;
	}
	
	public boolean addContact(Contact c)
	{
		return listeContact.add(c);
	}
	
	public Contact getContact(int index)
	{
		Iterator it = listeContact.iterator();
		if(index <0)
			return null;
		for(int i=0;i<index; i++)
		{
			try
			{
				it.next();
			}catch(NullPointerException n)
			{
				return null;
			}
		}
		return (Contact) it.next();
	}
	
	public boolean contactPresent(int idContact)
	{
		Iterator<Contact> it = listeContact.iterator();
		while(it.hasNext())
		{
			if(it.next().getId() == idContact)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Entreprise [numSiret=" + numSiret + ", listeContact="
				+ listeContact + "]";
	}
	
	
}
