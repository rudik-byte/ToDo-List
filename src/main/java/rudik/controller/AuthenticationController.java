package rudik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rudik.dto.authentication.AuthenticationRequest;
import rudik.dto.authentication.AuthenticationResponse;
import rudik.dto.authentication.RefreshResponse;
import rudik.security.annotation.RefreshAccess;
import rudik.service.JwtGenerationService;
import rudik.service.impl.UserDetailsServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtGenerationService jwtGenerationService;

    /**
     * This method receives a username and password and validates them in a database.
     *
     * @param request a username and password of a user stored in a database.
     * @return constructed access and refresh JWTs based on a user data.
     * @throws InternalAuthenticationServiceException if given username or password are incorrect.
     */
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        return AuthenticationResponse.builder()
                .accessToken(jwtGenerationService.generateAccessToken(userDetails))
                .refreshToken(jwtGenerationService.generateRefreshToken(userDetails))
                .build();
    }

    /**
     * This method receives a refresh JWT and then constructs and gives a new access JWT.
     *
     * @return a constructed access token based on a user data.
     * @throws InternalAuthenticationServiceException if given username or password are incorrect.
     * @throws AccessDeniedException                  if given JWT has no rights for this endpoint.
     */
    @RefreshAccess
    @PostMapping("/refresh")
    public RefreshResponse refreshAccessJWT() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        return RefreshResponse.builder()
                .accessToken(jwtGenerationService.generateAccessToken(userDetails))
                .build();
    }
}
