package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OffersXmlDTO {
    @XmlElement(name = "offer")
    List<ImportOfferXmlDTO> offers;

    public OffersXmlDTO() {
        offers = new ArrayList<>();
    }

    public List<ImportOfferXmlDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<ImportOfferXmlDTO> offers) {
        this.offers = offers;
    }
}
