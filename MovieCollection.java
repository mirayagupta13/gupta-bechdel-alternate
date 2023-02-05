import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;
import javafoundations.*;
import java.util.NoSuchElementException;

/**
 * MovieCollection is a class that represents an object that is a collection of Movie objects. A MovieCollection 
 * has a list of Movies and a list of Actors. 
 * It contains methods that are applied to MovieCollections. 
 * 
 * @author Miraya and Natasha
 * @version 15/12/22
 */
public class MovieCollection
{
    // instance variables
    private LinkedList<Movie> allMovies;
    private LinkedList<Actor> allActors;
    private String testFile;
    private String castFile;

    /**
     * Constructor for objects of class MovieCollection. Calls readMovies() and readCasts() methods to populate the instance variables. 
     */
    public MovieCollection(String testsFileName, String castsFileName)
    {
        // initialising instance variables
        allMovies = new LinkedList<Movie>();
        allActors = new LinkedList<Actor>();
        this.testFile = testsFileName;
        this.castFile = castsFileName;
        this.readMovies();
        this.readCasts();
    }

    /**
     * Method readMovies Reads the input file, and uses its first column (movie title) to create all 
     * movie objects. Adds the included information on the Bechdel test results to each movie. It then adds the movie 
     * to the LinkedList of Movies. 
     * This program will throw an IOException if the file is not found. This program will throw a NoSuchElementException 
     * if the file has no lines. If the file has only one line, the method will not add any movies to the allMovies LinkedList. 
     *
     */
    private void readMovies(){
        try{
            Scanner reader = new Scanner(new File(this.testFile));
            String line = reader.nextLine(); //discard header
            while (reader.hasNext()){
                line = reader.nextLine();
                String[] moviePieces = line.split(","); //file contents are separated by commas
                Movie m = new Movie(moviePieces[0]); //getting title
                int length = moviePieces[0].length(); 
                m.setTestResults(line.substring(length, line.length())); //calling setTestResults using the string with test results
                allMovies.add(m);
            }
            reader.close();
        }
        catch(IOException ex1){
            System.out.println(ex1);
        }
        catch (NoSuchElementException ex2){
            System.out.println(ex2);
        }
    }

    /**
     * Method readCasts Reads the casts for each movie, from input casts file; This method assumes lines in this file are 
     * formatted as followes: "MOVIE","ACTOR","CHARACTER_NAME","TYPE","BILLING","GENDER" For example: "Trolls",
     * Ricky Dillon","Aspen Heitz","Supporting","18","Male". It creates an Actor using this information and adds it to 
     * the LinkedList allActors. If many movies have the same actors, the actor is added to the Actor LinkedList only once. 
     * If a movie does not have any test results it is ignored.
     * This program will throw an IOException if the file is not found. This program will throw a NoSuchElementException 
     * if the file has no lines. If the file has only one line, the method will not add any actors to the allActors LinkedList.
     *
     */
    private void readCasts(){
        try{
            Scanner reader = new Scanner(new File(this.castFile));
            String line = reader.nextLine(); //discard header
            while (reader.hasNext()){
                line = reader.nextLine();
                String[] castPieces = line.split(",");
                String title = castPieces[0].substring(1, castPieces[0].length()-1); //remove quotes
                for (int i = 0; i < this.allMovies.size(); i++){
                    if (allMovies.get(i).getTitle().equals(title)){ 
                        Actor a = allMovies.get(i).addOneActor(line);
                        //actors should not be repeated
                        if (!(allActors.contains(a))){
                            allActors.add(a);
                        }
                    }
                    else if(castPieces.length == 1){
                        System.out.println("Movie " + title + " does not have any test results.");
                    }
                }
            }
            reader.close();
        }
        catch(IOException ex1){
            System.out.println(ex1);
        }
        catch (NoSuchElementException ex2){
            System.out.println(ex2);
        }
    }

    /**
     * getActors() returns the LinkedList of Actors in the collection
     * 
     * @return LinkedList<Actor> all actors in the collection
     */
    public LinkedList<Actor> getActors(){
        return allActors;
    }

    /**
     * getMovies() Returns all the movies in a LinkedList
     * 
     * @return a LinkedList with all the movies, each complete with its title, actors and Bechdel test results.
     */
    public LinkedList<Movie> getMovies(){
        return allMovies;
    }

