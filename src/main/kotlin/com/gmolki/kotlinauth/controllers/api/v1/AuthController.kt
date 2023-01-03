package com.gmolki.kotlinauth.controllers.api.v1

import com.gmolki.kotlinauth.dtos.requests.LoginRequest
import com.gmolki.kotlinauth.dtos.requests.SignupRequest
import com.gmolki.kotlinauth.dtos.responses.JwtResponse
import com.gmolki.kotlinauth.dtos.responses.MessageResponse
import com.gmolki.kotlinauth.models.ERole
import com.gmolki.kotlinauth.models.Role
import com.gmolki.kotlinauth.models.User
import com.gmolki.kotlinauth.repositories.RoleRepository
import com.gmolki.kotlinauth.repositories.UserRepository
import com.gmolki.kotlinauth.security.jwt.JwtUtils
import com.gmolki.kotlinauth.security.services.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    // see: https://www.rfc-editor.org/rfc/rfc9110.html#section-11
    @PostMapping("/tokens")
    fun tokens() {
//        TODO: implement following convention - https://datatracker.ietf.org/doc/html/rfc6749#section-3.2
//        It should receive grant_type in request body in order to know which action to execute
//        e.g. `grant_type=refresh_token` may refresh the JWT pair for the user generating new access and refresh JWTs
    }

    @PostMapping("/register")
    fun register() {
//        TODO: implement following convention - https://datatracker.ietf.org/doc/html/rfc6749#section-2
//        Expect to receive header: Authorization: Basic $(credentials)
//        e.g. `Authorization: Basic czZCaGRSa3F0Mzo3RmpmcDBaQnIxS3REUmJuZlZkbUl3`
    }



    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any?> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication

        println("Authentication ========= $authentication")

        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles: List<String> = userDetails.authorities.stream()
            .map { item: GrantedAuthority -> item.authority }
            .collect(Collectors.toList())

        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                userDetails.getId().toString(),
                userDetails.username,
                userDetails.getEmail(),
                roles
            )
        )
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignupRequest): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity
                .badRequest()
                .body(MessageResponse("Error: Email is already in use!"))
        }

        // Create new user's account
        val user = User(
            username = signUpRequest.username,
            email = signUpRequest.email,
            password = encoder.encode(signUpRequest.password)
        )
        val strRoles: Set<String>? = signUpRequest.roles
        val roles: MutableSet<Role> = HashSet()
        if (strRoles == null) {
            val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow { RuntimeException("Error: Role is not found.") }
            roles.add(userRole)
        } else {
            strRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        val adminRole: Role = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow {
                                RuntimeException(
                                    "Error: Role is not found."
                                )
                            }
                        roles.add(adminRole)
                    }

                    "mod" -> {
                        val modRole: Role = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow {
                                RuntimeException(
                                    "Error: Role is not found."
                                )
                            }
                        roles.add(modRole)
                    }

                    else -> {
                        val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow {
                                RuntimeException(
                                    "Error: Role is not found."
                                )
                            }
                        roles.add(userRole)
                    }
                }
            })
        }

        user.roles = roles
        userRepository.save(user)

        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}