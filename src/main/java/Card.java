import java.awt.Image;
import java.io.Serializable;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Class representing a standard playing deck card
 */
public class Card implements Serializable {
	private String name;
	private transient Image face;
	private String value;
	private String suit;
	private Boolean inWinningHand;
	
	/**
	 * Constructor
	 * @param Name	String name of the card
	 * @param face	Image of card face
	 */
	public Card(String name, Image face){
		this.name  = name.toUpperCase();
		this.face  = face;
		this.value = this.name.substring(0, name.length() - 1);
		this.suit  = this.name.substring(name.length() - 1);
		this.inWinningHand = false;
	}

	/**
	 * Reinitializes the images of a card after loading from a save game
	 */
	public void reinitFace() {
		try {
			this.face = ImageIO.read(this.getClass().getResource("/" + this.name + ".png"));
		} catch (IOException ioex) {
			System.exit(1);
		}
	}

	/**
	 * Gets the name of the card
	 * @return	Card name string
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets image of the card
	 * @return 	Card face image
	 */
	public Image getFace() {
		return this.face;
	}

	/**
	 * Gets String representation of the rank of the card
	 * @return 	String card rank
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Gets integer representation of the rank of the card
	 * @return	Integer card rank
	 */
	public int getNumericValue() {
		int ret;
		switch (this.getValue()) {
			case "A":
				ret = 14;
				break;
			case "K":
				ret = 13;
				break;
			case "Q":
				ret = 12;
				break;
			case "J":
				ret = 11;
				break;
			case "10":
				ret = 10;
				break;
			case "9":
				ret = 9;
				break;
			case "8":
				ret = 8;
				break;
			case "7":
				ret = 7;
				break;
			case "6":
				ret = 6;
				break;
			case "5":
				ret = 5;
				break;
			case "4":
				ret = 4;
				break;
			case "3":
				ret = 3;
				break;
			case "2":
				ret = 2;
				break;
			default:
				ret = -1;
				break;
		}
		return ret;
	}

	public String getSuit(){
		return this.suit;
	}

	public void setInWinningHand(Boolean inWinningHand){
		this.inWinningHand = inWinningHand;
	}
	
	public Boolean isInWinningHand(){
		return this.inWinningHand;
	}

	/**
	 * Compares the rank of two cards
	 * @param comp  Card to compare to
	 * @return		Integer representation of card relationship
	 * 				 1 = greater than
	 * 				 0 = equal
	 * 				-1 = less than
	 */
	public int compareTo(Card comp) {
		int a = this.getNumericValue();
		int b = comp.getNumericValue();

		if (a > b) {
			return 1;
		} else if (a < b) {
			return -1;
		}

		return 0;

	}
}