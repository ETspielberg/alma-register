package org.unidue.ub.unidue.almaregister.model.reports;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "result")
public class OverdueReport {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "row")
    List<Overdue> rows;

    public List<Overdue> getRows() {
        return rows;
    }

    public void setRows(List<Overdue> rows) {
        this.rows = rows;
    }
}
