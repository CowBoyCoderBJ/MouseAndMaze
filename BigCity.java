/**
* BigCity
*
* COMP 1020 SECTION A03
* INSTRUCTOR Ali Neshati
* ASSIGNMENT 2
* @author Bhavik Jain, 7927054
* @version March 27th, 2022
*
* PURPOSE: Your goal is to lead Suzie to the cheese in these dangerous times.
*/

// Phase 7 not complete, rest phases 1-6 works completely good.

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.Inflater;
import java.io.*;

public class BigCity {
    private char[][] gridMatrix;
    private int numBoxes;
    private int numCheese;
    private int[][] positionOfCheeze;
    private int totalMoves;
    private int cheeseCrumbs;
    private boolean looseAndRoaming;
    private char[][][] charArray;

    public BigCity(int rows,int cols, int numboxes, int numCheese, int [][] cheesePostions){

    /*
        * PURPOSE: To make first constructor.
    */

        this.numBoxes = numboxes;
        this.numCheese = Math.min(numBoxes,numCheese);
        totalMoves = 0;
        cheeseCrumbs = 0;
        looseAndRoaming = true;
        charArray = new char [5][][] ;

        gridMatrix = new char[rows][cols];
        positionOfCheeze = new int[numCheese][2];
        for (int i = 0; i < numCheese;i++){
            for(int j = 0; j < 2; j++){
                positionOfCheeze[i][j] = cheesePostions[i][j];
            }
        } 

        fillGrid();

    }

    public BigCity(String filename) throws IOException{

    /*
        * PURPOSE: To make second constructor.
    */

        totalMoves = 0;
        cheeseCrumbs = 0;
        looseAndRoaming = true;
        FileReader theFile;
        charArray = new char [5][][] ;

        try{
                theFile = new FileReader(filename);
            }
            catch (IOException e){
                throw new IOException("The file " + filename + " specified was not found");
            }
		
                BufferedReader inFile = new BufferedReader(theFile);
                Scanner input = new Scanner(inFile);
                String line;
                line = input.nextLine();
                String[] data = line.split(" ");

                int row;
                int col;

                try{
                row = Integer.parseInt(data[0]);
                col = Integer.parseInt(data[1]);
                gridMatrix = new char[row][col];
                }catch(Exception e){
                    throw new IOException("No dimensions to read");
                }
                
                if (row == 0 || col == 0 ){
                    throw new IOException("No grid to read");
                }

                int counter = 0;
                for (int i = 0; i < row; i++){
                    line = input.nextLine();
                    if ((line.startsWith("s") || line.startsWith(".") || line.startsWith("b"))){
                        counter++;
                        String [] words = line.split(" ");
                        if (!(words.length == col)){
                            throw new IOException("inaccurate number of columns in the file. Saw " + words.length + " expected " + col);
                        }
                        for (int j = 0; j < col; j++){
                            gridMatrix[i][j] = words[j].charAt(0);
                        }
                        
                    }else{
                        throw new IOException("inaccurate number of rows in the file. Saw " + counter + " expected " + row);
                    }
                }

                line = input.nextLine();
                this.numBoxes = Integer.parseInt(line);
                if ( numBoxes== 0){
                    throw new IOException("No boxes on grid");
                }
                line = input.nextLine();
                this.numCheese = Integer.parseInt(line);

                positionOfCheeze = new int[numCheese][2];
                int cheezecounter = 0;
                for (int i = 0; i < numCheese; i++){
                    if(input.hasNextLine()){
                        line = input.nextLine();
                        cheezecounter++;
                        String [] words = line.split(" ");
                        for (int j = 0; j < 2; j++){
                            positionOfCheeze[i][j] = Integer.parseInt(words[j]);
                        }
                    }else{
                        throw new IOException("Inaccurate number of rows of cheese positions in the file. Saw " + cheezecounter + " expected " + numCheese);
                    }
                }
                theFile.close();
    }
                
    private void fillGrid(){

    /*
        * PURPOSE: Fill the grid.
    */

        for (int i =0;i<gridMatrix.length;i++){
            for (int j = 0; j < gridMatrix[i].length;j++){
                gridMatrix[i][j] = '.';
            }
        }

        gridMatrix[0][0] ='s';
        for (int i = 0; i<positionOfCheeze.length;i++){
            for (int j = 0; j < gridMatrix[i].length; j++){
                gridMatrix[positionOfCheeze[i][0]][positionOfCheeze[i][1]] = 'b';
            }
            
        }

        setTraps();
    }

