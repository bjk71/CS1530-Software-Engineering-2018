import java.awt.*;
import java.io.*;
import javax.imageio.*;


public class Card implements Serializable {
	private String name;
	private transient Image face;
	private String value;
	private String suit;
	
	public Card(String name, Image face){
		this.name  = name.toUpperCase();
		this.face  = face;
		this.value = this.name.substring(0, name.length() - 1);
		this.suit  = this.name.substring(name.length() - 1);
	}

	public void reinitFace() {
		try {
			this.face = ImageIO.read(this.getClass().getResource("/" + this.name + ".png"));
		} catch (IOException ioex) {
			System.exit(1);
		}
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