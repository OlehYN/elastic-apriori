package com.ukma.bigdata.yupro.apriori.model;

import java.util.List;

public class AprioriResult<K> {
	private List<FrequentSet<K>> frequentSets;
	private List<AssociationRule<K>> associationsRules;

	public List<FrequentSet<K>> getFrequentSets() {
		return frequentSets;
	}

	public void setFrequentSets(List<FrequentSet<K>> frequentSets) {
		this.frequentSets = frequentSets;
	}

	public List<AssociationRule<K>> getAssociationsRules() {
		return associationsRules;
	}

	public void setAssociationsRules(List<AssociationRule<K>> associationsRules) {
		this.associationsRules = associationsRules;
	}

	@Override
	public String toString() {
		return "AprioriResult [frequentSets=" + frequentSets + ", associationsRules=" + associationsRules + "]";
	}

}
