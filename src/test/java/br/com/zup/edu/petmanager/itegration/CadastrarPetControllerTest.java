package br.com.zup.edu.petmanager.itegration;

import br.com.zup.edu.petmanager.controller.request.PetRequest;
import br.com.zup.edu.petmanager.controller.request.TipoPetRequest;
import br.com.zup.edu.petmanager.core.BaseIntegration;
import br.com.zup.edu.petmanager.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastrarPetControllerTest extends BaseIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetRepository petRepository;


    @BeforeEach
    void setup() {
        petRepository.deleteAll();
    }


    @Test
    void deveCadastrarUmNovoPetComSucesso() throws Exception {
        PetRequest petRequest = new PetRequest("Bob", "Poodle", TipoPetRequest.CAO, LocalDate.of(200, Month.APRIL, 3));

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://localhost/pets/*"));

        assertThat(petRepository.findAll()).size().isEqualTo(1);

    }

    @Test
    void naoDeveCadastrarUmPetComCampoNomeInvalido() throws Exception {
        PetRequest petRequest = new PetRequest(" ", "Poodle", TipoPetRequest.CAO, LocalDate.of(200, Month.APRIL, 3));

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("O campo nome não deve estar em branco"));
        assertThat(petRepository.findAll()).size().isEqualTo(0);
    }

    @Test
    void naoDeveCadastrarUmPetComCampoRacaInvalido() throws Exception {
        PetRequest petRequest = new PetRequest("Bod", " ", TipoPetRequest.CAO, LocalDate.of(200, Month.APRIL, 3));

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("O raca nome não deve estar em branco"));
        assertThat(petRepository.findAll()).size().isEqualTo(0);
    }


    @Test
    void naoDeveCadastrarUmPetComCampoTipoInvalido() throws Exception {
        PetRequest petRequest = new PetRequest("Bod", " ", null, LocalDate.of(200, Month.APRIL, 3));

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("O campo tipo não deve ser nulo"));
        assertThat(payloadResponse.contains("O campo raca não deve estar em branco"));
        assertThat(petRepository.findAll()).size().isEqualTo(0);
    }

    @Test
    void naoDeveCadastrarUmPetComCampoDataNascimentoNoPresente() throws Exception {
        PetRequest petRequest = new PetRequest("Bod", " ", null, LocalDate.now());

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("O campo dataNascimento deve ser uma data passada"));
        assertThat(petRepository.findAll()).size().isEqualTo(0);
    }

    @Test
    void naoDeveCadastrarUmPetComCampoDataNascimentoNoFuturo() throws Exception {
        PetRequest petRequest = new PetRequest("Bod", " ", null, LocalDate.now().plusDays(1));

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept-Language", "pt-br")
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(payloadResponse.contains("O campo dataNascimento deve ser uma data passada"));
        assertThat(petRepository.findAll()).size().isEqualTo(0);
    }
}