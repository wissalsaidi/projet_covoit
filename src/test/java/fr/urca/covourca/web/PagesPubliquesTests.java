package fr.urca.covourca.web;

import fr.urca.covourca.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PagesPubliquesTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Test
    void pageAccueilAccessible() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void pageConnexionAccessible() throws Exception {
        mvc.perform(get("/connexion")).andExpect(status().isOk());
    }

    @Test
    void dashboardRedirigeVersConnexionSiNonConnecte() throws Exception {
        mvc.perform(get("/dashboard"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrlPattern("**/connexion"));
    }

    @Test
    void inscriptionPuisConnexion() throws Exception {
        mvc.perform(post("/inscription").with(csrf())
                .param("nom", "Test").param("prenom", "Jenkins")
                .param("email", "jenkins@urca.fr").param("telephone", "0612345678")
                .param("motDePasse", "secret123"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/connexion"));

        assertThat(utilisateurRepository.findByEmail("jenkins@urca.fr")).isPresent();

        mvc.perform(post("/connexion").with(csrf())
                .param("username", "jenkins@urca.fr").param("password", "secret123"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/dashboard"));
    }
}
