package softuni.exam.models.entity;

import softuni.exam.models.enums.ApartmentType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ApartmentType apartmentType;

    @Column(nullable = false)
    private double area;

    @ManyToOne
    private Town town;

    @OneToMany(mappedBy = "apartment", targetEntity = Offer.class)
    private Set<Offer> offers;

    public Apartment() {
        offers = new HashSet<>();
    }

    public Apartment(ApartmentType apartmentType, double area) {
        this();
        this.apartmentType = apartmentType;
        this.area = area;
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

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
