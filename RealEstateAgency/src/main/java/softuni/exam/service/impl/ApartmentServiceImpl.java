package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.Messages;
import softuni.exam.models.dto.ApartmentsXmlDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;

    private final TownRepository townRepository;

    private final ModelMapper mapper;

    private final JAXBContext jaxbApartmentsContext;

    private final StringBuilder sb;

    private final ValidationUtil validationUtil;

    private static final Path APARTMENTS_XML_PATH = Path.of("src/main/resources/files/xml/apartments.xml");

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownRepository townRepository,
                                ModelMapper mapper, JAXBContext jaxbApartmentsContext, StringBuilder sb, ValidationUtil validationUtil) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;
        this.mapper = mapper;
        this.jaxbApartmentsContext = jaxbApartmentsContext;
        this.sb = sb;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(APARTMENTS_XML_PATH);
    }

    @Override
    @Transactional
    public String importApartments() throws IOException, JAXBException {
        ApartmentsXmlDTO apartmentsDTO = (ApartmentsXmlDTO) jaxbApartmentsContext.createUnmarshaller()
                .unmarshal(new StringReader(readApartmentsFromFile()));

        apartmentsDTO.getApartments().stream().filter(apartmentDTO -> {

                    if (validationUtil.isValid(apartmentDTO)) {

                        if (apartmentRepository.findByTownNameAndArea(apartmentDTO.getTown(), apartmentDTO.getArea()).isPresent()) {
                            sb.append(String.format(Messages.INVALID_DATA, "apartment")).append("\n");
                            return false;
                        }

                        sb.append(String.format(Messages.SUCCESSFUL_IMPORT_APARTMENT,
                                        apartmentDTO.getApartmentType().toString(), apartmentDTO.getArea()))
                                .append("\n");

                        return true;
                    }
                    sb.append(String.format(Messages.INVALID_DATA, "apartment")).append("\n");

                    return false;
                })
                .map(apartmentDTO -> {
                    Apartment apartment = mapper.map(apartmentDTO, Apartment.class);
                    apartment.setTown(townRepository.findByTownName(apartmentDTO.getTown()).orElse(null));

                    return apartment;
                })
                .forEach(apartmentRepository::saveAndFlush);

        return sb.toString().trim();
    }
}
