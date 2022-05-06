package br.com.zup.edu.petmanager.core;

import br.com.zup.edu.petmanager.PetmanagerApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PetmanagerApplication.class)
public class BaseIntegration {
}
