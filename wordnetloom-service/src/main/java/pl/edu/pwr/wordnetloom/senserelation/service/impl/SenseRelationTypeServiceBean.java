package pl.edu.pwr.wordnetloom.senserelation.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.senserelation.model.SenseRelationType;
import pl.edu.pwr.wordnetloom.senserelation.repository.SenseRelationTypeRepository;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationTypeServiceLocal;

@Stateless
public class SenseRelationTypeServiceBean implements SenseRelationTypeServiceLocal {

    @Inject
    private SenseRelationTypeRepository relationTypeRepository;

    @Override
    public SenseRelationType findById(Long id) {
        return relationTypeRepository.findById(id);
    }

    @Override
    public boolean isReverseRelation(SenseRelationType[] relations, SenseRelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public boolean isReverseRelation(Collection<SenseRelationType> relations, SenseRelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public void deleteAll() {
        relationTypeRepository.deleteAll();
    }

    @Override
    public Long findReverseId(SenseRelationType relationType) {
        return relationTypeRepository.findReverseId(relationType);
    }

    @Override
    public SenseRelationType getEagerRelationTypeByID(SenseRelationType rt) {
        return relationTypeRepository.findFullByRelationType(rt);
    }

    @Override
    public SenseRelationType findReverseByRelationType(SenseRelationType relationType) {
        return relationTypeRepository.findReverseByRelationType(relationType);
    }

    @Override
    public SenseRelationType save(SenseRelationType rel) {
        return relationTypeRepository.save(rel);
    }

    @Override
    public List<SenseRelationType> findtHighest(List<Long> lexicons) {
        return relationTypeRepository.findHighestLeafs(lexicons);
    }

    @Override
    public List<SenseRelationType> findLeafs(List<Long> lexicons) {
        return relationTypeRepository.findLeafs(lexicons);
    }

    @Override
    public List<SenseRelationType> dbFullGetRelationTypes(List<Long> lexicons) {
        return relationTypeRepository.findFullRelationTypes(lexicons);
    }

    @Override
    public List<SenseRelationType> findChildren(SenseRelationType relation, List<Long> lexicons) {
        return relationTypeRepository.findChildren(relation);
    }

    @Override
    public void delete(SenseRelationType relation, List<Long> lexicons) {
        relationTypeRepository.delete(relation, lexicons);
    }

}
