package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String townName;

    @Column(nullable = false)
    private long population;

    @OneToMany(mappedBy = "town", targetEntity = Agent.class)
    private Set<Agent> agents;

    @OneToMany(mappedBy = "town", targetEntity = Apartment.class)
    private Set<Apartment> apartments;

    public Town() {
        agents = new HashSet<>();
        apartments = new HashSet<>();
    }

    public Town(String townName, long population) {
        this();
        this.townName = townName;
        this.population = population;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }
}