    private void setTraps(){

        /**
        * PURPOSE: To set *numBoxes minus numCheese* positions of traps in the grid.
        * This method is called by fillGrid().
        * This method returns nothing.
        */

        Random rand = new Random();
        int count = 0;
        int numBombs = numBoxes - numCheese;
        while (count < numBombs){
            int newRow = rand.nextInt(gridMatrix.length);
            int newCol = rand.nextInt(gridMatrix[newRow].length);
            if (newRow != 0 && newCol !=0){
                if (gridMatrix[newRow][newCol] != 'b'){
                    gridMatrix[newRow][newCol] = 'b';
                count++;
                }
            }
        }
    }

    public void move(char direction){

    /*
        * PURPOSE: To make move method.
    */

        if (!(direction == 'w' || direction == 's' || direction == 'a' || direction == 'd')){
            System.out.print("Invalid move");

        }else{
             for (int i = 0; i < gridMatrix.length;i++){
                for (int j = 0; j < gridMatrix[i].length; j++){
                    if (gridMatrix[i][j] == 's'){
                        if (direction == 'w' && i < 1){
                            throw new IndexOutOfBoundsException("This move takes Suzie out of the grid.");
                        }

                         if (direction == 's' && i > gridMatrix.length-2){
                            throw new IndexOutOfBoundsException("This move takes Suzie out of the grid.");
                        }

                        else if (direction == 'a' && j < 1){
                            throw new IndexOutOfBoundsException("This move takes Suzie out of the grid.");
                        }

                        else if (direction == 'd' && j > (gridMatrix[0].length)-2){
                            throw new IndexOutOfBoundsException("This move takes Suzie out of the grid.");
                        }

                        else{
                            processMove(direction);
                            return;
                        }
                    }
                }
             }

        }

        
    }
        
    private void processMove(char direction){

    /*
        * PURPOSE: To make processMove method.
    */

        for (int i = 0; i < gridMatrix.length;i++){

            for (int j = 0; j < gridMatrix[0].length; j++){

                if (gridMatrix[i][j] == 's'){
                    
                    if (direction == 'w'){

                        if(gridMatrix[i-1][j] == '.'){
                            gridMatrix[i-1][j] = 's';
                            gridMatrix[i][j] = '.';
                            totalMoves += 1;
                            
                        }
                        else{

                            if (isBoxCheese(i-1, j)){
                                cheeseCrumbs += 1;
                                totalMoves+=1;
                                gridMatrix[i][j] = '.';
                                gridMatrix[i-1][j] = 's';
                                
                            }else{

                                gridMatrix[i-1][j] = 's';
                                gridMatrix[i][j] = '.';
                                totalMoves+=1;
                                endTerror();
                                
                            }
                        }
                    }
                    

                    if (direction == 's'){

                        if(gridMatrix[i+1][j] == '.'){
                            gridMatrix[i+1][j] = 's';
                            gridMatrix[i][j] = '.';
                            totalMoves += 1;
                            
                        }else{

                            if (isBoxCheese(i+1, j)){
                                cheeseCrumbs += 1;
                                totalMoves+=1;
                                gridMatrix[i][j] = '.';
                                gridMatrix[i+1][j] = 's';
                                
                            }else{

                                gridMatrix[i][j] = '.';
                                gridMatrix[i+1][j] = 's';
                                totalMoves+=1;
                                endTerror();
                                
                            }
                        }
                    }

                    if (direction == 'a'){
                        if(gridMatrix[i][j-1] == '.'){
                            gridMatrix[i][j-1] = 's';
                            gridMatrix[i][j] = '.';
                            totalMoves += 1;
                            
                        }else{

                            System.out.println("In a 1st else stat");
                            if (isBoxCheese(i, j-1)){
                                System.out.println("In a 2nd else stat");
                                cheeseCrumbs += 1;
                                totalMoves+=1;
                                gridMatrix[i][j] = '.';
                                gridMatrix[i][j-1] = 's';
                                
                            }else{
                                
                                gridMatrix[i][j-1] = 's';
                                gridMatrix[i][j] = '.';
                                totalMoves+=1;
                                endTerror();
                                
                            }
                    }
                }
                    if (direction == 'd'){
                        if(gridMatrix[i][j+1] == '.'){
                            gridMatrix[i][j+1] = 's';
                            gridMatrix[i][j] = '.';
                            totalMoves += 1;
                            
                        }else{

                            if (isBoxCheese(i, j+1)){
                                cheeseCrumbs += 1;
                                totalMoves+=1;
                                gridMatrix[i][j] = '.';
                                gridMatrix[i][j+1] = 's';
                                
                            }else{

                                gridMatrix[i][j] = '.';
                                gridMatrix[i][j+1] = 's';
                                totalMoves+=1;
                                endTerror();
                                
                            }
                        }
                    }
                    return;
                }
            }
        }

    }

