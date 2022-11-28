package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ApartmentsXmlDTO {
    @XmlElement(name = "apartment")
    private List<ImportApartmentXmlDTO> apartments;

    public ApartmentsXmlDTO() {
        apartments = new ArrayList<>();
    }

    public List<ImportApartmentXmlDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ImportApartmentXmlDTO> apartments) {
        this.apartments = apartments;
    }
}
