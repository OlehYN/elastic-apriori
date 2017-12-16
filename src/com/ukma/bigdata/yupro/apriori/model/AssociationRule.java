package com.ukma.bigdata.yupro.apriori.model;

import java.util.Set;

public class AssociationRule<K> {
	private Set<K> antecedent;
	private Set<K> consequent;

	public Set<K> getAntecedent() {
		return antecedent;
	}

	public void setAntecedent(Set<K> antecedent) {
		this.antecedent = antecedent;
	}

	public Set<K> getConsequent() {
		return consequent;
	}

	public void setConsequent(Set<K> consequent) {
		this.consequent = consequent;
	}

	@Override
	public String toString() {
		return "AssociationRule [antecedent=" + antecedent + ", consequent=" + consequent + "]";
	}

}
