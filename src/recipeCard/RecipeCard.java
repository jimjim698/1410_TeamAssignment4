package recipeCard;

import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 * RecipeCard gets various information about a recipe and formats it into a card.
 * 
 * @author I Zuniga, J Lotruglio 
 *
 */
public class RecipeCard {

	private JTextField nameOfDish, fromKitchen, serves, prepTime, totalTime, ovenTemp;
	private JTextArea ingredients;
	private JTextArea directions;

	/**
	 * Initializes the fields
	 * 
	 * @param nameOfDish
	 * @param fromKitchen
	 * @param serves
	 * @param prepTime
	 * @param totalTime
	 * @param ovenTemp
	 * @param ingredients
	 * @param directions
	 */
	public RecipeCard(JTextField nameOfDish, JTextField fromKitchen, JTextField serves, JTextField prepTime,
			JTextField totalTime, JTextField ovenTemp, JTextArea ingredients, JTextArea directions) {
		this.nameOfDish = nameOfDish;
		this.fromKitchen = fromKitchen;
		this.serves = serves;
		this.prepTime = prepTime;
		this.totalTime = totalTime;
		this.ovenTemp = ovenTemp;
		this.ingredients = ingredients;
		this.directions = directions;
	}

	/**
	 * Gets the name of dish
	 * @return the nameOfDish
	 */
	public JTextField getNameOfDish() {
		return nameOfDish;
	}

	/**
	 * Gets the creator of the dish
	 * @return the fromKitchen
	 */
	public JTextField getFromKitchen() {
		return fromKitchen;
	}

	/**
	 * Gets how many it serves
	 * @return the serves
	 */
	public JTextField getServes() {
		return serves;
	}

	/**
	 * Gets the prep time
	 * @return the prepTime
	 */
	public JTextField getPrepTime() {
		return prepTime;
	}

	/**
	 * Gets the total time
	 * @return the totalTime
	 */
	public JTextField getTotalTime() {
		return totalTime;
	}

	/**
	 * Gets the oven temp
	 * @return the ovenTemp
	 */
	public JTextField getOvenTemp() {
		return ovenTemp;
	}

	/**
	 * Gets the ingredients
	 * @return the ingredients
	 */
	public JTextArea getIngredients() {
		return ingredients;
	}

	/**
	 * Gets the directions
	 * @return the directions
	 */
	public JTextArea getDirections() {
		return directions;
	}

	/**
	 * Creates a string of the following format:
	 * 
	 * Recipe for: {nameOfDish}
	 * 
	 * From the kitchen of: {fromKitchen}
	 * 
	 * Serves: {serves}
	 * Prep time: {prepTime}
	 * Total time: {totalTime}
	 * Oven temp: {ovenTemp}
	 * 
	 * Ingredients:
	 * {ingredients}
	 * 
	 * Directions:
	 * {directions}
	 * 
	 */
	@Override
	public String toString() {
		return String.format(
				"Recipe for: %s %n%nFrom the kitchen of: %s %n%nServes: %s %nPrep time: %s %nTotal time: %s %nOven Temp: %s %n%nIngredients:%n%s %n%nDirections:%n%s",
				nameOfDish.getText(), fromKitchen.getText(), serves.getText(), prepTime.getText(), totalTime.getText(),
				ovenTemp.getText(), ingredients.getText(), directions.getText());
	}

}
