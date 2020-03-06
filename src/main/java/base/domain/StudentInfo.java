package base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A StudentInfo.
 */
@Entity
@Table(name = "student_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AplicareLicenta> aplicareLics = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Consultatie> consults = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AplicareConsultatie> aplicareConsults = new HashSet<>();

    @OneToOne(mappedBy = "studentInfo")
    @JsonIgnore
    private Licenta licenta;

    @ManyToOne
    @JsonIgnoreProperties("studentCoordonats")
    private ProfesorInfo profesor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public StudentInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AplicareLicenta> getAplicareLics() {
        return aplicareLics;
    }

    public StudentInfo aplicareLics(Set<AplicareLicenta> aplicareLicentas) {
        this.aplicareLics = aplicareLicentas;
        return this;
    }

    public StudentInfo addAplicareLic(AplicareLicenta aplicareLicenta) {
        this.aplicareLics.add(aplicareLicenta);
        aplicareLicenta.setStudent(this);
        return this;
    }

    public StudentInfo removeAplicareLic(AplicareLicenta aplicareLicenta) {
        this.aplicareLics.remove(aplicareLicenta);
        aplicareLicenta.setStudent(null);
        return this;
    }

    public void setAplicareLics(Set<AplicareLicenta> aplicareLicentas) {
        this.aplicareLics = aplicareLicentas;
    }

    public Set<Consultatie> getConsults() {
        return consults;
    }

    public StudentInfo consults(Set<Consultatie> consultaties) {
        this.consults = consultaties;
        return this;
    }

    public StudentInfo addConsult(Consultatie consultatie) {
        this.consults.add(consultatie);
        consultatie.setStudent(this);
        return this;
    }

    public StudentInfo removeConsult(Consultatie consultatie) {
        this.consults.remove(consultatie);
        consultatie.setStudent(null);
        return this;
    }

    public void setConsults(Set<Consultatie> consultaties) {
        this.consults = consultaties;
    }

    public Set<AplicareConsultatie> getAplicareConsults() {
        return aplicareConsults;
    }

    public StudentInfo aplicareConsults(Set<AplicareConsultatie> aplicareConsultaties) {
        this.aplicareConsults = aplicareConsultaties;
        return this;
    }

    public StudentInfo addAplicareConsult(AplicareConsultatie aplicareConsultatie) {
        this.aplicareConsults.add(aplicareConsultatie);
        aplicareConsultatie.setStudent(this);
        return this;
    }

    public StudentInfo removeAplicareConsult(AplicareConsultatie aplicareConsultatie) {
        this.aplicareConsults.remove(aplicareConsultatie);
        aplicareConsultatie.setStudent(null);
        return this;
    }

    public void setAplicareConsults(Set<AplicareConsultatie> aplicareConsultaties) {
        this.aplicareConsults = aplicareConsultaties;
    }

    public Licenta getLicenta() {
        return licenta;
    }

    public StudentInfo licenta(Licenta licenta) {
        this.licenta = licenta;
        return this;
    }

    public void setLicenta(Licenta licenta) {
        this.licenta = licenta;
    }

    public ProfesorInfo getProfesor() {
        return profesor;
    }

    public StudentInfo profesor(ProfesorInfo profesorInfo) {
        this.profesor = profesorInfo;
        return this;
    }

    public void setProfesor(ProfesorInfo profesorInfo) {
        this.profesor = profesorInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentInfo)) {
            return false;
        }
        return id != null && id.equals(((StudentInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
            "id=" + getId() +
            "}";
    }
}