    private boolean isBoxCheese(int row, int col){

    /*
        * PURPOSE: To check if there is cheeze in the box.
    */

        for (int i = 0; i < positionOfCheeze.length; i++){

                if (positionOfCheeze[i][0] == row && positionOfCheeze[i][1] == col){
                    return true;
                }
            }
        
        return false;
    }
        
    private void endTerror(){
        looseAndRoaming = false;
    }

    public boolean isRoamingCity(){
        return looseAndRoaming;
    }
    
    public char [] extractRow(int rowNum) throws DataDoesNotExistException{

        try{
            return gridMatrix[rowNum];
        }
        catch (Exception e){
            throw new DataDoesNotExistException("BigCity grid doenot have a row index of " + rowNum);
        }
    }

    public char [] extractColumn(int colNum) throws DataDoesNotExistException{

        try{
            char[] colExtracted = new char[gridMatrix.length];
            for (int i = 0; i < gridMatrix.length; i ++){
                colExtracted[i] = gridMatrix[i][colNum];
            }
            return colExtracted;
        }
        catch (Exception e){
            throw new DataDoesNotExistException("BigCity grid does not have a column index of " + colNum);
        }
    }

    private void saveGrid(){
        for (int i = 0; i < gridMatrix.length; i++){
            for (int j = 0; j < gridMatrix[0].length; j++){
                charArray[0][i][j] = gridMatrix[i][j];
            }
        }

        
    }

    public void undo(){

    }

    public String toString(){

        String result = "";
        for (int i = 0; i < gridMatrix.length;i++){
            for (int j = 0; j < gridMatrix[i].length;j++){
                result += (gridMatrix[i][j]) + " ";
            }
            result += "\n";
        }
        
        if (cheeseCrumbs == numCheese){
            if (cheeseCrumbs>1){
                if (totalMoves>1){
                    if ((numBoxes - numCheese)>1){
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " moves and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumbs. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " traps.";
                    }else
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " moves and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumbs. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " trap.";
                }else
                    if ((numBoxes - numCheese)>1){
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " move and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumbs. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " traps.";
                    }else
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " move and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumbs. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " trap.";
            }else
                if (totalMoves>1){
                    if ((numBoxes - numCheese)>1){
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " moves and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumb. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " traps.";
                    }else
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " moves and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumb. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " trap.";
                }else
                    if ((numBoxes - numCheese)>1){
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " move and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumb. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " traps.";
                    }else
                        result += "Suzie outsmarted the exterminators, making " + Integer.toString(totalMoves) + " move and collecting all " + Integer.toString(cheeseCrumbs) + " cheese crumb. She sniffed out the " + Integer.toString(numBoxes-numCheese) + " trap.";
        }else
            if (cheeseCrumbs>1){
                if (totalMoves>1){
                    result += "Suzie's reign of terror came to an end abruptly after just " + Integer.toString(totalMoves) + " moves. She was captured with " + Integer.toString(cheeseCrumbs) + " cheese crumbs on her person.";
                    
                }else
                    result += "Suzie's reign of terror came to an end abruptly after just " + Integer.toString(totalMoves) + " move. She was captured with " + Integer.toString(cheeseCrumbs) + " cheese crumbs on her person.";
            }else
                if (totalMoves>1){
                    result += "Suzie's reign of terror came to an end abruptly after just " + Integer.toString(totalMoves) + " moves. She was captured with " + Integer.toString(cheeseCrumbs) + " cheese crumb on her person.";
                    
                }else
                    result += "Suzie's reign of terror came to an end abruptly after just " + Integer.toString(totalMoves) + " move. She was captured with " + Integer.toString(cheeseCrumbs) + " cheese crumb on her person.";

        
        return result;
        
    }

}
