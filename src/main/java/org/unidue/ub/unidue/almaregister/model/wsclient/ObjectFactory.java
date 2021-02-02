//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.01.14 um 05:29:57 PM CET 
//


package org.unidue.ub.unidue.almaregister.model.wsclient;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.unidue.ub.unidue.almaregister.model.wsclient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.unidue.ub.unidue.almaregister.model.wsclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReadAddressByRegistrationnumber }
     * 
     */
    public ReadAddressByRegistrationnumber createReadAddressByRegistrationnumber() {
        return new ReadAddressByRegistrationnumber();
    }

    /**
     * Create an instance of {@link ReadAddressByRegistrationnumberResponse }
     * 
     */
    public ReadAddressByRegistrationnumberResponse createReadAddressByRegistrationnumberResponse() {
        return new ReadAddressByRegistrationnumberResponse();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link ServiceFault }
     * 
     */
    public ServiceFault createServiceFault() {
        return new ServiceFault();
    }

    /**
     * Create an instance of {@link ValidationMessage }
     * 
     */
    public ValidationMessage createValidationMessage() {
        return new ValidationMessage();
    }

}
