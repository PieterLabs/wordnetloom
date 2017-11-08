package pl.edu.pwr.wordnetloom.service;

import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.dto.SenseDataEntry;
import pl.edu.pwr.wordnetloom.model.*;
import pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.Particle;

import javax.ejb.Remote;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Remote
public interface LexicalUnitServiceRemote extends DAORemote {

    Sense dbClone(Sense unit);

    Sense dbGet(Long id);

    Sense dbGetWithYiddish(Long id);

    int dbGetSynsetsCount(Sense unit);

    void dbDeleteAll();

    int dbGetUnitsCount(String filter);

    int dbGetNextVariant(String lemma, pl.edu.pwr.wordnetloom.model.PartOfSpeech pos);

    int dbDelete(List<Sense> list, String owner);

    int dbUsedUnitsCount();

    int dbGetUnitsCount();

    List<Sense> dbGetUnitsWithoutForms();

    List<Sense> dbGetUnitsNotInAnySynset(String filter, pl.edu.pwr.wordnetloom.model.PartOfSpeech pos);

    List<Sense> dbGetUnitsAppearingInMoreThanOneSynset();

    boolean dbInAnySynset(Sense unit);

    boolean dbDelete(Sense unit, String owner);

    String getSensAtrribute(Sense sense, String nazwaPola);

    void setSenseAtrribute(Sense sense, String key, String value);

    Sense updateSense(Sense sense);

    Set<Long> dbUsedUnitsIDs();

    Word seekOrSaveWord(Word word);

    Word saveWord(Word word);

    List<Lexicon> getAllLexicons();

    List<Lexicon> getLexiconsFromList(List<Long> lexicons);

    int dbGetHighestVariant(String word, List<Long> lexicons);

    List<Sense> dbFullGetUnits(String filter, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(String lemma, List<Long> lexicon);

    List<Sense> dbFullGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, int limit, List<Long> lexicons);

    List<Sense> dbFastGetUnits(Synset synset, List<Long> lexicons);

    List<Synset> dbFastGetSynsets(Sense unit, List<Long> lexicons);

    int dbGetUnitsCount(Synset synset, List<Long> lexicons);

    List<Sense> dbFindUnits(CriteriaDTO dto, PartOfSpeech pos, int limitSize, List<Long> lexicons);

    List<Long> getAllLemasForLexicon(List<Long> lexicon);

    Lexicon dbSaveLexicon(Lexicon lexicon);

    List<Sense> getSensesByLemmaID(long id, long lexicon);

    void dbRemoveYddishExtensionById(Long id);

    YiddishSenseExtension save(YiddishSenseExtension ext);

    Particle saveParticle(Particle p);

    void removeParticle(Particle p);

    void dbRemoveYiddishSenseExtension(YiddishSenseExtension sense);

    int dbGetUnitCountByDomain(String domain);

    List<CountModel> dbGetEtymologicalRootsCount();

    HashMap<Long, SenseDataEntry> prepareCacheForRootNode(Sense sense, List<Long> lexicons);
}
