import java.util.*;
import java.io.*;
import java.lang.Math;
/**
 * Represents an object of type Movie.
 * A Movie object has a title, some Actors, and results for the twelve Bechdel tests.
 *
 * @author Miraya and Natasha [and Stella]
 * @version 15/12/22
 */
public class Movie implements Comparable<Movie>
{
    private String title;
    private Hashtable<Actor, String> roles; //key = actor, value = type of role
    private Vector<String> result; //test results 0 or 1
    private final double minDiff = 0.0001; //for comparing double values

    /**
     * Constructor for objects of class Movie. 
     */
    public Movie(String title)
    {
        // initialise instance variables
        this.title = title;
        roles = new Hashtable<Actor, String>();
        result = new Vector<String>(); 
    }

    /**
     * getTitle() is a getter method for title. It returns the title of the movie
     *
     * @return    title of movie
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Method addOneActor takes in a String, formatted as lines are in the input file 
     * ("nextBechdel_castGender.txt"), generates an Actor, and adds the object to the actors of this
     * movie. Input String has the following formatting: "ACTOR","CHARACTER_NAME","TYPE","BILLING",
     * "GENDER" Example of input: "Trolls","Ricky Dillon","Aspen Heitz","Supporting","18","Male"
     * It will only add the actors that are from the movie with the same title as given. 
     *
     * @param line String representing the information of each Actor
     * @return The Actor that was just added to this movie
     */
    public Actor addOneActor(String line){
        String[] actorPieces = line.split(",");
        String movieTitle = actorPieces[0].substring(1, actorPieces[0].length()-1); //removing quotation marks
        if(movieTitle.equals(this.title)){
            String name = actorPieces[1].substring(1, actorPieces[1].length()-1);
            String gender = actorPieces[5].substring(1, actorPieces[5].length()-1);
            String type = actorPieces[3].substring(1, actorPieces[3].length()-1);
            Actor a = new Actor(name, gender);
            roles.put(a, type); //adding to hashtable
            return a;
        }
        return null;
    }

    /**
     * Method addAllActors Reads the input file ("nextBechdel_castGender.txt"), and adds all its Actors
     * to this movie. Each line in the movie has the following formatting: Input String has the following 
     * formatting: "MOVIE TITLE","ACTOR","CHARACTER_NAME","TYPE","BILLING","GENDER" Example of input: 
     * "Trolls","Ricky Dillon","Aspen Heitz","Supporting","18","Male". This method sifts through 
     * the data using addOneActor(). 
     *
     * @param actorsFile The file containing information on each actor who acted in the movie.
     */
    public void addAllActors(String actorsFile){
        try{
            Scanner reader = new Scanner(new File(actorsFile));
            String line = reader.nextLine(); //discard header
            while (reader.hasNext()){
                line = reader.nextLine();
                addOneActor(line);
            }
            reader.close();
        }
        catch(IOException ex){
            System.out.println(ex);
        }
    }

    /**
     * getAllActors() is a getter for all actors
     *
     * @return hashtable with all actor names
     */
    public Hashtable<Actor,String> getAllActors()
    {
        return roles; 
    }

    /**
     * getActors returns a Linked List with all the actor names who played in this movie. It returns an 
     * empty Linked List if there are no actors in the movie. This method uses the instance variable roles (hashtable)
     * to populate the LinkedList. Therefore roles must be initialised and filled with values before calling this method. 

     *
     * @return    linked list with all actor names
     */
    public LinkedList<String> getActors()
    {
        LinkedList<String> actors = new LinkedList<String>();
        if (roles.size() > 0){
            Enumeration<Actor> allNames = roles.keys(); //creating object to iterate through keys of hashtable 
            for (int i = 0; i < roles.size(); i++){
                String x = allNames.nextElement().getName();
                actors.add(x);
            }
        }
        return actors;
    }

    /**
     * Method getAllTestResults returns a Vector with all the Bechdel test results for this movie

     *
     * @return returns a Vector with all the Bechdel test results for this movie

     */
    public Vector<String> getAllTestResults(){
        return result;
    }

    /**
     * Method setTestResults populates the test results vector with 0s and 1s. Each represents the result 
     * of the corresponding test on the movie. This information will be read from the file "nextBechdel_allTests.txt"
     *
     * @param results string consisting of 0s and 1s. Each of these values denotes the result of the corresponding test on the movie
     */
    public void setTestResults(String results){
        String[] splitResults = results.split(","); //test results separated by commas in file
        for(int i = 0; i < splitResults.length; i++){
            result.add(splitResults[i]);
        }
    }

    /**
     * Method toString returns a string representation of this movie. This string representation provides information
     * about the title and the number of actors in the movie. 
     *
     * @return String returns a string representation of this movie
     */
    public String toString(){
        return "Movie: " + title + "\t" + "Number of actors: " + roles.size();
    }

    /**
     * Tests this movie object with the input one and determines whether they are equal.
     * 
     * @return true if both objects are movies and have the same title, 
     * false in any other case.
     */
    public boolean equals(Object other) {
        if (other instanceof Movie) {
            return this.title.equals(((Movie) other).title); // Need explicit (Movie) cast to use .title
        } else {
            return false;
        }
    }

