import java.awt.*;

public class Card{
	private String name;
	private Image  face;
	private String value;
	private String suit;
	
	public Card(String name, Image face){
		this.name  = name.toUpperCase();
		this.face  = face;
		this.value = name.substring(0, name.length() - 1).toUpperCase();
		this.suit  = name.substring(name.length() - 1).toUpperCase();
	}
	
	public String getName(){
		return this.name;
	}
	
	public Image getFace(){
		return this.face;
	}

	public String getValue(){
		return this.value;
	}

	public String getSuit(){
		return this.suit;
	}
		
}