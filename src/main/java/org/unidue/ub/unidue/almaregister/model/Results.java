package org.unidue.ub.unidue.almaregister.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "results")
public class Results {

    public static final String JSON_PROPERTY_OVERDUE_REPORT = "overdueReport";
    @XmlAttribute(name = "overdueReport")
    List<OverdueReport> overdueReport;

    @JsonProperty(JSON_PROPERTY_OVERDUE_REPORT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<OverdueReport> getOverdueReport() {
        return overdueReport;
    }

    public void setOverdueReport(List<OverdueReport> overdueReport) {
        this.overdueReport = overdueReport;
    }
}
