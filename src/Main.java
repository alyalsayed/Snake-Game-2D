import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Main extends Application {
     // create two arrays to specify the values of x and y postion of the snake .
     int[] snakePosX = new int[500];
     int[] snakePosY = new int[500];
     
    //Create array of objects to store the postion of the snake(x,y)
    ArrayList<Point> snake = new ArrayList();
    //create boolean variables for checking where the snake is moving.
    boolean isLeft = false;
    boolean isRight = false;
    boolean isUp = false;
    boolean isDown = false;
    boolean gameOver = false;
    
    //create the image of the snake and its body using the image class which takes url in its constructor. 
    Image snakeBodyImage = new Image(getClass().getResourceAsStream("/images/body.png"));
    Image foodImage = new Image(getClass().getResourceAsStream("/images/food.png"));
    
   // create two arrays to specify the values of x and y postion of the food
    int[] targetFoodX = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
    int[] targetFoodY = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
    // create object of timeline for animating the image of the snake and the body for a specific time
    Timeline animation = new Timeline();
    
    int snakeLength = 3;//specify the snake legnth
    int noOfMoves = 0; // specify the number of moves the snake made
    int finalScore = 0;// specify the final score 
    int foodEaten = 0;//specify the number of food eaten
    int counter = 100;// specify the initial value of the counter that will decrease until 10
    int highestScore = readHighestScore();// store the max score
    
    // generate random number for the food postion x and y to appear randomly 
    Random rand = new Random();
    int posX = rand.nextInt(22);
    int posY = rand.nextInt(22);
    
    // create a method to store or write the final highest score in the file .
    public void writeHighestScore() {
        if (finalScore >= highestScore) {
            //surround our code with try catch block to prevent exception
            try {
                //PrintWriter class accepts object of type file(new file (URL OF FILE)) in its constructor
                PrintWriter output = new PrintWriter(new File("./snake game highest score.txt"));
                output.println(highestScore + "");//write highestScore in the file
                output.close();// close the file to save the score .
            } catch (FileNotFoundException ex) {
               
            }
        }
    }
    // create a method to read the finalscore from the file at the start of the program.   
    public int readHighestScore() {
        // surround the code with try catch block to avoid sxception
        try {
             File file= new File("./snake game highest score.txt");
             Scanner input = new Scanner(file);//to accept input from a file
             String data = "";// variable to store the score 
             data+= input.nextLine();// accept input from file , concat the score and store it to data variable
             
             if (data.equals("")) 
                    data = "0";
             
           return Integer.parseInt(data);// convert string "data" to integer values. 
         } catch (FileNotFoundException ex) {
            
         }
         return 0;
    }
    public void drawShapes(GraphicsContext graphics) {
        // set the initial values of the snake postion  
        if (noOfMoves == 0) {
            //each pic has a size of 20 pixels so we arrange them as follows
            snakePosX[2] = 40;
            snakePosX[1] = 60;
            snakePosX[0] = 80;
            
            //to make the parts of the snake at the same horizental postion, so we do the following 
            snakePosY[2] = 100;
            snakePosY[1] = 100;
            snakePosY[0] = 100;
            // set the counter to 100
            counter = 100;
            animation.play();// play the game when we start the program
        }
        // store the max score in an variable (highestScore)if it is higher than the previous score 
        if (finalScore > highestScore) {
            highestScore = finalScore;
        }
        //set the size of the window and the fill color
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, 750, 500);
        //Create colored border 
        graphics.setFill(Color.color(0.2,0.4,0.5));
        for (int i = 1; i <= 500; i += 24) {
            for (int j = 1; j <= 500; j += 24) {
                graphics.fillRect(i, j, 20, 20);
            }
        }
        //create the board where the snake can move in it and set its color to black 
        graphics.setFill(Color.BLACK);
        graphics.fillRect(20, 20, 460, 460);
        
        // write "snake game" in the top right corner
        graphics.setFill(Color.color(0.2,0.4,0.2));
        graphics.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        graphics.fillText("Snake game", 530, 40);
        
        // set the postion of the decreasing counter
        graphics.setFill(Color.RED);// set counter color to red
        graphics.setFont(Font.font("Helvetica", FontWeight.NORMAL, 13));// set the font 
        graphics.fillText("+ " + counter, 505, 222);//set the postion of the counter 
        
        // adjust the highest score postion and its border
        graphics.setFill(Color.GREEN);// set the color to green
        graphics.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 18));
        graphics.fillText("Highest Score", 550, 110);
        graphics.setFill(Color.WHITE);
        graphics.fillRect(540, 120, 140, 30);//BORDER
        graphics.setFill(Color.BLACK);
        graphics.fillRect(541, 121, 138, 28);//FILL COLOR
        graphics.setFill(Color.GREEN);
        graphics.fillText(highestScore + "", 600, 142);
        
        // adjust the final score postion and its border
        graphics.setFill(Color.GREEN);
        graphics.fillText("Final Score", 550, 190);
        graphics.setFill(Color.WHITE);
        graphics.fillRect(540, 200, 140, 30);
        graphics.setFill(Color.BLACK);
        graphics.fillRect(541, 201, 138, 28);
        graphics.setFill(Color.GREEN);
        graphics.fillText(finalScore + "",600 , 222);
        
       // adjust the foodeaten  postion and its border
        graphics.fillText("Food Eaten", 550, 270);
        graphics.setFill(Color.WHITE);
        graphics.fillRect(540, 280, 140, 30);
        graphics.setFill(Color.BLACK);
        graphics.fillRect(541, 281, 138, 28);
        graphics.setFill(Color.GREEN);
        graphics.fillText(foodEaten + "", 600, 302);
        
        // write the keyboard controls  on the bottom side 
        graphics.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        graphics.fillText("Controls", 540, 350);
        graphics.setFont(Font.font("Courier", FontWeight.BOLD, 14));
        graphics.fillText("Pause Or Start : Space", 540, 370);
        graphics.fillText("Up : Arrow Up", 540, 390);
        graphics.fillText("Down : Arrow Down", 540, 410);
        graphics.fillText("Left : Arrow Left", 540, 430);
        graphics.fillText("Right : Arrow Right", 540, 450);
          
        // draw the the snake body on the board using drawimage method
        for (int i = 0; i < snakeLength; i++) {
          
             graphics.drawImage(snakeBodyImage, snakePosX[i], snakePosY[i]);
             snake.add(new Point(snakePosX[i], snakePosY[i]));//add the point(x,y) of each part(pic) in the array list 
        }
        //make the counter decrease until it reaches 10
        if (counter != 10) {
            counter--;
        }
        // if the snake bite (eat) itself then game over will be shown in the screen
        for (int i = 1; i < snakeLength; i++) {
            // if the head of the snake(snakePosX[0]) touches any of the parts of the snake snakePosX[i] then game is over
            if (snakePosX[i] == snakePosX[0] && snakePosY[i] == snakePosY[0])
            {
              
                gameOver = true;
                
                animation.pause();//pause the animation
                
                graphics.setFill(Color.RED);
                graphics.setFont(Font.font("Arial", FontWeight.BOLD, 50));
                graphics.fillText("Game Over", 110, 220);
                
                graphics.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                graphics.fillText("Press Space To Restart", 130, 260);
                
                writeHighestScore();//call the writeHighestScore to save the final score in the text file if it is greater than the HighestScore 
            }
        }
        // when the snake eats food then (foodEaten) increases and also the finalScore increses based on the counter number at this instant
        if ((targetFoodX[posX] == snakePosX[0]) && targetFoodY[posY] == snakePosY[0]) {
            finalScore += counter;
            counter = 100;//reset the counter back to 100
            foodEaten++;
            snakeLength++;//the snake length  increases 
        }
        
        // this part makes the food  disappear when the snake postion equal to the food postion (when the snake eat food)
        // then the food randomly appear again based on posX ,posX
        for (int i = 0; i < snake.size(); i++) {
            
            if (snake.get(i).getX() == targetFoodX[posX] && snake.get(i).getY() == targetFoodY[posY]) {
                posX = rand.nextInt(22);
                posY = rand.nextInt(22);
            }
        }
        snake.clear();//clears the content of the array list
        graphics.drawImage(foodImage, targetFoodX[posX], targetFoodY[posY]);// Draw the ffod image
        
        //control the movement of the snake when moves right 
        if (isRight)
        {
            for (int i = snakeLength-1 ; i >= 0; i--)
                snakePosY[i + 1] = snakePosY[i];// control the y postion of the parts of the snake 
            for (int i = snakeLength; i >= 0; i--)
            {
                if (i == 0)
                    snakePosX[i] = snakePosX[i] + 20;//the head of the snake moves 20 pixels(pic size) to the right
                else
                    snakePosX[i] = snakePosX[i - 1];// swap the parts of the snake to the right
                
                if (snakePosX[i] > 460) // if the snake x postion exceeds the board width then it will appear from the left  
                    snakePosX[i] = 20;
            }
        } 
        //control the movement of the snake when moves left 
        else if (isLeft) {
            for (int i = snakeLength -1; i >= 0; i--)
                snakePosY[i + 1] = snakePosY[i];
            for (int i = snakeLength; i >= 0; i--)
            {
                if (i == 0)
                    snakePosX[i] = snakePosX[i] - 20;//the head of the snake moves 20 pixels(pic size) to the left
                
                else
                    snakePosX[i] = snakePosX[i - 1];// swap the parts of the snake to left postion
                if (snakePosX[i] < 20) // if the snake x postion exceeds the board width then it will appear from the right  
                    snakePosX[i] = 460;
            }
        }
        //control the movement of the snake when moves up 
        else if (isUp)
        {
            for (int i = snakeLength -1; i >= 0; i--)
                snakePosX[i + 1] = snakePosX[i];
            for (int i = snakeLength; i >= 0; i--)
            {
                if (i == 0)
                    snakePosY[i] = snakePosY[i] - 20;//the head of the snake moves 20 pixels(pic size) to the up postion
                else
                    snakePosY[i] = snakePosY[i - 1];// swap the parts of the snake to up postion
                if (snakePosY[i] < 20)//if the snake y postion exceeds the board height then it will appear from down 
                    snakePosY[i] = 460;
            }
        } 
        //control the movement of the snake when moves down 
        else if (isDown)
        {
            for (int i = snakeLength -1 ; i >= 0; i--)
                snakePosX[i + 1] = snakePosX[i];
            for (int i = snakeLength; i >= 0; i--)
            {
                if (i == 0)
                    snakePosY[i] = snakePosY[i] + 20;//the head of the snake moves 20 pixels(pic size) to the down postion
                else
                    snakePosY[i] = snakePosY[i - 1];// swap the parts of the snake to down postion
                if (snakePosY[i] > 460)// if the snake y postion exceeds the board height then it will appear from the top  
                    snakePosY[i] = 20;
            }
        }
        
    }
    
    @Override
    public void start(Stage stage) {
        //create object of the  class canvas to draw shapes 
        Canvas canvas = new Canvas(750, 500);
        
        GraphicsContext graphics = canvas.getGraphicsContext2D();//return object of class GraphicsContext using the method getGraphicsContext2D in the canvas class
        //create pane to place our nodes 
        Pane root = new Pane();
        //add canvas to the pane
        root.getChildren().add(canvas);
        //add the pane to the scene
        Scene scene = new Scene(root);
        
        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();
        //we can control the speed of the snake by setting the duration 
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), e -> {
            drawShapes(graphics);//call the method drawShapes which has the complete code to be repeated 
        }));
        animation.setCycleCount(Timeline.INDEFINITE);//repeat the code infintly
        animation.play();//play the animation
        scene.setOnKeyPressed( e -> {
            if (null != e.getCode())//getcode returns the key pressed  
            {
                switch (e.getCode())
                {
                    case SPACE://if we press on space key do the following
                        
                        if (animation.getStatus() == Timeline.Status.RUNNING && gameOver == false)
                        {
                            animation.pause();// if no "game over" and the game is still running  pause the game if we press on the space  
                        }
                        else if (animation.getStatus() != Timeline.Status.RUNNING && gameOver == false)
                        {
                            animation.play();//if no "game over" and the game is  stopping  play the game if we press on the space  
                        }
                        // if game is over reset the game to the initial values
                        else if (animation.getStatus() != Timeline.Status.RUNNING && gameOver == true)
                        {    
                            gameOver = false;
                            animation.play();
                            noOfMoves = 0;
                            finalScore = 0;
                            foodEaten = 0;
                            snakeLength = 3;
                            isRight = true;
                            isLeft = false;
                            posX = rand.nextInt(22);
                            posY = rand.nextInt(22);
                        }
                        break;
                        
                    case RIGHT://if we press on right arrow key
                        
                        noOfMoves++;//increase the number of moves 
                        isRight = true;//the snake moves right 
                        if (!isLeft) {
                            isRight = true;
                        }
                        else
                        {
                            isRight = false;
                            isLeft = true;
                        }
                        isUp = false;//as the snake moves right we set the isUp and isDown to false  . 
                        isDown = false;
                        break;
                        
                    case LEFT:
                        
                        noOfMoves++;
                        isLeft = true;
                        if (!isRight)
                        {
                            isLeft = true;
                        }
                        else
                        {
                            isLeft = false;
                            isRight = true;
                        }
                        isUp = false;
                        isDown = false;
                        break;
                        
                    case UP:
                        
                        noOfMoves++;
                        isUp = true;
                        if (!isDown)
                        {
                            isUp = true;
                        }
                        else {
                            isUp = false;
                            isDown = true;
                        }
                        isLeft = false;
                        isRight = false;
                        break;
                        
                    case DOWN:
                        // if we press on the down arrow key 
                        noOfMoves++;
                        isDown = true;// as the snake moves down
                        if (!isUp)
                        {
                            isDown = true;
                        }
                        else {
                            isUp = true;
                            isDown = false;
                        }
                        isLeft = false;//as the snake moves down we set the isLeft and isRight to false
                        isRight = false;
                        break;
                }
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}