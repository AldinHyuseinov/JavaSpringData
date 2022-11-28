package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.Messages;
import softuni.exam.models.dto.ImportAgentJsonDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;

    private final TownRepository townRepository;

    private final Gson gson;

    private final ModelMapper mapper;

    private final StringBuilder sb;

    private final ValidationUtil validationUtil;

    private static final Path AGENTS_JSON_PATH = Path.of("src/main/resources/files/json/agents.json");

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository, TownRepository townRepository, Gson gson, ModelMapper mapper, StringBuilder sb, ValidationUtil validationUtil) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.mapper = mapper;
        this.sb = sb;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(AGENTS_JSON_PATH);
    }

    @Override
    @Transactional
    public String importAgents() throws IOException {
        Arrays.stream(gson.fromJson(readAgentsFromFile(), ImportAgentJsonDTO[].class))
                .filter(agentDTO -> {

                    if (validationUtil.isValid(agentDTO)) {

                        if (agentRepository.findByFirstName(agentDTO.getFirstName()).isPresent()) {
                            sb.append(String.format(Messages.INVALID_DATA, "agent")).append("\n");
                            return false;
                        }

                        sb.append(String.format(Messages.SUCCESSFUL_IMPORT_AGENT,
                                        agentDTO.getFirstName(), agentDTO.getLastName()))
                                .append("\n");

                        return true;
                    }
                    sb.append(String.format(Messages.INVALID_DATA, "agent")).append("\n");

                    return false;
                })
                .map(agentDTO -> {
                    Agent agent = mapper.map(agentDTO, Agent.class);
                    agent.setTown(townRepository.findByTownName(agentDTO.getTown()).orElse(null));

                    return agent;
                })
                .forEach(agentRepository::saveAndFlush);

        return sb.toString().trim();
    }
}
