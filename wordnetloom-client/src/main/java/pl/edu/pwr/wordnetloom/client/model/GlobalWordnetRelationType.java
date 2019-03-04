package pl.edu.pwr.wordnetloom.client.model;

import java.util.Arrays;
import java.util.List;

public enum GlobalWordnetRelationType {
    participle(RelationArgument.SENSE_RELATION),
    pertainym(RelationArgument.SENSE_RELATION),
    derivation(RelationArgument.SENSE_RELATION),
    agent(RelationArgument.SYNSET_RELATION),
    also(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    attribute(RelationArgument.SYNSET_RELATION),
    be_in_state(RelationArgument.SYNSET_RELATION),
    causes(RelationArgument.SYNSET_RELATION),
    classified_by(RelationArgument.SYNSET_RELATION),
    classifies(RelationArgument.SYNSET_RELATION),
    co_agent_instrument(RelationArgument.SYNSET_RELATION),
    co_agent_patient(RelationArgument.SYNSET_RELATION),
    co_agent_result(RelationArgument.SYNSET_RELATION),
    co_instrument_agent(RelationArgument.SYNSET_RELATION),
    co_instrument_patient(RelationArgument.SYNSET_RELATION),
    co_instrument_result(RelationArgument.SYNSET_RELATION),
    co_patient_agent(RelationArgument.SYNSET_RELATION),
    co_patient_instrument(RelationArgument.SYNSET_RELATION),
    co_result_agent(RelationArgument.SYNSET_RELATION),
    co_result_instrument(RelationArgument.SYNSET_RELATION),
    co_role(RelationArgument.SYNSET_RELATION),
    direction(RelationArgument.SYNSET_RELATION),
    domain_region(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    domain_topic(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    exemplifies(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    entails(RelationArgument.SYNSET_RELATION),
    eq_synonym(RelationArgument.SYNSET_RELATION),
    has_domain_region(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    has_domain_topic(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    is_exemplified_by(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    holo_location(RelationArgument.SYNSET_RELATION),
    holo_member(RelationArgument.SYNSET_RELATION),
    holo_part(RelationArgument.SYNSET_RELATION),
    holo_portion(RelationArgument.SYNSET_RELATION),
    holo_substance(RelationArgument.SYNSET_RELATION),
    holonym(RelationArgument.SYNSET_RELATION),
    hypernym(RelationArgument.SYNSET_RELATION),
    hyponym(RelationArgument.SYNSET_RELATION),
    in_manner(RelationArgument.SYNSET_RELATION),
    instance_hypernym(RelationArgument.SYNSET_RELATION),
    instance_hyponym(RelationArgument.SYNSET_RELATION),
    instrument(RelationArgument.SYNSET_RELATION),
    involved(RelationArgument.SYNSET_RELATION),
    involved_agent(RelationArgument.SYNSET_RELATION),
    involved_direction(RelationArgument.SYNSET_RELATION),
    involved_instrument(RelationArgument.SYNSET_RELATION),
    involved_location(RelationArgument.SYNSET_RELATION),
    involved_patient(RelationArgument.SYNSET_RELATION),
    involved_result(RelationArgument.SYNSET_RELATION),
    involved_source_direction(RelationArgument.SYNSET_RELATION),
    involved_target_direction(RelationArgument.SYNSET_RELATION),
    is_caused_by(RelationArgument.SYNSET_RELATION),
    is_entailed_by(RelationArgument.SYNSET_RELATION),
    location(RelationArgument.SYNSET_RELATION),
    manner_of(RelationArgument.SYNSET_RELATION),
    mero_location(RelationArgument.SYNSET_RELATION),
    mero_member(RelationArgument.SYNSET_RELATION),
    mero_part(RelationArgument.SYNSET_RELATION),
    mero_portion(RelationArgument.SYNSET_RELATION),
    mero_substance(RelationArgument.SYNSET_RELATION),
    meronym(RelationArgument.SYNSET_RELATION),
    similar(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    other(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION),
    patient(RelationArgument.SYNSET_RELATION),
    restricted_by(RelationArgument.SYNSET_RELATION),
    restricts(RelationArgument.SYNSET_RELATION),
    result(RelationArgument.SYNSET_RELATION),
    role(RelationArgument.SYNSET_RELATION),
    source_direction(RelationArgument.SYNSET_RELATION),
    state_of(RelationArgument.SYNSET_RELATION),
    target_direction(RelationArgument.SYNSET_RELATION),
    subevent(RelationArgument.SYNSET_RELATION),
    is_subevent_of(RelationArgument.SYNSET_RELATION),
    antonym(RelationArgument.SYNSET_RELATION, RelationArgument.SENSE_RELATION);

    public List<RelationArgument> type;

    GlobalWordnetRelationType(RelationArgument... type) {
        this.type = Arrays.asList(type);
    }

}
