package recipeCard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



/**
 * Manages the RecipeCard files
 * 
 * TODO Create recipe card update and delete method
 * 
 * @author I Zuniga J Lotruglio
 *
 */
public class RecipeCardManager {

	/**
	 * Creates a Recipe Cards folder if it does not exist then proceeds to create
	 * the recipe card to a file. Checks if file name exists if it does it removes
	 * the recipe from the list. Uses JOptionPane messages to let the user the
	 * folder was created, if the recipe file name exists, or if the recipe card was
	 * created
	 * 
	 * @param recipeToFile Recipe to be created to a file
	 * @param recipeName   Uses the recipe name to name the file
	 * @param index        Current index of List
	 */
	public void createCardFile(List<RecipeCard> recipeToFile, DefaultListModel<String> model, JTextField recipeName,
			int index) {
		// Used to create Recipe Cards Folder
		String path = "Recipe Cards";
		File RecipeCardsFolder = new File(path);

		//String fileName = "Recipe Cards/" + titleCase(recipeName.getText());
		
		String fileName = "Recipe Cards/" + "Pork Roll";
		File myFile = new File(fileName);

		if (myFile.exists()) {
			JOptionPane optionPane = new JOptionPane("Recipe name already exists", JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Error");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			recipeToFile.remove(index);
		} else {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(myFile))) {
				bw.write(recipeToFile.get(index).toString());
				bw.flush();
				JOptionPane.showMessageDialog(null, "Recipe card created!");
				model.addElement(recipeName.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads a file and displays it to a JTextArea
	 * 
	 * @param model
	 * @param viewRecipeText
	 * @param recipeToView
	 * Jim
	 */
	public void readFile(DefaultListModel<String> model, JTextArea viewRecipeText, int recipeToView) {
		try (BufferedReader br = new BufferedReader(new FileReader("Recipe Cards/" + model.get(recipeToView)))) {
			viewRecipeText.setEditable(false);
			viewRecipeText.read(br, "Recipe Cards/" + model.get(recipeToView));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Deletes a file from the directory Recipe Cards
	 * 
	 * @param model
	 * @param indexToDelete
	 */
	public void deleteRecipe(DefaultListModel<String> model, int indexToDelete) {
		if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recipe?", "Confirm",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			File r = new File("Recipe Cards/" + model.get(indexToDelete));
			model.remove(indexToDelete);
			r.delete();
		} else {
			JOptionPane.showMessageDialog(null, "Recipe not deleted.");
		}
	}

	/**
	 * Updates the recipe file
	 * 
	 * @param model
	 * @param recipeTextArea
	 * @param indexToUpdate
	 * Jim
	 */
	public void updateRecipe(DefaultListModel<String> model, JTextArea recipeTextArea, int indexToUpdate) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Recipe Cards/" + model.get(indexToUpdate)))) {
			bw.write(recipeTextArea.getText());
			bw.flush();
			JOptionPane.showMessageDialog(null, "Recipe updated!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Jim
	 * Populates a DefaultListModel with files from directory Recipe Cards
	 * 
	 * @param model
	 * 
	 */
	public void populateList(DefaultListModel<String> model) {
		File f = new File("Recipe Cards/");
		String path = "Recipe Cards";
		File RecipeCardsFolder = new File(path);
				
		if (!RecipeCardsFolder.exists()) {
			RecipeCardsFolder.mkdir();
		}else{
			
		}
			File[] listOfF = f.listFiles();
			for (File o : listOfF) {
				model.addElement(o.getName());
			}
		}
	
	private static String titleCase(String name) {
		StringBuilder titleCased = new StringBuilder();
		for(String word : name.split(" ")) {
			titleCased.append(Character.toUpperCase(word.charAt(0)));
			titleCased.append(word.substring(1));
			titleCased.append(" ");
		}
		return titleCased.toString();
	}


	}












	
