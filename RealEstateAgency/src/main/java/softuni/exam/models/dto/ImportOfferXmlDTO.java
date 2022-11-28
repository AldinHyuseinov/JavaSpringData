package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "offer")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ImportOfferXmlDTO {
    @Min(value = 0)
    private double price;

    private AgentNameDTO agent;

    private ApartmentIdDTO apartment;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate publishedOn;

    public ImportOfferXmlDTO() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AgentNameDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentNameDTO agent) {
        this.agent = agent;
    }

    public ApartmentIdDTO getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentIdDTO apartment) {
        this.apartment = apartment;
    }

    public LocalDate getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }

    private static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
        private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public String marshal(LocalDate dateTime) {
            return dateTime.format(dateFormat);
        }

        @Override
        public LocalDate unmarshal(String dateTime) {
            return LocalDate.parse(dateTime, dateFormat);
        }
    }
}
