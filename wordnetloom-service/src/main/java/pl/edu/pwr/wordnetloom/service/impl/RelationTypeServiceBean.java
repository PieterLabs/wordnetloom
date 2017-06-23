package pl.edu.pwr.wordnetloom.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.RelationTypeDAOLocal;
import pl.edu.pwr.wordnetloom.dao.impl.DAOBean;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relation.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.service.RelationTypeServiceRemote;

@Stateless
public class RelationTypeServiceBean extends DAOBean implements RelationTypeServiceRemote {

    @EJB
    private RelationTypeDAOLocal local;

    @Override
    public RelationType save(RelationType rel) {
        return local.save(rel);
    }

    @Override
    public RelationType dbGet(Long id) {
        return local.dbGet(id);
    }

    @Override
    public boolean isReverseRelation(RelationType[] relations, RelationType test) {
        return local.isReverseRelation(relations, test);
    }

    @Override
    public boolean isReverseRelation(Collection<RelationType> relations,
            RelationType test) {
        return local.isReverseRelation(relations, test);
    }

    @Override
    public void dbDelete(RelationType relation, List<Long> lexicons) {
        local.dbDelete(relation, lexicons);
    }

    @Override
    public List<RelationType> dbGetChildren(RelationType relation, List<Long> lexicons) {
        return local.dbGetChildren(relation, lexicons);
    }

    @Override
    public void dbDeleteAll() {
        local.dbDeleteAll();
    }

    @Override
    public List<RelationType> dbGetHighest(RelationArgument argument,
            PartOfSpeech pos, List<Long> lexicons) {
        return local.dbGetHighest(argument, pos, lexicons);
    }

    @Override
    public List<RelationType> dbGetLeafs(RelationArgument argument, List<Long> lexicons) {
        return local.dbGetLeafs(argument, lexicons);
    }

    @Override
    public List<RelationType> dbFullGetRelationTypes(List<Long> lexicons) {
        return local.dbFullGetRelationTypes(lexicons);
    }

    @Override
    public Long getReverseID(RelationType relationType) {
        return local.getReverseID(relationType);
    }

    @Override
    public RelationType getEagerRelationTypeByID(RelationType rt) {
        return local.getEagerRelationTypeByID(rt);
    }

    @Override
    public RelationType dbGetReverseByRelationType(RelationType relationType) {
        return local.dbGetReverseByRelationType(relationType);
    }
}
