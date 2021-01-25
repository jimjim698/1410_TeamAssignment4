package recipeCard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * Creates a recipe card user interface that formats the information into a
 * recipe card.
 * 
 * @author I Zuniga J Lotruglio
 *
 */
public class RecipeCardGui implements ActionListener {
	// Components
	private JFrame frame;
	private JButton[] buttons;
	private JLabel[] labels;
	private JTextField[] fields;
	private JTextArea ingredientsText, directionsText, recipeTextArea;
	private JPanel contentPanel, recipePanel, recipeViewerAndEditorPanel;
	private JList<String> recipeJList;
	private List<RecipeCard> recipeCards = new ArrayList<>();
	private DefaultListModel<String> model;

	// Layout for contentPanel
	private CardLayout recipeLayout = new CardLayout();

	// Fonts for components
	private Font fontLarge = new Font("Copperplate Gothic Light", Font.PLAIN, 23),
			fontMedium = new Font("Copperplate Gothic Light", Font.PLAIN, 15),
			fontSmall = new Font("Copperplate Gothic Light", Font.PLAIN, 13);;
	// Alignments for components
	private int center = SwingConstants.CENTER, right = SwingConstants.RIGHT;

	// Index of recipeCards and JList
	private int index, indexOfRecipeJlist, indexToDelete;

	// Creates an object of RecipeCardManager
	private RecipeCardManager recipeManager = new RecipeCardManager();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecipeCardGui window = new RecipeCardGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RecipeCardGui() {
	
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 700, 625);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		contentPanel = new JPanel();
		contentPanel.setLayout(recipeLayout);
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

		recipePanel = recipeCardPanel();
		contentPanel.add(recipePanel, "Recipe creator");

		recipeViewerAndEditorPanel = recipeViewerAndEditor();
		contentPanel.add(recipeViewerAndEditorPanel, "Recipe viewer/editor");

		recipeLayout.show(contentPanel, "Recipe creator");

		JPanel btnPanel = btnPanel();
		frame.getContentPane().add(btnPanel, BorderLayout.SOUTH);

