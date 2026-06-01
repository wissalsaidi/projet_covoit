package fr.urca.covourca.service;

import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur u = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        var roles = u.isEstConducteur()
            ? List.of(new SimpleGrantedAuthority("ROLE_CONDUCTEUR"), new SimpleGrantedAuthority("ROLE_PASSAGER"))
            : List.of(new SimpleGrantedAuthority("ROLE_PASSAGER"));

        return new User(u.getEmail(), u.getMotDePasse(), roles);
    }
}
