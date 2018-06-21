/**
 * Stores integer value related to Player class bet. Positive value represents
 * a dollar amount, zero represents checking, and a negative value is folding.
 */
public class Action {

    private int value = 0;
    
    /**
     * Call constructor with value of 0 dollars (check).
     */
    public Action() {
        this(0);
    }

    /**
     * Constructor for Action object taking int for value as argument.
     */
    public Action(int v) {
        this.value = v;
    }

    /**
     * Set <b>this.value</b>.
     * @param  value Value to assign to this.
     * @return       Value assigned.
     */
    public int setValue(int value) {
        this.value = value;
        return this.value;
    }

    /**
     * Return private int value.
     * @return <b>this.value</b>
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Compare two Action objects.
     * @param  compare Value to compare <b>this.value</b> against.
     * @return Return int value, 1 if <b>this.value</b> is greater than <b>compare</b>, 
     * 0 if equal and -1 if less than.
     */
    public int compare(Action compare) {
        if(this.value > compare.getValue()) {
            return 1;
        } else if(this.value == compare.getValue()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Return string of Action value for printing to console.
     * @return String representation of <b>this.value</b>.
     */
    public String toString() {
        if(value == 0) {
            return "checked";
        } else if(value > 0) {
            return "bet $" + Integer.toString(this.value);
        } else {
            return "folded";
        }
    }
}