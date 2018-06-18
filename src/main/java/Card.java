import java.awt.*;

public class Card{
    private String name;
    private Image  face;
    
    public Card(String name, Image face){
        this.name = name;
        this.face = face;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Image getFace(){
        return this.face;
    }
        
}