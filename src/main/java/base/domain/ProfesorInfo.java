package base.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProfesorInfo.
 */
@Entity
@Table(name = "profesor_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfesorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "profesor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StudentInfo> studentCoordonats = new HashSet<>();

    @OneToMany(mappedBy = "profesor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Licenta> licentes = new HashSet<>();

    @OneToMany(mappedBy = "profesor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Consultatie> consultaties = new HashSet<>();

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

    public ProfesorInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<StudentInfo> getStudentCoordonats() {
        return studentCoordonats;
    }

    public ProfesorInfo studentCoordonats(Set<StudentInfo> studentInfos) {
        this.studentCoordonats = studentInfos;
        return this;
    }

    public ProfesorInfo addStudentCoordonat(StudentInfo studentInfo) {
        this.studentCoordonats.add(studentInfo);
        studentInfo.setProfesor(this);
        return this;
    }

    public ProfesorInfo removeStudentCoordonat(StudentInfo studentInfo) {
        this.studentCoordonats.remove(studentInfo);
        studentInfo.setProfesor(null);
        return this;
    }

    public void setStudentCoordonats(Set<StudentInfo> studentInfos) {
        this.studentCoordonats = studentInfos;
    }

    public Set<Licenta> getLicentes() {
        return licentes;
    }

    public ProfesorInfo licentes(Set<Licenta> licentas) {
        this.licentes = licentas;
        return this;
    }

    public ProfesorInfo addLicente(Licenta licenta) {
        this.licentes.add(licenta);
        licenta.setProfesor(this);
        return this;
    }

    public ProfesorInfo removeLicente(Licenta licenta) {
        this.licentes.remove(licenta);
        licenta.setProfesor(null);
        return this;
    }

    public void setLicentes(Set<Licenta> licentas) {
        this.licentes = licentas;
    }

    public Set<Consultatie> getConsultaties() {
        return consultaties;
    }

    public ProfesorInfo consultaties(Set<Consultatie> consultaties) {
        this.consultaties = consultaties;
        return this;
    }

    public ProfesorInfo addConsultatie(Consultatie consultatie) {
        this.consultaties.add(consultatie);
        consultatie.setProfesor(this);
        return this;
    }

    public ProfesorInfo removeConsultatie(Consultatie consultatie) {
        this.consultaties.remove(consultatie);
        consultatie.setProfesor(null);
        return this;
    }

    public void setConsultaties(Set<Consultatie> consultaties) {
        this.consultaties = consultaties;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfesorInfo)) {
            return false;
        }
        return id != null && id.equals(((ProfesorInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProfesorInfo{" +
            "id=" + getId() +
            "}";
    }
}
