package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ImportTownJsonDTO {
    @Expose
    @Size(min = 2)
    private String townName;

    @Expose
    @Min(value = 1)
    private long population;

    public ImportTownJsonDTO() {
    }

    public ImportTownJsonDTO(String townName, long population) {
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
}