    /**
     * feministScore() is a method that calculates the feminist score for each movie. The following
     * tests have been used to calculate the score: White Test, Feldman Test, Ko Test, Pierce Test. Tests have been weighted 
     * differently based on which aspects were more important in determining if a movie is 'feminist'
     * or not. This method calls 2 helper methods, numTestsPassed() and femaleActorCount().
     * 
     * @return double the calculated feminist score for the movie
     */
    public double feministScore(){
        double count = 0;
        String pierce = this.result.elementAt(2);
        if(pierce.equals("0")){
            count += 2;
        }
        String feldman = this.result.elementAt(4);
        if(feldman.equals("0")){
            count += 4;
        }
        String ko = this.result.elementAt(7);
        if(ko.equals("0")){
            count += 1;
        } else {
            count -= 0.5; //penalty for not passing Ko
        }
        String white = this.result.elementAt(12);
        if(white.equals("0")){
            count += 3;
        }
        count += (this.numTestsPassed() + this.femaleActorCount());
        return count;
    }

    /**
     * Method compareTo compares two Movie objects and returns an integer. If this Movie's score is greater than 
     * input Movie's score, the method returns 1, if it is less than input Movie's score, the method returns
     * -1. If the scores are equal, the method returns a positive integer, negative integer, or 0 depending on whether this movie's title 
     * comes lexographically after, before or same as the other movie title. If the feminist score for 
     * a movie cannot be calculated, then the movies are only compared based on the alphabetical order of the titles. 
     * Since this method compares the values of doubles, it uses minDiff to ensure accurate comparison. 
     *
     * @param other the Movie to be compared to this Movie
     * @return int 0, negative integer or positive integer. 
     */
    public int compareTo(Movie other){
        //if feminist score cannot be calculated
        if (this.roles.size() == 0 || other.roles.size() == 0){
            return this.title.compareTo(other.title);
        }
        double thisScore = this.feministScore();
        double otherScore = other.feministScore();
        if (Math.abs(thisScore - otherScore) <= minDiff) {
            return this.title.compareTo(other.title);
        }
        else if(thisScore - otherScore > minDiff){
            return 1;
        }
        return -1;
    }

    /**
     * numTestsPassed() is a helper method for feministScore(). This method finds the number of tests the movie 
     * has passed. It divides this by the total number of tests (13) to return a double. 
     * 
     * @return  double   fraction of number of tests passed divided by total number of tests
     */
    public double numTestsPassed(){
        double counter = 0;
        for(int i = 0; i < result.size();i++){
            if(result.elementAt(i).equals("0")){
                counter++;
            }
        }
        double fraction = counter/13;
        return fraction;
    }

    /**
     * Method femaleActorCount is a helper method for feministScore(). This method finds the number 
     * of female actors in the movie's cast and divides this by the total number of actors. 
     *
     * @return double    fraction of number of female actors divided by total number of actors
     */
    public double femaleActorCount(){
        Enumeration<Actor> allActors = roles.keys(); //creating object to iterate through keys of hashtable
        double count = 0;
        for (int i = 0; i < roles.size(); i++){
            if(allActors.nextElement().getGender().equals("Female")){
                count++;
            }
        }
        return count/roles.size();
    }

    /**
     * Method main for testing
     *
     */
    public static void main(String[] args){
        Movie m1 = new Movie("Gamma");
        System.out.println("New movie created. Printing movie. [Gamma, 0 actors]");
        System.out.println(m1);
        System.out.println("Calling getActors() on movie with no actors: " + m1.getActors());
        System.out.println();

        m1.addAllActors("data/small_castGender.txt"); 
        System.out.println("After adding 2 actors: ");
        System.out.println("Expected: Tyler Perry, Cassie Davis, 2 actors");
        System.out.println(m1);
        System.out.println(m1.getAllActors());
        System.out.println("Testing getTitle. [Gamma]: " + m1.getTitle());

        System.out.println("Printing LinkedList of actors." + m1.getActors());
        System.out.println();

        System.out.println("Adding the following values to result vector: 0,0,0,1,0,0,0,1,0,0,1,1,1");
        m1.setTestResults("0,0,0,1,0,0,0,1,0,0,1,1,1");
        System.out.println("Printing result vector: " + m1.getAllTestResults());

        System.out.println();

        System.out.println("Calling addAllActors() with invalid filename. Expecting error. ");
        Movie m2 = new Movie("Alpha");
        m2.addAllActors("x.txt");

        Movie m3 = new Movie("Alpha");

        System.out.println();
        System.out.println("Testing compareTo() on m2 and m1 [negative]: " + m2.compareTo(m1));
        System.out.println("Testing compareTo() on m2 and m3 (both same title) [0]: " + m2.compareTo(m3));
        
        System.out.println();

        System.out.println("Testing numTestsPassed() [0.6153]: " + m1.numTestsPassed());
        System.out.println("Testing femaleActorCount() [0.5]: " + m1.femaleActorCount());
    }
}
