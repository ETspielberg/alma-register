/*
 * Ex Libris APIs
 * For more information on how to use these APIs, including how to create an API key required for authentication, see [Alma REST APIs](https://developers.exlibrisgroup.com/alma/apis).
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.unidue.ub.unidue.almaregister.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A specific user&#39;s block.
 */
@ApiModel(description = "A specific user's block.")
@JsonPropertyOrder({
  UserBlock.JSON_PROPERTY_SEGMENT_TYPE,
  UserBlock.JSON_PROPERTY_BLOCK_TYPE,
  UserBlock.JSON_PROPERTY_BLOCK_DESCRIPTION,
  UserBlock.JSON_PROPERTY_BLOCK_STATUS,
  UserBlock.JSON_PROPERTY_BLOCK_NOTE,
  UserBlock.JSON_PROPERTY_CREATED_BY,
  UserBlock.JSON_PROPERTY_CREATED_DATE,
  UserBlock.JSON_PROPERTY_EXPIRY_DATE,
  UserBlock.JSON_PROPERTY_ITEM_LOAN_ID
})

@XmlRootElement(name = "user_block")
@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "user_block")
public class UserBlock implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String JSON_PROPERTY_SEGMENT_TYPE = "segment_type";
  @XmlAttribute(name = "segment_type")
  private String segmentType;

  public static final String JSON_PROPERTY_BLOCK_TYPE = "block_type";
  @XmlElement(name = "block_type")
  private UserBlockBlockType blockType;

  public static final String JSON_PROPERTY_BLOCK_DESCRIPTION = "block_description";
  @XmlElement(name = "block_description")
  private UserBlockBlockDescription blockDescription;

  public static final String JSON_PROPERTY_BLOCK_STATUS = "block_status";
  @XmlElement(name = "block_status")
  private String blockStatus;

  public static final String JSON_PROPERTY_BLOCK_NOTE = "block_note";
  @XmlElement(name = "block_note")
  private String blockNote;

  public static final String JSON_PROPERTY_CREATED_BY = "created_by";
  @XmlElement(name = "created_by")
  private String createdBy;

  public static final String JSON_PROPERTY_CREATED_DATE = "created_date";
  @XmlElement(name = "created_date")
  private OffsetDateTime createdDate;

  public static final String JSON_PROPERTY_EXPIRY_DATE = "expiry_date";
  @XmlElement(name = "expiry_date")
  private OffsetDateTime expiryDate;

  public static final String JSON_PROPERTY_ITEM_LOAN_ID = "item_loan_id";
  @XmlElement(name = "item_loan_id")
  private String itemLoanId;


  public UserBlock segmentType(String segmentType) {

    this.segmentType = segmentType;
    return this;
  }

   /**
   * The type of the segment (Internal or External). Relevant only for User API (and not for SIS). For internal users, all the segments are considered internal. External users might have internal or external segments. Empty or illegal segment_type for external user will be considered as external.
   * @return segmentType
  **/
  @ApiModelProperty(example = "Internal", value = "The type of the segment (Internal or External). Relevant only for User API (and not for SIS). For internal users, all the segments are considered internal. External users might have internal or external segments. Empty or illegal segment_type for external user will be considered as external.")
  @JsonProperty(JSON_PROPERTY_SEGMENT_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(isAttribute = true, localName = "segment_type")

  public String getSegmentType() {
    return segmentType;
  }


  public void setSegmentType(String segmentType) {
    this.segmentType = segmentType;
  }


  public UserBlock blockType(UserBlockBlockType blockType) {

    this.blockType = blockType;
    return this;
  }

   /**
   * Get blockType
   * @return blockType
  **/
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_BLOCK_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "block_type")

  public UserBlockBlockType getBlockType() {
    return blockType;
  }


  public void setBlockType(UserBlockBlockType blockType) {
    this.blockType = blockType;
  }


  public UserBlock blockDescription(UserBlockBlockDescription blockDescription) {

    this.blockDescription = blockDescription;
    return this;
  }

   /**
   * Get blockDescription
   * @return blockDescription
  **/
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_BLOCK_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  @JacksonXmlProperty(localName = "block_description")

  public UserBlockBlockDescription getBlockDescription() {
    return blockDescription;
  }


  public void setBlockDescription(UserBlockBlockDescription blockDescription) {
    this.blockDescription = blockDescription;
  }


  public UserBlock blockStatus(String blockStatus) {
    
    this.blockStatus = blockStatus;
    return this;
  }

   /**
   * The block&#39;s status. Possible values: Active, Inactive. Default is Active.
   * @return blockStatus
  **/
  @ApiModelProperty(value = "The block's status. Possible values: Active, Inactive. Default is Active.")
  @JsonProperty(JSON_PROPERTY_BLOCK_STATUS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "block_status")

  public String getBlockStatus() {
    return blockStatus;
  }


  public void setBlockStatus(String blockStatus) {
    this.blockStatus = blockStatus;
  }


  public UserBlock blockNote(String blockNote) {
    
    this.blockNote = blockNote;
    return this;
  }

   /**
   * The block&#39;s related note.
   * @return blockNote
  **/
  @ApiModelProperty(value = "The block's related note.")
  @JsonProperty(JSON_PROPERTY_BLOCK_NOTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "block_note")

  public String getBlockNote() {
    return blockNote;
  }


  public void setBlockNote(String blockNote) {
    this.blockNote = blockNote;
  }


  public UserBlock createdBy(String createdBy) {
    
    this.createdBy = createdBy;
    return this;
  }

   /**
   * Creator of the block
   * @return createdBy
  **/
  @ApiModelProperty(value = "Creator of the block")
  @JsonProperty(JSON_PROPERTY_CREATED_BY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "created_by")

  public String getCreatedBy() {
    return createdBy;
  }


  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public UserBlock createdDate(OffsetDateTime createdDate) {
    
    this.createdDate = createdDate;
    return this;
  }

   /**
   * Creation date of the block
   * @return createdDate
  **/
  @ApiModelProperty(example = "2024-05-30T09:30:10Z", value = "Creation date of the block")
  @JsonProperty(JSON_PROPERTY_CREATED_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "created_date")

  public OffsetDateTime getCreatedDate() {
    return createdDate;
  }


  public void setCreatedDate(OffsetDateTime createdDate) {
    this.createdDate = createdDate;
  }


  public UserBlock expiryDate(OffsetDateTime expiryDate) {
    
    this.expiryDate = expiryDate;
    return this;
  }

   /**
   * Expiration date of the block
   * @return expiryDate
  **/
  @ApiModelProperty(example = "2019-12-18T14:36:48.659Z", value = "Expiration date of the block")
  @JsonProperty(JSON_PROPERTY_EXPIRY_DATE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "expiry_date")

  public OffsetDateTime getExpiryDate() {
    return expiryDate;
  }


  public void setExpiryDate(OffsetDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }


  public UserBlock itemLoanId(String itemLoanId) {
    
    this.itemLoanId = itemLoanId;
    return this;
  }

   /**
   * Internal identifier of the loan which generated this block.
   * @return itemLoanId
  **/
  @ApiModelProperty(example = "1200186133000121", value = "Internal identifier of the loan which generated this block.")
  @JsonProperty(JSON_PROPERTY_ITEM_LOAN_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  @JacksonXmlProperty(localName = "item_loan_id")

  public String getItemLoanId() {
    return itemLoanId;
  }


  public void setItemLoanId(String itemLoanId) {
    this.itemLoanId = itemLoanId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserBlock userBlock = (UserBlock) o;
    return Objects.equals(this.segmentType, userBlock.segmentType) &&
        Objects.equals(this.blockType, userBlock.blockType) &&
        Objects.equals(this.blockDescription, userBlock.blockDescription) &&
        Objects.equals(this.blockStatus, userBlock.blockStatus) &&
        Objects.equals(this.blockNote, userBlock.blockNote) &&
        Objects.equals(this.createdBy, userBlock.createdBy) &&
        Objects.equals(this.createdDate, userBlock.createdDate) &&
        Objects.equals(this.expiryDate, userBlock.expiryDate) &&
        Objects.equals(this.itemLoanId, userBlock.itemLoanId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(segmentType, blockType, blockDescription, blockStatus, blockNote, createdBy, createdDate, expiryDate, itemLoanId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserBlock2 {\n");
    sb.append("    segmentType: ").append(toIndentedString(segmentType)).append("\n");
    sb.append("    blockType: ").append(toIndentedString(blockType)).append("\n");
    sb.append("    blockDescription: ").append(toIndentedString(blockDescription)).append("\n");
    sb.append("    blockStatus: ").append(toIndentedString(blockStatus)).append("\n");
    sb.append("    blockNote: ").append(toIndentedString(blockNote)).append("\n");
    sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
    sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    itemLoanId: ").append(toIndentedString(itemLoanId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

