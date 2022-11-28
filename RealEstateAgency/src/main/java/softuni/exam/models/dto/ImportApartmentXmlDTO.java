package softuni.exam.models.dto;

import softuni.exam.models.enums.ApartmentType;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apartment")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ImportApartmentXmlDTO {
    private ApartmentType apartmentType;

    @Min(value = 40)
    private double area;

    private String town;

    public ImportApartmentXmlDTO() {
    }

    public ImportApartmentXmlDTO(ApartmentType apartmentType, double area, String town) {
        this.apartmentType = apartmentType;
        this.area = area;
        this.town = town;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
