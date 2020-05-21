package base.web.rest;

import base.config.Constants;
import base.domain.AplicareConsultatie;
import base.domain.AplicareLicenta;
import base.domain.Consultatie;
import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.repository.UserRepository;
import base.security.AuthoritiesConstants;
import base.security.SecurityUtils;
import base.service.AplicareConsultatieService;
import base.service.AplicareLicentaService;
import base.service.ConsultatieService;
import base.service.LicentaService;
import base.service.MailService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
import base.service.dto.UserDTO;
import base.web.rest.errors.BadRequestAlertException;
import base.web.rest.errors.EmailAlreadyUsedException;
import base.web.rest.errors.LoginAlreadyUsedException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


import javax.transaction.Transactional;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final StudentInfoService studentInfoService;

    private final LicentaService licentaService;

    private final UserRepository userRepository;

    private final AplicareLicentaService aplicareLicentaService;

    private final ProfesorInfoService profesorInfoService;

    public final ConsultatieService consultatieService;

    private final AplicareConsultatieService aplicareConsultatieService;

    private final MailService mailService;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService, StudentInfoService studentInfoService, 
                        LicentaService licentaService, AplicareLicentaService aplicareLicentaService, ProfesorInfoService profesorInfoService,
                        ConsultatieService consultatieService, AplicareConsultatieService aplicareConsultatieService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.studentInfoService = studentInfoService;
        this.licentaService = licentaService;
        this.aplicareLicentaService = aplicareLicentaService;
        this.profesorInfoService = profesorInfoService;
        this.consultatieService = consultatieService;
        this.aplicareConsultatieService = aplicareConsultatieService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        Optional<User> user =  this.userService.findOneByLogin(login);
        Boolean isStudent =this.userService.hasRole(user.get(), "ROLE_STUDENT");
        Boolean isProfesor =this.userService.hasRole(user.get(), "ROLE_PROFESOR");
        
        if (isStudent) {
            Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());
            if(student.isPresent()){
                Optional<Licenta> licenta =  this.licentaService.findOneByStudentInfo(student.get());
                if(licenta.isPresent()){
                    Licenta licentaStudentului = licenta.get();
                    licentaStudentului.setStudentInfo(null);
                    licentaStudentului.setAtribuita(false);
                    this.licentaService.save(licentaStudentului);
                }
                List<AplicareLicenta> listaAplicari = this.aplicareLicentaService.findAllByStudent2(student.get().getId());
                if(listaAplicari.size() != 0){
                    for (AplicareLicenta aplicareLicenta : listaAplicari) {
                        Long idAplicare = aplicareLicenta.getId();
                        this.aplicareLicentaService.delete(idAplicare);
                    }
                }
                List<Consultatie> listaConsultatii = this.consultatieService.findAllByStudent(student.get());
                if(listaConsultatii.size() != 0){
                    for (Consultatie consultatie : listaConsultatii) {
                        consultatie.setStudent(null);
                        consultatie.setAcceptata(false);
                        this.consultatieService.save(consultatie);
                    }
                }
                List<AplicareConsultatie> listaAplicariConsultatie = this.aplicareConsultatieService.findAllByStudent2(student.get().getId());
                if(listaAplicariConsultatie.size() != 0){
                    for (AplicareConsultatie aplicareConsultatie : listaAplicariConsultatie) {
                        this.aplicareConsultatieService.delete(aplicareConsultatie.getId());
                    }
                }
                this.studentInfoService.delete(student.get().getId());
            }    
        }
        else if(isProfesor){
            Optional<ProfesorInfo> profesor= this.profesorInfoService.findOneByUser(user.get());
            if(profesor.isPresent()){
                List<Licenta> licenteleProfesorului =  this.licentaService.findAllByProfesor(profesor.get());
                if(licenteleProfesorului.size() != 0){
                    for (Licenta licenta : licenteleProfesorului) {
                        List<AplicareLicenta> listaAplicari =  this.aplicareLicentaService.findAllByLicenta(licenta);
                        if(listaAplicari.size() != 0){
                            for (AplicareLicenta aplicari : listaAplicari) {
                                this.aplicareLicentaService.delete(aplicari.getId());  
                            }
                        }
                        if(licenta.isAtribuita()){
                            StudentInfo student = licenta.getStudentInfo();
                            student.setProfesor(null);
                            this.studentInfoService.save(student);
                        }
                        this.licentaService.delete(licenta.getId());
                    }
                }
                List<Consultatie> consultatiileProfesorului = this.consultatieService.findAllByProfesor(profesor.get());
                if(consultatiileProfesorului.size() != 0){
                    for (Consultatie consultatie : consultatiileProfesorului) {
                        List<AplicareConsultatie> listaAplicariConsultatie = this.aplicareConsultatieService.findAllByConsultatie(consultatie);
                        if(listaAplicariConsultatie.size() != 0){
                            for (AplicareConsultatie aplicareConsultatie : listaAplicariConsultatie) {
                                this.aplicareConsultatieService.delete(aplicareConsultatie.getId());
                            }
                        }
                        this.consultatieService.delete(consultatie.getId());
                    }
                }
                this.profesorInfoService.delete(profesor.get().getId());
            }
            
        }
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "userManagement.deleted", login)).build();
    }
}
