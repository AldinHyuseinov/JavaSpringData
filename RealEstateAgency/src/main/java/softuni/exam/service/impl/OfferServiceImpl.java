package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.Messages;
import softuni.exam.models.dto.ExportBestOfferDTO;
import softuni.exam.models.dto.OffersXmlDTO;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;

    private final AgentRepository agentRepository;

    private final ApartmentRepository apartmentRepository;

    private final JAXBContext jaxbOffersContext;

    private final ModelMapper mapper;

    private final StringBuilder sb;

    private final ValidationUtil validationUtil;

    private static final Path OFFERS_XML_PATH = Path.of("src/main/resources/files/xml/offers.xml");

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentRepository agentRepository,
                            ApartmentRepository apartmentRepository, JAXBContext jaxbOffersContext,
                            ModelMapper mapper, StringBuilder sb, ValidationUtil validationUtil) {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
        this.jaxbOffersContext = jaxbOffersContext;
        this.mapper = mapper;
        this.sb = sb;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(OFFERS_XML_PATH);
    }

    @Override
    @Transactional
    public String importOffers() throws IOException, JAXBException {
        OffersXmlDTO offersDTO = (OffersXmlDTO) jaxbOffersContext.createUnmarshaller()
                .unmarshal(new StringReader(readOffersFileContent()));

        offersDTO.getOffers().stream().filter(offerDTO -> {

                    if (validationUtil.isValid(offerDTO)) {

                        if (agentRepository.findByFirstName(offerDTO.getAgent().getName()).isEmpty()) {
                            sb.append(String.format(Messages.INVALID_DATA, "offer")).append("\n");
                            return false;
                        }

                        sb.append(String.format(Messages.SUCCESSFUL_IMPORT_OFFER,
                                offerDTO.getPrice())).append("\n");

                        return true;
                    }
                    sb.append(String.format(Messages.INVALID_DATA, "offer")).append("\n");

                    return false;
                })
                .map(offerDTO -> {
                    Offer offer = mapper.map(offerDTO, Offer.class);
                    offer.setAgent(agentRepository.findByFirstName(offerDTO.getAgent().getName()).orElse(null));
                    offer.setApartment(apartmentRepository.findById(offerDTO.getApartment().getId()).orElse(null));

                    return offer;
                })
                .forEach(offerRepository::saveAndFlush);

        return sb.toString().trim();
    }

    @Override
    public String exportOffers() {
        List<ExportBestOfferDTO> bestOffers = offerRepository.findBestOffers();

        if (!sb.isEmpty()) {
            sb.setLength(0);
        }

        bestOffers.forEach(offer -> sb.append("Agent ").append(offer.getAgentFullName()).append(" with offer №")
                .append(offer.getOfferId()).append(":\n").append("  -ApartmentArea: ")
                .append(offer.getApartmentArea()).append("\n").append("  --Town: ").append(offer.getTownName()).append("\n")
                .append("  ---Price: ").append(offer.getPrice()).append("$\n"));

        return sb.toString().trim();
    }
}