		JPanel recipeListPanel = recipeListPanel();
		frame.getContentPane().add(recipeListPanel, BorderLayout.EAST);
	}

	/**
	 * Action performed of buttons used
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttons[0]) {
			recipeManager.deleteRecipe(model, indexToDelete);
		}
		if (e.getSource() == buttons[1]) {
			if (!recipeTextArea.isEditable()) {
				recipeTextArea.setEditable(true);
				JOptionPane.showMessageDialog(frame, "Ready to update, when done press update to save");
			} else {
				recipeManager.updateRecipe(model, recipeTextArea, indexOfRecipeJlist);
				recipeTextArea.setEditable(false);
			}
		}
		if (e.getSource() == buttons[2]) {
			addRecipe();
		}
	}

	/**
	 * Method to create a recipe card and file of it
	 */
	private void addRecipe() {
		if (recipeViewerAndEditorPanel.isVisible()) {
			recipeLayout.show(contentPanel, "Recipe creator");
		} else if (!fields[0].getText().isBlank() && !ingredientsText.getText().isBlank()) {
			recipeCards.add(new RecipeCard(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5],
					ingredientsText, directionsText));

			// Assigns index to the current recipeCard to be created
			index = recipeCards.size() - 1;

			// Passed to recipeManger to create file
			recipeManager.createCardFile(recipeCards, model, fields[0], index);

			// Clears fields for next recipe
			for (int i = 0; i < fields.length; i++) {
				fields[i].setText(null);
			}
			ingredientsText.setText(null);
			directionsText.setText(null);
		} else {
			JOptionPane optionPane = new JOptionPane("Your recipe needs a name and ingredients",
					JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Missing recipe information");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}

	/**
	 * Creates the recipe viewer/editor panel
	 * 
	 * @return
	 */
	private JPanel recipeViewerAndEditor() {
		JPanel viewerPanel = new JPanel();
		GridBagLayout viewerPanelGBL = new GridBagLayout();
		viewerPanelGBL.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		viewerPanelGBL.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		viewerPanel.setLayout(viewerPanelGBL);

		// GridBagConstraints for viewer components
		GridBagConstraints recipeViewerGBC = new GridBagConstraints();

		JLabel titleLbl = new JLabel("Your recipe");
		titleLbl.setFont(fontMedium);
		recipeViewerGBC.insets = new Insets(20, 0, 0, 0);
		recipeViewerGBC.gridx = 0;
		recipeViewerGBC.gridy = 0;
		viewerPanel.add(titleLbl, recipeViewerGBC);

		recipeTextArea = new JTextArea();
		recipeTextArea.setFont(fontMedium);
		recipeTextArea.setEditable(false);
		recipeTextArea.setLineWrap(true);
		recipeTextArea.setWrapStyleWord(true);

		JScrollPane recipeTxtSP = new JScrollPane(recipeTextArea);
		recipeViewerGBC.insets = new Insets(8, 15, 25, 0);
		recipeViewerGBC.fill = GridBagConstraints.BOTH;
		recipeViewerGBC.gridy = 1;
		viewerPanel.add(recipeTxtSP, recipeViewerGBC);

		return viewerPanel;
	}

	/**
	 * Creates the recipe list panel
	 * 
	 * @return
	 */
	private JPanel recipeListPanel() {
		GridBagLayout recipePanelGBL = new GridBagLayout();
		recipePanelGBL.columnWidths = new int[] { 211 };
		recipePanelGBL.rowHeights = new int[] { 13, 0, 0 };
		recipePanelGBL.columnWeights = new double[] { 1.0 };
		recipePanelGBL.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };

		JPanel recipeListPanel = new JPanel();
		recipeListPanel.setLayout(recipePanelGBL);

		GridBagConstraints lblGBC = new GridBagConstraints();
		lblGBC.insets = new Insets(20, 0, 10, 0);
		lblGBC.gridx = 0;
		lblGBC.gridy = 0;

		JLabel recipeListLbl = new JLabel("Recipe list");
		recipeListLbl.setFont(fontSmall);
		recipeListLbl.setHorizontalAlignment(center);
		recipeListPanel.add(recipeListLbl, lblGBC);

		GridBagConstraints scrollPaneGBC = new GridBagConstraints();
		scrollPaneGBC.insets = new Insets(0, 0, 25, 0);
		scrollPaneGBC.fill = GridBagConstraints.VERTICAL;
		scrollPaneGBC.gridx = 0;
		scrollPaneGBC.gridy = 1;

		model = new DefaultListModel<>();
		recipeJList = new JList<>(model);
		recipeJList.setFont(fontSmall);
		recipeJList.setPrototypeCellValue("beef wellington beef");
		recipeJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		recipeJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		recipeJList.setVisibleRowCount(-1);

		// Call to populate DefaultListModel
		recipeManager.populateList(model);

		JScrollPane recipeScrollPane = new JScrollPane(recipeJList);
		recipeScrollPane.setPreferredSize(new Dimension(180, 80));
		recipeListPanel.add(recipeScrollPane, scrollPaneGBC);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					// TODO highlight and pass to delete
					indexToDelete = recipeJList.locationToIndex(e.getPoint());

				}
				if (e.getClickCount() == 2) {
					recipeLayout.show(contentPanel, "Recipe viewer/editor");
					indexOfRecipeJlist = recipeJList.locationToIndex(e.getPoint());
					recipeManager.readFile(model, recipeTextArea, indexOfRecipeJlist);
					recipeJList.clearSelection();
				}
			}
		};
		recipeJList.addMouseListener(mouseListener);

		return recipeListPanel;
	}

	/**
	 * Creates the button panel
	 * 
	 * @return
	 */
	private JPanel btnPanel() {
		GridBagLayout btnGBL = new GridBagLayout();
		btnGBL.columnWeights = new double[] { 1.0, 1.0, 1.0 };
		btnGBL.columnWidths = new int[] { 250, 240, 220 };
		GridBagConstraints btnGBC = new GridBagConstraints();
		btnGBC.fill = GridBagConstraints.BOTH;

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(btnGBL);

		// Creates 3 buttons and adds actionListener
		buttons = new JButton[3];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			buttons[i].setFont(fontSmall);
			buttons[i].setFocusPainted(false);
			buttons[i].addActionListener(this);
			btnPanel.add(buttons[i], btnGBC);
		}
		btnGBC.gridx = 0;
		btnGBC.gridwidth = 1;
		btnGBC.insets = new Insets(0, 14, 20, 0);
		btnGBL.setConstraints(buttons[0], btnGBC);
		buttons[0].setText("Delete recipe");

		btnGBC.gridx = 1;
		btnGBC.insets = new Insets(0, 15, 20, 2);
		btnGBL.setConstraints(buttons[1], btnGBC);
		buttons[1].setText("Update recipe");

		btnGBC.gridx = 2;
		btnGBC.insets = new Insets(0, 15, 20, 17);
		btnGBL.setConstraints(buttons[2], btnGBC);
		buttons[2].setText("Add recipe");

		return btnPanel;
	}

	/**
	 * Creates the main recipe card panel
	 * 
	 * @return
	 */
	private JPanel recipeCardPanel() {
		// JPanel GridLayout info
		GridBagLayout recipeGBL = new GridBagLayout();
		recipeGBL.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		recipeGBL.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		recipeGBL.rowHeights = new int[] { 50, 0, 0, 50, 50, 50, 50, 50, 40, 150 };
		recipeGBL.columnWidths = new int[] { 117, 117, 117, 119 };

		JPanel recipePanel = new JPanel();
		recipePanel.setLayout(recipeGBL);

		// GridBagConstraints for labels
		GridBagConstraints lblsGBC = new GridBagConstraints();
		lblsGBC.fill = GridBagConstraints.HORIZONTAL;

		// Creates 9 labels
		labels = new JLabel[9];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel();
			labels[i].setFont(fontSmall);
			labels[i].setHorizontalAlignment(center);
		}

		// Recipe for label
		labels[0].setFont(fontLarge);
		labels[0].setText("Recipe for...");
		lblsGBC.insets = new Insets(10, 0, 5, 5);
		lblsGBC.gridwidth = 2;
		lblsGBC.gridx = 0;
		lblsGBC.gridy = 0;
		recipePanel.add(labels[0], lblsGBC);

		// From the kitchen of label
		labels[1].setText("From the kitchen of");
		lblsGBC.insets = new Insets(10, 0, 5, 5);
		lblsGBC.gridy = 2;
		recipePanel.add(labels[1], lblsGBC);

		// Name of dish label
		labels[2].setText("Name of dish");
		lblsGBC.insets = new Insets(0, 5, 5, 0);
		lblsGBC.gridy = 1;
		lblsGBC.gridx = 2;
		recipePanel.add(labels[2], lblsGBC);

		// Ingredients label
		labels[3].setFont(fontMedium);
		labels[3].setText("Ingredients");
		lblsGBC.insets = new Insets(15, 5, 10, 0);
		lblsGBC.gridx = 2;
		lblsGBC.gridy = 2;

		recipePanel.add(labels[3], lblsGBC);

		// Serves label
		labels[4].setHorizontalAlignment(right);
		labels[4].setText("Serves");
		lblsGBC.insets = new Insets(10, 0, 5, 5);
		lblsGBC.gridwidth = 1;
		lblsGBC.gridx = 0;
		lblsGBC.gridy = 4;
		recipePanel.add(labels[4], lblsGBC);

		// Prep time label
		labels[5].setHorizontalAlignment(right);
		labels[5].setText("Prep time");
		lblsGBC.gridy = 5;
		recipePanel.add(labels[5], lblsGBC);

		// Total time label
		labels[6].setHorizontalAlignment(right);
		labels[6].setText("Total time");
		lblsGBC.gridy = 6;
		recipePanel.add(labels[6], lblsGBC);

		// Oven temp label
		labels[7].setHorizontalAlignment(right);
		labels[7].setText("Oven temp");
		lblsGBC.gridy = 7;
		recipePanel.add(labels[7], lblsGBC);

		// Directions label
		labels[8].setFont(fontMedium);
		labels[8].setText("Directions");
		lblsGBC.insets = new Insets(0, 0, 5, 0);
		lblsGBC.gridwidth = 4;
		lblsGBC.gridy = 8;
		recipePanel.add(labels[8], lblsGBC);

		// Creates 5 TextFields
		fields = new JTextField[6];
		for (int j = 0; j < fields.length; j++) {
			fields[j] = new JTextField();
			fields[j].setFont(fontSmall);
			fields[j].setHorizontalAlignment(center);
		}
		// GridBagConstraints for Fields
		GridBagConstraints fieldsGBC = new GridBagConstraints();

		// Dish name field
		fields[0].setFont(fontSmall);
		fieldsGBC.insets = new Insets(20, 20, 5, 0);
		fieldsGBC.fill = GridBagConstraints.BOTH;
		fieldsGBC.gridx = 2;
		fieldsGBC.gridy = 0;
		fieldsGBC.gridwidth = 2;
		recipePanel.add(fields[0], fieldsGBC);

		// From the kitchen of field
		fields[1].setFont(fontMedium);
		fieldsGBC.insets = new Insets(0, 15, 5, 5);
		fieldsGBC.gridx = 0;
		fieldsGBC.gridy = 3;
		recipePanel.add(fields[1], fieldsGBC);

		// Serves field
		fieldsGBC.insets = new Insets(10, 0, 5, 5);
		fieldsGBC.gridwidth = 1;
		fieldsGBC.gridx = 1;
		fieldsGBC.gridy = 4;
		recipePanel.add(fields[2], fieldsGBC);

		// Prep time field
		fieldsGBC.gridy = 5;
		recipePanel.add(fields[3], fieldsGBC);

		// Total timne field
		fieldsGBC.gridy = 6;
		recipePanel.add(fields[4], fieldsGBC);

		// Oven tmep field
		fieldsGBC.gridy = 7;
		recipePanel.add(fields[5], fieldsGBC);

		// Creates ingredients JTextArea with ScrollPane
		ingredientsText = new JTextArea();
		ingredientsText.setFont(fontMedium);
		ingredientsText.setLineWrap(true);
		ingredientsText.setWrapStyleWord(true);
		JScrollPane ingredientsScroll = new JScrollPane(ingredientsText);

		// GridBagConstraints for ingredients scroll pane
		GridBagConstraints ingredientsScrollGBC = new GridBagConstraints();
		ingredientsScrollGBC.insets = new Insets(0, 20, 5, 0);
		ingredientsScrollGBC.gridheight = 5;
		ingredientsScrollGBC.gridwidth = 2;
		ingredientsScrollGBC.fill = GridBagConstraints.BOTH;
		ingredientsScrollGBC.gridx = 2;
		ingredientsScrollGBC.gridy = 3;
		recipePanel.add(ingredientsScroll, ingredientsScrollGBC);

		// Creates directions JTextArea with ScrollPane
		directionsText = new JTextArea();
		directionsText.setFont(fontMedium);
		directionsText.setLineWrap(true);
		directionsText.setWrapStyleWord(true);
		JScrollPane directionsScroll = new JScrollPane(directionsText);

		// GridBagConstraints for directions scroll pane
		GridBagConstraints directScrollGBC = new GridBagConstraints();
		directScrollGBC.insets = new Insets(0, 15, 25, 0);
		directScrollGBC.fill = GridBagConstraints.BOTH;
		directScrollGBC.gridwidth = 4;
		directScrollGBC.gridx = 0;
		directScrollGBC.gridy = 9;

		recipePanel.add(directionsScroll, directScrollGBC);

		return recipePanel;

	}

}
