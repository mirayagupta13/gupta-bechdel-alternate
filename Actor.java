/**
 * Represents an object of type Actor. An Actor has a name and a gender.
 *
 * @author Miraya and Natasha [and Stella]
 * @version 15/12/22
 */
public class Actor
{
    private String name;
    private String gender;
    
    /**
     * Constructor for objects of class Actor
     */
    public Actor(String actorName, String actorGender)
    {
        // initialise instance variables
        name = actorName;
        gender = actorGender;
    }

    /**
     * getName() is a Getter for name
     *
     * @return    name of actor
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * setName() is a Setter for name
     *
     *@param    name    String name to be set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * getGender() is a Getter for gender
     *
     * @return    gender of actor
     */
    public String getGender()
    {
        return gender;
    }
    
    /**
     * setGender() is a Setter for gender
     *
     * @param   gender  String gender to be set
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }
    
    /**
     * Method toString() returns a string representation of the actor
     *
     * @return a string representation of the actor
     */
    public String toString(){
        return "Actor " + name + " is " + gender; 
    }
    

    /**
     * This method is defined here because Actor (mutable) is used as a key in a Hashtable.
     * It makes sure that same Actors have always the same hash code.
     * So, the hash code of any object taht is used as key in a hash table,
     * has to be produced on an *immutable* quantity,
     * like a String (such a string is the name of the actor in our case)
     * 
     * @return an integer, which is the has code for the name of the actor
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Tests this actor against the input one and determines whether they are equal.
     * Two actors are considered equal if they have the same name and gender.
     * 
     * @return true if both objects are of type Actor, 
     * and have the same name and gender, false in any other case.
     */
    public boolean equals(Object other) {
        if (other instanceof Actor) {
            return this.name.equals(((Actor) other).name) && 
            this.gender.equals(((Actor) other).gender); // Need explicit (Actor) cast to use .name
        } else {
            return false;
        }
    }
    
    public void rankMovies(){}
    
    /**
     * Method main for testing
     *
     */
    public static void main(String[] args){
        Actor a1 = new Actor("Shah Rukh Khan", "Male");
        Actor a2 = new Actor("Fawad Khan", "Male");
        Actor a3 = new Actor("Farah Khan", "Female");
        Actor a4 = new Actor("Mahira Khan", "Female");
        Actor a5 = new Actor("Aloknath", "Male");
        Actor a6 = new Actor("Bobby Deol", "Male");
        
        System.out.println("Testing toString()");
        System.out.println(a1);
        System.out.println(a3);
        System.out.println(a6);
        
        System.out.println("Testing getName() and setName()");
        a3.setName("Aamir Khan");
        System.out.println("Setting a3 to Aamir Khan [Aamir Khan]: " + a3.getName());
        
        
        System.out.println("Testing getGender() and setGender()");
        a3.setGender("Male");
        System.out.println("Setting a3 to Male [Male]: " + a3.getGender());
    
    }
    
    

}
