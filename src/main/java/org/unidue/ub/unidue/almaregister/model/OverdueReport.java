package org.unidue.ub.unidue.almaregister.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "overdueReport")
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "overdueReport")
public class OverdueReport {

    public static final String JSON_PROPERTY_TITLE = "Title";
    @XmlAttribute(name = "Title")
    private String title;

    public static final String JSON_PROPERTY_FIRST_NAME = "FirstName";
    @XmlAttribute(name = "FirstName")
    private String firstName;

    public static final String JSON_PROPERTY_LAST_NAME = "LastName";
    @XmlAttribute(name = "LastName")
    private String lastName;

    public static final String JSON_PROPERTY_PRIMARY_IDENTIFIER = "PrimaryIdentifier";
    @XmlAttribute(name = "PrimaryIdentifier")
    private String primaryIdentifier;

    public static final String JSON_PROPERTY_DUE_DATE = "DueDate";
    @XmlAttribute(name = "DueDate")
    private String dueDate;

    public static final String JSON_PROPERTY_BARCODE = "Barcode";
    @XmlAttribute(name = "Barcode")
    private String barcode;

    public OverdueReport() {
    }

    public OverdueReport(String title, String firstName, String lastName, String primaryIdentifier, String dueDate, String barcode) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryIdentifier = primaryIdentifier;
        this.dueDate = dueDate;
        this.barcode = barcode;
    }

    @JsonProperty(JSON_PROPERTY_TITLE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty(JSON_PROPERTY_FIRST_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty(JSON_PROPERTY_LAST_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty(JSON_PROPERTY_PRIMARY_IDENTIFIER)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    @JsonProperty(JSON_PROPERTY_DUE_DATE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty(JSON_PROPERTY_BARCODE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