    /**
     * getActorNames() Returns the names of all actors in the collection
     * 
     * @return a LinkedList with the names of all actors
     */
    public LinkedList<String> getActorNames(){
        LinkedList<String> actorNames = new LinkedList<String>();
        for(int i = 0; i < allActors.size(); i++){
            actorNames.add(allActors.get(i).getName());
        }
        return actorNames;
    }

    /**
     * getMovieTitles() Returns the titles of all movies in the collection
     * 
     * @return a LinkedList with the titles of all the movies
     */
    public LinkedList<String> getMovieTitles(){
        LinkedList<String> movieTitles = new LinkedList<String>();
        for(int i = 0; i < allMovies.size(); i++){
            movieTitles.add(allMovies.get(i).getTitle());
        }
        return movieTitles;
    }

    /**
     *  findAllMoviesPassedTestNum() Returns a list of all Movies that pass the n-th test. If the given n 
     *  is invalid (less than 0 or greater than 12) an informative message will be printed and an empty LinkedList of 
     *  Movies will be returned. If there are no movies in the MovieCollection, an informative message will be printed
     *  and an empty LinkedList will be returned. 
     *  
     *  @param n - integer identifying the n-th test in the list of 12 Bechdel alternatives, starting from zero
     * 
     *@return A list of all Movies which have passed the n-th test
     */
    public LinkedList<Movie> findAllMoviesPassedTestNum(int n){
        LinkedList<Movie> passedMovies = new LinkedList<Movie>();
        if(n > 12 && n < 0){
            System.out.println("Invalid testing number.");
        } 
        else if (allMovies.size() == 0){
            System.out.println("There are no movies in this MovieCollection.");
        }
        else {
            LinkedList<Movie> titles = allMovies;
            for(int i = 0; i < titles.size(); i++){
                String ans = titles.get(i).getAllTestResults().elementAt(n);
                if(ans.equals("0")){
                    passedMovies.add(titles.get(i));
                }
            }
        }
        return passedMovies;
    }

    /**
     * findAllMoviesPassedPorL() Returns a list of all Movies that pass the Pierce test or the Landau test. If there are 
     * no movies in the MovieCollection, an informative message will be printed and an empty LinkedList will be returned. 
     * 
     * @return a linked list of all Movies that pass the Pierce test or the Landau test
     */
    public LinkedList<Movie> findAllMoviesPassedPorL(){
        LinkedList<Movie> passedMovies = new LinkedList<Movie>();
        if (allMovies.size() == 0){
            System.out.println("There are no movies in this MovieCollection.");
        }
        else{
            for(int i = 0; i < allMovies.size(); i++){
                String pierce = allMovies.get(i).getAllTestResults().elementAt(2);
                String landau = allMovies.get(i).getAllTestResults().elementAt(3);
                if(pierce.equals("0") || landau.equals("0")){
                    passedMovies.add(allMovies.get(i));
                }
            }
        }
        return passedMovies;
    }

    /**
     * findAllMoviesPassedBechdel() returns a linked list of Movies that pass the Bechdel test. If there are 
     * no movies in the MovieCollection, an informative message will be printed and an empty LinkedList will be 
     * returned.
     * 
     * @return a linked list of all Movies that pass the Bechdel test
     */
    public LinkedList<Movie> findAllMoviesPassedBechdel(){
        LinkedList<Movie> passedMovies = new LinkedList<Movie>();
        if (allMovies.size() == 0){
            System.out.println("There are no movies in this MovieCollection.");
        }
        else{
            for(int i = 0; i < allMovies.size(); i++){
                String ans = allMovies.get(i).getAllTestResults().elementAt(1);
                if(ans.equals("0")){
                    passedMovies.add(allMovies.get(i));
                }
            }
        }
        return passedMovies;
    }

    /**
     * findAllMoviesPassedWnotRD() returns a linked list of Movies that pass the White test but not the Rees-Davies test.
     * If there are no movies in the MovieCollection, an informative message will be printed and an empty LinkedList 
     * will be returned.
     * @return a linked list of Movies that pass the White test but not the Rees-Davies test. 
     */
    public LinkedList<Movie> findAllMoviesPassedWnotRD(){
        LinkedList<Movie> passedMovies = new LinkedList<Movie>();
        if (allMovies.size() == 0){
            System.out.println("There are no movies in this MovieCollection.");
        }
        else{
            for(int i = 0; i < allMovies.size(); i++){
                String white = allMovies.get(i).getAllTestResults().elementAt(12);
                String rees = allMovies.get(i).getAllTestResults().elementAt(13);
                if(white.equals("0") && rees.equals("1")){
                    passedMovies.add(allMovies.get(i));
                }
            }
        }
        return passedMovies;
    }

