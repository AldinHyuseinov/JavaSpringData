package softuni.exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.exam.models.dto.ApartmentsXmlDTO;
import softuni.exam.models.dto.OffersXmlDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper createMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson createGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Bean
    public StringBuilder createStringBuilder() {
        return new StringBuilder();
    }

    @Bean(name = "jaxbApartmentsContext")
    public JAXBContext createJaxbApartmentsInstance() throws JAXBException {
        return JAXBContext.newInstance(ApartmentsXmlDTO.class);
    }

    @Bean(name = "jaxbOffersContext")
    public JAXBContext createJaxbOffersInstance() throws JAXBException {
        return JAXBContext.newInstance(OffersXmlDTO.class);
    }
}
