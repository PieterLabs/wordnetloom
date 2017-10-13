/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
 */

package pl.edu.pwr.wordnetloom.plugins.relationtypes.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.plugins.relationtypes.RelationTypesIM;
import pl.edu.pwr.wordnetloom.systems.common.Pair;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.systems.ui.SwitchPanel;
import pl.edu.pwr.wordnetloom.systems.ui.TextAreaPlain;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.RelationTest;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.plugins.relationtypes.da.RelationTypesDA;
import pl.edu.pwr.wordnetloom.plugins.relationtypes.models.RelationTreeModel;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.systems.ui.IconFrame;
import pl.edu.pwr.wordnetloom.systems.ui.SplitPaneExt;
import pl.edu.pwr.wordnetloom.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.utils.Hints;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import se.datadosen.component.RiverLayout;

/**
 * okienko do zarządzania relacjami
 * 
 * @author Max
 */
public class RelationsEditorFrame extends IconFrame
		implements ActionListener, TreeSelectionListener, CaretListener, ListSelectionListener, MouseListener {

	private static String REVERSE_RELATION_NAME_NO_AUTO = "%s";
	private static String REVERSE_RELATION_NAME_AUTO = "%s " + Labels.AUTO;
	private static String lastText = "<x#> / <y#>";

	private static final long serialVersionUID = 1L;
	private static PartOfSpeech lastPos = PosManager.getInstance().decode("przymiotnik");

	private ButtonExt buttonNew, buttonNewSub, buttonDelete, buttonPos, buttonReverse;
	private ButtonExt buttonNewTest, buttonEditTest, buttonDeleteTest;
	private ButtonExt buttonUpTest, buttonDownTest;
	private ButtonExt buttonUpRel, buttonDownRel;
	private ButtonExt buttonSave;
	private JPanel bottomPanel, topPanel;
	private TextFieldPlain relationName;
	private TextFieldPlain relationDisplay;
	private TextFieldPlain relationShortcut;
	private TextAreaPlain relationDescription;
	private TextFieldPlain relationReverse;
	private TextFieldPlain relationPos;
	private ComboBoxPlain<RelationArgument> objectType;
	private ComboBoxPlain<Lexicon> lexicon;
	private JList testsList;
	private JTree tree;
	private JCheckBox multilingual;
	private SwitchPanel objectSwitch;

	private GenericListModel<RelationTest> testsModel = new GenericListModel<RelationTest>();
	private RelationType lastRelation = null;
	private RelationTreeModel model = new RelationTreeModel();

	private RelationsEditorFrame(Workbench workbench) {
		super(workbench.getFrame(), Labels.EDIT_RELATION_TYPES, 550, 700);

		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension separatorSize = new Dimension(10, 20);
		createTopPanel(separatorSize);
		createBottomPanel(separatorSize);
		refreshTree();
	}

	private void createBottomPanel(Dimension separatorSize) {
		Dimension normal = new Dimension(200, 25);
		Dimension smaller = new Dimension(150, 25);

		lexicon = new ComboBoxPlain<Lexicon>();
		lexicon.setPreferredSize(normal);
		lexicon.setEnabled(false);
		lexicon.addActionListener(this);

		for (Lexicon lex : RemoteUtils.lexicalUnitRemote
				.getLexiconsFromList(LexiconManager.getInstance().getLexicons())) {
			this.lexicon.addItem(lex);
		}

		relationName = new TextFieldPlain("");
		relationName.setEnabled(false);
		relationName.addCaretListener(this);
		relationName.setPreferredSize(normal);

		relationDisplay = new TextFieldPlain("");
		relationDisplay.setEnabled(false);
		relationDisplay.addCaretListener(this);
		relationDisplay.setPreferredSize(normal);

		multilingual = new JCheckBox(Labels.MULTILINGUAL);
		multilingual.setEnabled(false);
		multilingual.addActionListener(this);

		relationShortcut = new TextFieldPlain("");
		relationShortcut.setEnabled(false);
		relationShortcut.addCaretListener(this);
		relationShortcut.setPreferredSize(normal);

		relationPos = new TextFieldPlain(PosManager.getInstance().getFromID(0).toString());
		relationPos.setEnabled(false);
		relationPos.setPreferredSize(smaller);

		buttonPos = new ButtonExt("...", this);
		buttonPos.setToolTipText(Hints.CHOOSE_POS_FOR_RELATION);
		buttonPos.setEnabled(false);

		relationReverse = new TextFieldPlain(Labels.NO_VALUE);
		relationReverse.setEnabled(false);
		relationReverse.setPreferredSize(smaller);

		relationDescription = new TextAreaPlain(Labels.NO_VALUE);
		relationDescription.addCaretListener(this);
		relationDescription.setRows(2);
		relationDescription.setEnabled(false);

		buttonReverse = new ButtonExt("...", this);
		buttonReverse.setToolTipText(Hints.CHOOSE_REVERSE_RELATION);
		buttonReverse.setEnabled(false);

		objectType = new ComboBoxPlain<RelationArgument>(
				new RelationArgument[] { new RelationArgument(0L, Labels.LEXICAL_RELATIONS),
						new RelationArgument(1L, Labels.SYNSET_BETWEEN_RELATIONS),
						new RelationArgument(2L, Labels.SYNONYM_RELATIONS) },
				null);
		objectType.addActionListener(this);

		objectSwitch = new SwitchPanel(objectType, new TextFieldPlain(""), normal);
		objectSwitch.setEnabled(false);

		testsList = new JList();
		testsList.setEnabled(false);
		testsList.setModel(testsModel);
		testsList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		testsList.addListSelectionListener(this);
		testsList.addMouseListener(this);

		buttonNewTest = new ButtonExt(RelationTypesIM.getNewImage(), this);
		buttonNewTest.setToolTipText(Hints.CREATE_NEW_TEST);
		buttonNewTest.setEnabled(false);
		buttonEditTest = new ButtonExt(RelationTypesIM.getEditImage(), this);
		buttonEditTest.setToolTipText(Hints.EDIT_SELECTED_TEST);
		buttonEditTest.setEnabled(false);
		buttonDeleteTest = new ButtonExt(RelationTypesIM.getDeleteImage(), this);
		buttonDeleteTest.setEnabled(false);
		buttonDeleteTest.setToolTipText(Hints.REMOVE_SELECTED_TEST);
		buttonUpTest = new ButtonExt(RelationTypesIM.getMoveUpImage(), this);
		buttonUpTest.setEnabled(false);
		buttonUpTest.setToolTipText(Hints.MOVE_TEST_UP);
		buttonDownTest = new ButtonExt(RelationTypesIM.getMoveDownImage(), this);
		buttonDownTest.setEnabled(false);
		buttonDownTest.setToolTipText(Hints.MOVE_TEST_DOWN);
		buttonSave = new ButtonExt(Labels.SAVE, this);
		buttonSave.setEnabled(false);
		buttonSave.setToolTipText(Hints.SAVE_CHANGES);

		JLabel separator2 = new JLabel(" ");
		separator2.setPreferredSize(separatorSize);

		// panel z przyciskami
		JPanel testsButtonsPanel = new JPanel(new RiverLayout(0, 0));
		testsButtonsPanel.add("", separator2); // aby byl odstep
		testsButtonsPanel.add("tab", buttonUpTest);
		testsButtonsPanel.add("br tab", buttonDownTest);
		testsButtonsPanel.add("br tab", buttonNewTest);
		testsButtonsPanel.add("br tab", buttonEditTest);
		testsButtonsPanel.add("br tab", buttonDeleteTest);

		// panel posredni
		JPanel bottomSubPanel = new JPanel(new BorderLayout());
		bottomSubPanel.add(new JScrollPane(testsList), BorderLayout.CENTER);
		bottomSubPanel.add(testsButtonsPanel, BorderLayout.EAST);

		bottomPanel = new JPanel(new RiverLayout());
		bottomPanel.add("br", new LabelExt(Labels.LEXICON_COLON, 'l', lexicon));
		bottomPanel.add("br", lexicon);
		bottomPanel.add("tab", multilingual);
		bottomPanel.add("br", new LabelExt(Labels.RELATION_NAME_COLON, 'n', relationName));
		bottomPanel.add("tab", new LabelExt(Labels.RELATION_DOMAIN_COLON, 'r', objectSwitch));
		bottomPanel.add("br", relationName);
		bottomPanel.add("tab", objectSwitch);
		bottomPanel.add("tab", buttonSave);
		bottomPanel.add("br", new LabelExt(Labels.DISPALY_FORM_COLON, 'f', relationDisplay));
		bottomPanel.add("tab", new LabelExt(Labels.SHORTCUT_COLON, 's', relationShortcut));
		bottomPanel.add("br", relationDisplay);
		bottomPanel.add("tab", relationShortcut);
		bottomPanel.add("br", new LabelExt(Labels.DESCRIPTION_COLON, 'o', relationPos));
		bottomPanel.add("br hfill", new JScrollPane(relationDescription));
		bottomPanel.add("br", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 'c', relationPos));
		bottomPanel.add("tab", new LabelExt(Labels.REVERSE_RELATION, '\0', relationReverse));
		bottomPanel.add("br", relationPos);
		bottomPanel.add("", buttonPos);
		bottomPanel.add("tab", relationReverse);
		bottomPanel.add("", buttonReverse);
		bottomPanel.add("br left", new LabelExt(Labels.TESTS_COLON, 'e', testsList));
		bottomPanel.add("br hfill vfill", bottomSubPanel);

		// dodanie głownego splittera
		SplitPaneExt mainSplit = new SplitPaneExt(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		mainSplit.setStartDividerLocation(200);
		mainSplit.setResizeWeight(1.0f);

		this.setLayout(new BorderLayout());
		this.add(mainSplit, BorderLayout.CENTER);
	}

	private void createTopPanel(Dimension separatorSize) {
		tree = new JTree();
		tree.setScrollsOnExpand(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(true);
		tree.addTreeSelectionListener(this);
		tree.setModel(model);
		tree.setExpandsSelectedPaths(false);

		// ustawienie ikonek dla drzewa
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpenIcon(RelationTypesIM.getOpenImage());
		renderer.setClosedIcon(RelationTypesIM.getClosedImage());
		renderer.setLeafIcon(RelationTypesIM.getLeafImage());
		tree.setCellRenderer(renderer);

		Icon iconNew = IconFontSwing.buildIcon(FontAwesome.PLUS, 16);
		buttonNew = new ButtonExt(this);
		buttonNew.setIcon(iconNew);
		buttonNew.setToolTipText(Hints.CREATE_RELATION_TYPE);

		Icon iconNewSub = IconFontSwing.buildIcon(FontAwesome.PLUS_SQUARE_O, 16);
		buttonNewSub = new ButtonExt(this);
		buttonNewSub.setIcon(iconNewSub);
		buttonNewSub.setToolTipText(Hints.CREATE_NEW_RELATION_SUBTYPE);
		buttonNewSub.setEnabled(false);

		Icon iconRemove = IconFontSwing.buildIcon(FontAwesome.TRASH, 16);
		buttonDelete = new ButtonExt(this);
		buttonDelete.setEnabled(false);
		buttonDelete.setIcon(iconRemove);
		buttonDelete.setToolTipText(Hints.REMOVE_SELECTED_REL_AND_SUBRELATION);

		Icon iconUp = IconFontSwing.buildIcon(FontAwesome.ARROW_UP, 14);
		buttonUpRel = new ButtonExt(this);
		buttonUpRel.setEnabled(false);
		buttonUpRel.setIcon(iconUp);
		buttonUpRel.setToolTipText(Hints.MOVE_RELATION_UP);

		Icon iconDown = IconFontSwing.buildIcon(FontAwesome.ARROW_DOWN, 14);
		buttonDownRel = new ButtonExt(RelationTypesIM.getMoveDownImage(), this);
		buttonDownRel.setIcon(iconDown);
		buttonDownRel.setEnabled(false);
		buttonDownRel.setToolTipText(Hints.MOVE_RELATION_DOWN);

		JLabel separator1 = new JLabel(" ");
		separator1.setPreferredSize(separatorSize);

		JPanel buttonsPanel = new JPanel(new RiverLayout(0, 0));
		buttonsPanel.add("", separator1);
		buttonsPanel.add("tab", buttonUpRel);
		buttonsPanel.add("br tab", buttonDownRel);
		buttonsPanel.add("br tab", buttonNew);
		buttonsPanel.add("br tab", buttonNewSub);
		buttonsPanel.add("br tab", buttonDelete);

		JPanel topSubPanel = new JPanel(new BorderLayout());
		topSubPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
		topSubPanel.add(buttonsPanel, BorderLayout.EAST);

		topPanel = new JPanel(new RiverLayout());
		topPanel.add("", new LabelExt(Labels.RELATION_TYPES_COLON, 't', tree));
		topPanel.add("br hfill vfill", topSubPanel);
	}

	/**
	 * odswiezenie danych w drzewie
	 *
	 */
	private void refreshTree() {

		Collection<RelationType> relations = RelationTypesDA.getHighestRelations(null, null);
		for (RelationType type : relations) {
			RelationTypesDA.getChildren(type);
			RelationTypesDA.getTests(type);
		}

		TreePath selectedPath = tree.getSelectionPath();
		TreePath transformedPath = new TreePath(model.getRoot());
		if (selectedPath != null) {
			try {
				Object[] nodes = selectedPath.getPath();
				RelationType lastRel = null;
				if (nodes.length > 0) {
					// zanalezienie nowej relacji
					for (RelationType relType : relations) {
						if (relType.toString().equals(nodes[1].toString())) {
							lastRel = relType;
							break;
						}
					}
				}
				if (lastRel != null) {
					transformedPath = transformedPath.pathByAddingChild(lastRel);
					// znalezienie nowych podtypow
					if (nodes.length > 2) {
						for (RelationType subType : RemoteUtils.relationTypeRemote.dbGetChildren(lastRel,
								LexiconManager.getInstance().getLexicons())) {
							if (subType.toString().equals(nodes[2].toString())) {
								transformedPath = transformedPath.pathByAddingChild(subType);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				Logger.getLogger(RelationsEditorFrame.class).log(Level.ERROR, "Refreshing tree" + e);
			}
		}

		model.setData(relations);
		tree.updateUI();
		tree.setSelectionPath(transformedPath);
		tree.expandPath(transformedPath);
	}

	/**
	 * wyświetlenie okienka
	 * 
	 * @param workbench
	 *            - srodowisko
	 */
	public static void showModal(Workbench workbench) {
		RelationsEditorFrame frame = new RelationsEditorFrame(workbench);
		frame.setLocationRelativeTo(workbench.getFrame());
		frame.setVisible(true);
		frame.showModal();
	}

	/**
	 * zmienilo sie znazaczenie w drzewie
	 */
	public void valueChanged(TreeSelectionEvent event) {
		RelationType rel = null;
		if (event != null && event.getNewLeadSelectionPath() != null) {
			Object lastElem = event.getNewLeadSelectionPath().getLastPathComponent();
			int len = event.getNewLeadSelectionPath().getPath().length;
			Object beforeLastElem = null;
			if (len > 2)
				beforeLastElem = event.getNewLeadSelectionPath().getPath()[len - 2];

			if (lastElem != null && lastElem instanceof RelationType) {
				rel = (RelationType) lastElem;
				if (beforeLastElem == null)
					beforeLastElem = model.getRoot();

				int index = model.getIndexOfChild(beforeLastElem, lastElem);
				int count = model.getChildCount(beforeLastElem);

				buttonUpRel.setEnabled(index > 0);
				buttonDownRel.setEnabled(index + 1 < count);
			}
		}
		buttonNewSub.setEnabled(rel != null && rel.getParent() == null);
		buttonDelete.setEnabled(rel != null);
		refreshDetails(rel);
	}

	/**
	 * odswiezenie detali relacji
	 * 
	 * @param rel
	 *            - relacja do odswiezenia
	 */
	private void refreshDetails(RelationType rel) {
		rel = RemoteUtils.relationTypeRemote.getEagerRelationTypeByID(rel);
		int childrenRealtionsSize = RemoteUtils.relationTypeRemote
				.dbGetChildren(rel, LexiconManager.getInstance().getLexicons()).size();
		lastRelation = rel;
		lexicon.setSelectedItem(rel != null ? rel.getLexicon() : "");
		relationName.setText(rel != null ? rel.getName().getText() : "");
		relationDisplay.setText(rel != null ? rel.getDisplayText().getText() : "");
		relationShortcut.setText(rel != null ? rel.getShortDisplayText().getText() : "");
		relationName.setEnabled(rel != null);
		lexicon.setEnabled(rel != null);
		multilingual.setEnabled(rel != null);
		multilingual.setSelected(rel != null ? rel.isMultilingual() : false);
		relationDisplay.setEnabled(rel != null && childrenRealtionsSize == 0);
		relationShortcut.setEnabled(rel != null && childrenRealtionsSize == 0);

		// ustawienie pos
		if (rel != null && rel.getParent() != null)
			relationPos.setText(Labels.INHERITED);
		else
			relationPos.setText(PosManager.getInstance().getFromID(0).toString());

		buttonPos.setEnabled(rel != null && rel.getParent() == null);

		// ustawienie opisu
		if (rel != null && rel.getParent() != null)
			relationDescription.setText(Labels.INHERITED);
		else
			relationDescription.setText(
					rel != null && rel.getDescription() != null ? rel.getDescription().getText() : Labels.NO_VALUE);
		relationDescription.setEnabled(rel != null && rel.getParent() == null);

		// odczytanie relacji odwrotnej
		String reverseRelation = RelationTypesDA.getReverseRelationName(rel);
		if (rel != null && childrenRealtionsSize != 0)
			relationReverse.setText(Labels.IN_SUBTYPES);
		else {
			if (reverseRelation != null) {
				String format = REVERSE_RELATION_NAME_NO_AUTO;
				if (rel.isAutoReverse()) {
					format = REVERSE_RELATION_NAME_AUTO;
				}
				relationReverse.setText(String.format(format, reverseRelation));
			} else {
				relationReverse.setText(Labels.NO_VALUE);
			}
		}
		buttonReverse.setEnabled(rel != null && childrenRealtionsSize == 0);

		// typ obiektu
		objectType.setSelectedIndex(rel == null ? 0 : rel.getArgumentType().getId().intValue()+1);
		objectSwitch.setEnabled(rel != null && rel.getParent() == null);

		buttonNewTest.setEnabled(rel != null && childrenRealtionsSize == 0);
		buttonEditTest.setEnabled(false);
		buttonDeleteTest.setEnabled(false);

		buttonSave.setEnabled(false);

		refreshTests();
	}

	/**
	 * odswiezenie listy testow danej relacji
	 *
	 */
	private void refreshTests() {
		if (lastRelation != null) {
			List<RelationTest> tests = RelationTypesDA.getTests(lastRelation);

			if (tests.size() > 0) {
				Collections.sort(tests, new Comparator<RelationTest>() {
					@Override
					public int compare(final RelationTest object1, final RelationTest object2) {
						return object1.getOrder().compareTo(object2.getOrder());
					}
				});
			}

			lastRelation.setRelationTests(tests);
			Vector<RelationTest> to_set = new Vector<RelationTest>();
			Vector<RelationTest> to_model = new Vector<RelationTest>();

			for (RelationTest t : tests) {
				to_model.add(t);
			}

			for (RelationTest t : to_set) {
				RemoteUtils.testRemote.merge(t);
				to_model.add(t);
			}

			testsModel.setCollection(to_model);
			testsList.setEnabled(RemoteUtils.relationTypeRemote
					.dbGetChildren(lastRelation, LexiconManager.getInstance().getLexicons()).size() == 0);
		} else {
			testsList.setEnabled(false);
			testsModel.setCollection(null);
		}
		testsList.clearSelection();
	}

	/**
	 * zmienilo sie cos w polach edycyjnych
	 */
	public void caretUpdate(CaretEvent arg0) {
		if (lastRelation == null)
			return;
		if (arg0.getSource() instanceof TextFieldPlain) {
			TextFieldPlain field = (TextFieldPlain) arg0.getSource();
			buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
		} else if (arg0.getSource() instanceof TextAreaPlain) {
			TextAreaPlain field = (TextAreaPlain) arg0.getSource();
			buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
		}
	}

	/**
	 * wcisnieto jakis przycisk albo zmienil sie typ obiektu
	 */
	public void actionPerformed(ActionEvent event) {

		// proba zmiany statusu
		if (lastRelation != null && event.getSource() == objectType) {
			buttonSave.setEnabled(true);

		} else if (lastRelation != null && event.getSource() == lexicon) {
			buttonSave.setEnabled(true);
		} else if (lastRelation != null && event.getSource() == multilingual) {
			buttonSave.setEnabled(true);
			// zapisanie zmian
		} else if (lastRelation != null && event.getSource() == buttonSave) {
			RelationTypesDA.update(lastRelation, relationName.getText(), relationDisplay.getText(),
					relationShortcut.getText(), relationDescription.getText(), relationPos.getText(),
					(Lexicon) lexicon.getSelectedItem(), objectType.retriveComboBoxItem(), multilingual.isSelected());
			buttonSave.setEnabled(false);
			tree.repaint();

			// dodanie nowej relacji
		} else if (event.getSource() == buttonNew) {
			String result = DialogBox.inputDialog(Labels.NEW_RELATION_NAME_COLON, Labels.RELATION);
			if (result != null) {
				RelationTypesDA.newRelation(result, null, 0, false);
				refreshTree();
				RelationTypes.loadRels();
			}

			// dodanie nowej podrelacji
		} else if (lastRelation != null && event.getSource() == buttonNewSub) {
			// nie można utworzyć podtypów dla relacji specjalnych
			if (lastRelation.getArgumentType() == RelationArgument.LEXICAL_SPECIAL) {
				DialogBox.showError(Messages.ERROR_RELATION_CANT_HAVE_SUBTYPES);
				return;
			}
			lastRelation.setRelationTests(RelationTypesDA.getTests(lastRelation));
			boolean hasTests = lastRelation.getRelationTests().size() > 0;
			boolean isUsed = RelationTypesDA.getRelationUseCount(lastRelation) > 0;
			// zapytanie co zrobić jeśli relacja ma testy
			if (hasTests && DialogBox.showYesNoCancel(Messages.QUESTION_REASSIGN_TEST_TO_SUBTYPE) != DialogBox.YES) {
				return;
			}
			// teraz co zrobić jeśli relacja ma już jakieś wpisy
			if (isUsed && DialogBox.showYesNoCancel(Messages.QUESTION_REASSIGN_RELATION_TO_SUBTYPE) != DialogBox.YES) {
				return;
			}
			String result = DialogBox.inputDialog(Labels.SUBTYPE_RELATION_NAME_COLON, Labels.SUBTYPE);
			if (result != null) {

				RelationTypesDA.newRelation(result, lastRelation, 0, false);

				// czy trzeba stworzyć odpowiedni podtyp
				if (isUsed || hasTests) {
					RelationTypesDA.newRelation(Labels.CANNONICAL_FORM, lastRelation, 0, true);
				}
				refreshTree();
			}

			// ustawienie czesci mowy
		} else if (lastRelation != null && event.getSource() == buttonPos) {
			String posString = PosesFrame.showModal(this, relationPos.getText());
			relationPos.setText(posString);
			// zapisanie zmian
			RelationTypesDA.update(lastRelation, relationPos.getText());

			// ustawienie relacji odwrotnej
		} else if (lastRelation != null && event.getSource() == buttonReverse) {
			RelationType reverseRelation = RelationTypesDA.getReverseRelation(lastRelation);
			Pair<RelationType, Boolean> newReverse = ReverseRelationFrame.showModal(this, reverseRelation,
					lastRelation.isAutoReverse());

			// zapisanie zmian
			RelationTypesDA.update(lastRelation, newReverse.getA(), newReverse.getB());
			String format = REVERSE_RELATION_NAME_NO_AUTO;
			if (newReverse.getB().booleanValue()) {
				format = REVERSE_RELATION_NAME_AUTO;
			}
			String reverseRelationName = RelationTypesDA.getReverseRelationName(lastRelation);
			if (reverseRelationName != null) {
				relationReverse.setText(String.format(format, reverseRelationName));
			} else {
				relationReverse.setText(Labels.NO_VALUE);
			}

			// usuniecie relacji
		} else if (lastRelation != null && event.getSource() == buttonDelete) {
			if (DialogBox.showYesNoCancel(Messages.QUESTION_SURE_TO_REMOVE_ELEMENT) == DialogBox.YES) {
				RelationTypesDA.delete(lastRelation);
				refreshTree();
			}

			// dodanie nowego testu
		} else if (lastRelation != null && event.getSource() == buttonNewTest) {
			Pair<String, PartOfSpeech> result = TestEditorFrame.showModal(this, lastText, lastPos);
			if (result.getA() != null) {
				lastText = result.getA();
				lastPos = result.getB();
				RelationTypesDA.newTest(lastText, lastPos, lastRelation);
				refreshTests();
				buttonNewSub.setEnabled(lastRelation != null && lastRelation.getParent() == null
						&& lastRelation.getRelationTests().size() == 0);
			}

			// edycja testu
		} else if (lastRelation != null && event.getSource() == buttonEditTest) {
			int index = testsList.getSelectedIndex();
			RelationTest test = testsModel.getObjectAt(index);
			Pair<String, PartOfSpeech> result = TestEditorFrame.showModal(this, test.getText().getText(),
					test.getPos());
			if (result.getA() != null) {
				lastText = result.getA();
				lastPos = result.getB();
				RelationTypesDA.update(test, lastText, lastPos);
				testsList.repaint();
			}

			// usuniecie testu
		} else if (lastRelation != null && event.getSource() == buttonDeleteTest) {
			int index = testsList.getSelectedIndex();
			RelationTest test = testsModel.getObjectAt(index);
			if (DialogBox.showYesNoCancel(Messages.QUESTION_SURE_TO_REMOVE_TEST) == DialogBox.YES) {
				RelationTypesDA.delete(test);
				refreshTests();
				buttonNewSub.setEnabled(lastRelation != null && lastRelation.getParent() == null
						&& lastRelation.getRelationTests().size() == 0);
			}
		} else if (lastRelation != null && event.getSource() == buttonDownTest) {
			final int sel_idx = testsList.getSelectedIndex();
			final int low_idx = sel_idx + 1;
			RelationTest test_A = testsModel.getObjectAt(sel_idx);
			RelationTest test_B = testsModel.getObjectAt(low_idx);
			RelationTypesDA.update(test_A, sel_idx + 1);
			RelationTypesDA.update(test_B, low_idx - 1);
			switchTestsOrder(sel_idx, sel_idx + 1);
			testsList.setSelectedIndex(sel_idx + 1);

		} else if (lastRelation != null && event.getSource() == buttonUpTest) {
			final int sel_idx = testsList.getSelectedIndex();
			final int high_idx = sel_idx - 1;
			RelationTest test_A = testsModel.getObjectAt(sel_idx);
			RelationTest test_B = testsModel.getObjectAt(high_idx);
			RelationTypesDA.update(test_A, sel_idx - 1);
			RelationTypesDA.update(test_B, high_idx + 1);
			switchTestsOrder(sel_idx, sel_idx - 1);
			testsList.setSelectedIndex(sel_idx - 1);

		} else if (lastRelation != null && event.getSource() == buttonUpRel) {
			TreePath path = tree.getSelectionPath();
			model.moveUp(path);
			tree.setExpandsSelectedPaths(true);
			tree.setSelectionPath(path);
		} else if (lastRelation != null && event.getSource() == buttonDownRel) {
			TreePath path = tree.getSelectionPath();
			model.moveDown(path);
			tree.setExpandsSelectedPaths(true);
			tree.setSelectionPath(path);
		}
	}

	private void switchTestsOrder(int idx_a, int idx_b) {
		Collection<RelationTest> tests = testsModel.getCollection();
		Vector<RelationTest> new_tests = new Vector<RelationTest>();
		new_tests.setSize(tests.size());

		Iterator<RelationTest> itr = tests.iterator();

		if (idx_a > idx_b) {
			int aux = idx_a;
			idx_a = idx_b;
			idx_b = aux;
		}

		for (int i = 0; i != idx_a; ++i) {
			new_tests.set(i, itr.next());
		}
		RelationTest t_a = itr.next();

		for (int i = idx_a + 1; i != idx_b; ++i) {
			new_tests.set(i, itr.next());
		}

		RelationTest t_b = itr.next();

		new_tests.set(idx_a, t_b);
		new_tests.set(idx_b, t_a);

		for (int i = idx_b + 1; itr.hasNext(); ++i) {
			new_tests.set(i, itr.next());
		}

		testsModel.setCollection(new_tests);
	}

	/**
	 * zmienilo sie zaznaczenie na liscie
	 * 
	 * @param event
	 */
	public void valueChanged(ListSelectionEvent event) {
		if (testsList == null)
			return;

		if (event != null && event.getValueIsAdjusting())
			return;
		boolean empty = testsList.isSelectionEmpty();
		buttonDeleteTest.setEnabled(!empty);
		buttonEditTest.setEnabled(!empty);

		int sel_idx = testsList.getSelectedIndex();
		if (sel_idx == 0 || sel_idx == -1) {
			buttonUpTest.setEnabled(false);
		} else {
			buttonUpTest.setEnabled(true);
		}
		int tests_count = testsList.getModel().getSize();
		if (sel_idx == tests_count - 1 || sel_idx == -1) {
			buttonDownTest.setEnabled(false);
		} else {
			buttonDownTest.setEnabled(true);
		}
	}

	/**
	 * Double click on test
	 */
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2) {
			actionPerformed(new ActionEvent(buttonEditTest, 0, null));
		}
	}

	public void mousePressed(MouseEvent arg0) {
		/***/
	}

	public void mouseReleased(MouseEvent arg0) {
		/***/
	}

	public void mouseEntered(MouseEvent arg0) {
		/***/
	}

	public void mouseExited(MouseEvent arg0) {
		/***/
	}
}