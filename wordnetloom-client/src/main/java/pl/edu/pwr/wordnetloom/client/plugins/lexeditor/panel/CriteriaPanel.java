package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public abstract class CriteriaPanel extends WebPanel {

    private static final long serialVersionUID = 4649824763750406980L;
    public static final String STANDARD_VALUE_FILTER = "";
    private int SCROLL_PANE_HEIGHT = 400;
    private final int DEFAULT_WIDTH = 150;
    private final int DEFAULT_HEIGHT = 20;
    protected final Dimension DEFAULT_DIMENSION = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    private WebTextField searchTextField;
    private LexiconComboBox lexiconComboBox;
    private DomainMComboBox domainComboBox;
    private PartOfSpeechComboBox partsOfSpeechComboBox;
    private MComboBox<RelationType> synsetRelationsComboBox;
    private MComboBox<RelationType> senseRelationsComboBox;

    public CriteriaPanel(int scrollHeight) {
        SCROLL_PANE_HEIGHT = scrollHeight;
        initialize();
    }

    private void initialize() {
        setLayout(new RiverLayout());
        setMaximumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
        setMinimumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
        setPreferredSize(new Dimension(0, SCROLL_PANE_HEIGHT));

        lexiconComboBox = initLexiconComboBox();
        partsOfSpeechComboBox = initPartOfSpeechComboBox();
        searchTextField = new MTextField(STANDARD_VALUE_FILTER);
        domainComboBox = initDomainComboBox();

        synsetRelationsComboBox = createSynsetRelationsComboBox();
        synsetRelationsComboBox.setPreferredSize(DEFAULT_DIMENSION);

        senseRelationsComboBox = createSenseRelationsComboBox();
        senseRelationsComboBox.setPreferredSize(DEFAULT_DIMENSION);

    }

    private LexiconComboBox initLexiconComboBox()
    {
        LexiconComboBox resultComboBox = new LexiconComboBox(Labels.VALUE_ALL);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION);
        resultComboBox.addActionListener((ActionEvent e) -> {
            Lexicon lex = resultComboBox.getEntity();
            if (lex != null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else {
                domainComboBox.allDomains(true);
            }
            refreshSenseRelations();
        });

        return resultComboBox;
    }

    private PartOfSpeechComboBox initPartOfSpeechComboBox()
    {
        PartOfSpeechComboBox resultComboBox = new PartOfSpeechComboBox(Labels.VALUE_ALL);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION);
        resultComboBox.addItemListener((ItemEvent e) -> {
            PartOfSpeech pos = resultComboBox.getEntity();
            Lexicon lex = lexiconComboBox.getEntity();
            if (pos != null && lex != null) {
                domainComboBox.filterDomainByUbyPosAndLexcion(pos, lex, true);
            } else if (lex != null && pos == null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else if (pos != null && lex == null) {
                domainComboBox.filterDomainByUbyPos(pos, true);
            } else {
                domainComboBox.allDomains(true);
            }
        });

        return resultComboBox;
    }

    private DomainMComboBox initDomainComboBox()
    {
        DomainMComboBox resultComboBox = new DomainMComboBox(Labels.VALUE_ALL);
        resultComboBox.allDomains(true);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION);
        return resultComboBox;
    }

    protected abstract void initializeFormPanel();

    public abstract CriteriaDTO getCriteria();

    public abstract void restoreCriteria(CriteriaDTO criteria);

    protected void addSynsetRelationTypes() {
        add("br", new MLabel(Labels.RELATIONS_COLON, 'r', synsetRelationsComboBox));
        add("br hfill", synsetRelationsComboBox);
        refreshSynsetRelations();
    }

    protected void addSenseRelationTypes() {
        add("br", new MLabel(Labels.RELATIONS_COLON, 'r', senseRelationsComboBox));
        add("br hfill", senseRelationsComboBox);
        refreshSenseRelations();
    }

    protected void addDomain() {
        add("br", new MLabel(Labels.DOMAIN_COLON, 'd', domainComboBox));
        add("br hfill", domainComboBox);
    }

    protected void addPartsOfSpeach() {
        add("br", new MLabel(Labels.PARTS_OF_SPEECH_COLON, 'm', partsOfSpeechComboBox));
        add("br hfill", partsOfSpeechComboBox);
    }

    protected void addLexicon() {
        add("br", new MLabel(Labels.LEXICON_COLON, 'l', lexiconComboBox));
        add("br hfill", lexiconComboBox);
    }

    protected void addSearch() {
        add("", new MLabel(Labels.SEARCH_COLON, 'w', searchTextField));
        add("br hfill", searchTextField);
    }

    private MComboBox<RelationType> createSynsetRelationsComboBox() {
        MComboBox<RelationType> combo = new MComboBox<>();
        combo.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));
        combo.setPreferredSize(new Dimension(150, 20));
        return combo;
    }

    private MComboBox<RelationType> createSenseRelationsComboBox() {
        MComboBox<RelationType> combo = new MComboBox<>();
        combo.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));
        combo.setPreferredSize(new Dimension(150, 20));
        return combo;
    }

    public void refreshPartOfSpeech() {
        int selected = partsOfSpeechComboBox.getSelectedIndex();
        if (selected != -1) {
            partsOfSpeechComboBox.setSelectedIndex(selected);
        }
    }

    public void refreshDomain() {
        int selected = domainComboBox.getSelectedIndex();
        if (selected != -1) {
            domainComboBox.setSelectedIndex(selected);
        }
    }

    public void refreshSynsetRelations() {
    }

    public void refreshSenseRelations() {

        int selected = senseRelationsComboBox.getSelectedIndex();

        senseRelationsComboBox.removeAllItems();
        senseRelationsComboBox.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));

        if (lexiconComboBox.getEntity() != null) {
            RelationTypeManager
                    .getInstance()
                    .getRelationsWithoutProxyParent(RelationArgument.SENSE_RELATION, lexiconComboBox.getEntity())
                    .forEach(r ->
                            senseRelationsComboBox.addItem(new CustomDescription<>(
                                    RelationTypeManager
                                            .getInstance()
                                            .getFullName(r.getId()), r)));
        } else {
            RelationTypeManager
                    .getInstance()
                    .getRelationsWithoutProxyParent(RelationArgument.SENSE_RELATION)
                    .forEach(r ->
                            senseRelationsComboBox.addItem(new CustomDescription<>(
                                    RelationTypeManager
                                            .getInstance()
                                            .getFullName(r.getId()), r)));
        }

        if (selected != -1) {
            senseRelationsComboBox.setSelectedIndex(selected);
        }
    }

    public void resetFields() {
        searchTextField.setText("");
        domainComboBox.setSelectedIndex(0);
        partsOfSpeechComboBox.setSelectedIndex(0);
        synsetRelationsComboBox.setSelectedIndex(0);
        senseRelationsComboBox.setSelectedIndex(0);
        lexiconComboBox.setSelectedIndex(0);
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public MComboBox<Domain> getDomainComboBox() {
        return domainComboBox;
    }

    public MComboBox<RelationType> getSynsetRelationTypeComboBox() {
        return synsetRelationsComboBox;
    }

    public MComboBox<RelationType> getSenseRelationTypeComboBox() {
        return senseRelationsComboBox;
    }

    public LexiconComboBox getLexiconComboBox() {
        return lexiconComboBox;
    }

    public PartOfSpeechComboBox getPartsOfSpeechComboBox() {
        return partsOfSpeechComboBox;
    }

}
