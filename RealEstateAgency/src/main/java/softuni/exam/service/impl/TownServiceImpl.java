package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.Messages;
import softuni.exam.models.dto.ImportTownJsonDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;

    private final Gson gson;

    private final ModelMapper mapper;

    private final StringBuilder sb;

    private final ValidationUtil validationUtil;

    private static final Path TOWNS_JSON_PATH = Path.of("src/main/resources/files/json/towns.json");

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper mapper, StringBuilder sb, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.sb = sb;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(TOWNS_JSON_PATH);
    }

    @Override
    public String importTowns() throws IOException {
        Arrays.stream(gson.fromJson(readTownsFileContent(), ImportTownJsonDTO[].class))
                .filter(townDTO -> {

                    if (validationUtil.isValid(townDTO)) {

                        if (townRepository.findByTownName(townDTO.getTownName()).isPresent()) {
                            sb.append(String.format(Messages.INVALID_DATA, "town")).append("\n");
                            return false;
                        }

                        sb.append(String.format(Messages.SUCCESSFUL_IMPORT_TOWN,
                                        townDTO.getTownName(), townDTO.getPopulation()))
                                .append("\n");

                        return true;
                    }
                    sb.append(String.format(Messages.INVALID_DATA, "town")).append("\n");

                    return false;
                })
                .map(townDTO -> mapper.map(townDTO, Town.class))
                .forEach(townRepository::saveAndFlush);

        return sb.toString().trim();
    }
}
