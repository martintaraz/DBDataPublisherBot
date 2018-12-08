/*
 * Stationsdatenbereitstellung
 * An API providing master data for german railway stations by DB Station&Service AG.
 *
 * OpenAPI spec version: 2.2.01
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.github.martintaraz.hackathon2018.models.stations;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * reference object. an internal organization type of Station&amp;Service, regional department.
 */
@ApiModel(description = "reference object. an internal organization type of Station&Service, regional department.")
@javax.annotation.processing.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-07T20:50:44.823+01:00")
public class RegionalBereichRef {
  @SerializedName("name")
  private String name = null;

  @SerializedName("shortName")
  private String shortName = null;

  @SerializedName("number")
  private Integer number = null;

  public RegionalBereichRef name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the regional department
   * @return name
  **/
  @ApiModelProperty(value = "name of the regional department")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RegionalBereichRef shortName(String shortName) {
    this.shortName = shortName;
    return this;
  }

   /**
   * Get shortName
   * @return shortName
  **/
  @ApiModelProperty(value = "")
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public RegionalBereichRef number(Integer number) {
    this.number = number;
    return this;
  }

   /**
   * unique identifier of the regional department
   * @return number
  **/
  @ApiModelProperty(value = "unique identifier of the regional department")
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionalBereichRef regionalBereichRef = (RegionalBereichRef) o;
    return Objects.equals(this.name, regionalBereichRef.name) &&
        Objects.equals(this.shortName, regionalBereichRef.shortName) &&
        Objects.equals(this.number, regionalBereichRef.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, shortName, number);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionalBereichRef {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