    /**
     * toString() returns a String representation of the Movie collection. It returns the number of Movies and the
     * Movies themselves. If there are no movies in the MovieCollection it returns a String informing the user. 
     * 
     * @return A String representation of the Movie collection
     */
    public String toString(){
        String s = "The movie collection has " + allMovies.size() + " movies:\n";
        if (allMovies.size() == 0){
            String z = "This movie collection has no movies.";
            return z;
        }
        for(int i = 0; i < allMovies.size(); i++){
            s+= allMovies.get(i) + "\n";
        }
        return s;
    }

    /**
     * Method rankMovies adds the movies to a PriorityQueue based on their feminist score, and they are stores from 
     * highest score to lowest score. It breaks ties between movies that have the same score by ranking the movies based on 
     * the reverse alphabetical order of their titles. If the MovieCollection has no movies, an empty PriorityQueue is 
     * returned and an informative message is printed. 
     *
     * @return PriorityQueue<Movie> of movies in order of most to least feminist
     */
    public PriorityQueue<Movie> rankMovies(){
        PriorityQueue<Movie> queue = new PriorityQueue<Movie>();
        if (allMovies.size() == 0){
            System.out.println("There are no movies in this MovieCollection");
        }
        else{
            for (int i=0; i < allMovies.size(); i++){
                queue.enqueue(allMovies.get(i));
            }
        }
        return queue;
    }

    /**
     * Method main for testing
     *
     */
    public static void main(String[] args){
        System.out.println("Creating movie collection m1");
        MovieCollection m1 = new MovieCollection("data/nextBechdel_allTests.txt", "data/nextBechdel_castGender.txt");

        System.out.println("Testing getMovies()");
        System.out.println(m1.getMovies());
        System.out.println("Testing getActors()");
        System.out.println(m1.getActors());

        System.out.println();

        //testing toString()
        System.out.println(m1);

        System.out.println("Testing getActorNames()");
        System.out.println(m1.getActorNames());

        System.out.println();

        System.out.println("Testing getMovieTitles()");
        System.out.println(m1.getMovieTitles());
        System.out.println();
        

        System.out.println("Testing findAllMoviesPassedBechdel()");
        LinkedList<Movie> bechdel = m1.findAllMoviesPassedBechdel();
        System.out.println("Number of movies that passed Bechdel: " + bechdel.size());
        System.out.println("Printing the movies that passed Bechdel: ");
        if (!(bechdel.size() == 0)){
            for (int i=0; i < bechdel.size(); i++){
                System.out.println(bechdel.get(i));
            }
        }
        else{
            System.out.println("No movies passed this test.");
        }

        System.out.println();

        System.out.println("Testing findAllMoviesPassedPorL()");
        LinkedList<Movie> pOrL = m1.findAllMoviesPassedPorL();
        System.out.println("Number of movies that passed Peirce or Landau: " + pOrL.size());
        System.out.println("Printing the movies that passed Peirce or Landau: ");
        if (!(pOrL.size() == 0)){
            for (int i=0; i < pOrL.size(); i++){
                System.out.println(pOrL.get(i));
            }
        }
        else{
            System.out.println("No movies passed this test.");
        }

        System.out.println();
        
        System.out.println("Testing findAllMoviesPassedWnotRD()");
        LinkedList<Movie> wNotRD = m1.findAllMoviesPassedWnotRD();
        System.out.println("Number of movies that passed White but not Rees-Davis: " + wNotRD.size());
        System.out.println("Printing the movies that passed but not Rees-Davis: ");
        if (!(wNotRD.size() == 0)){
            for (int i=0; i < wNotRD.size(); i++){
                System.out.println(wNotRD.get(i));
            }
        }
        else{
            System.out.println("No movies passed this test.");
        }
        System.out.println();
        
        System.out.println("Printing the movies and their feminist scores");

        for(int i=0; i < m1.allMovies.size(); i++){
            System.out.println(m1.allMovies.get(i).getTitle() + "   Score: " + m1.allMovies.get(i).feministScore());
        }

        System.out.println();
        
        System.out.println("Testing rankMovies() method on the MovieCollection");
        PriorityQueue<Movie> q1 = m1.rankMovies();
        System.out.println("Order in which movies will be dequeued: ");
        System.out.println(q1);
    }
}