package uk.ac.ebi.biosamples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Sample {

    private String accession;
    private Map<String, String> characteristics;
    @JsonIgnore
    private List<Sample> derivedTo = new ArrayList<Sample>();
    @JsonIgnore
    private Sample derivedFrom;

    public Sample() {}


    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setCharacteristics(Map<String, String> characteristics) {
        this.characteristics = characteristics;
    }

    public void setDerivedTo(List<Sample> derivedTo) {
        this.derivedTo = derivedTo;
    }

    public Sample getDerivedFrom() {
        return derivedFrom;
    }

    public List<Sample> getDerivedTo() {
        return derivedTo;
    }

    public String getAccession() {
        return accession;
    }

    public Map<String, String> getCharacteristics() {
        return characteristics;
    }

    public void setDerivedFrom(Sample derivedFrom) {
        this.derivedFrom = derivedFrom;
    }
}
