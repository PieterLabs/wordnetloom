package pl.edu.pwr.wordnetloom.service.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.POSDaoLocal;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.service.POSServiceRemote;

@Stateless
public class POSServiceBean implements POSServiceRemote {

    @EJB
    private POSDaoLocal local;

    @Override
    public List<PartOfSpeech> getAllPartsOfSpeech(List<Long> usedLexicons) {
        return local.getAllPOSes(usedLexicons);
    }

    @Override
    public List<PartOfSpeech> getAllPartsOfSpeech() {
        return local.getAllPOSes();
    }

    @Override
    public PartOfSpeech getPOSByID(Long id) {
        return local.getPOSbyID(id);
    }
}
