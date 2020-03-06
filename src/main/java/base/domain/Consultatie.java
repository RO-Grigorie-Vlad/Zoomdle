package base.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Consultatie.
 */
@Entity
@Table(name = "consultatie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Consultatie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @Column(name = "rezolvata")
    private Boolean rezolvata;

    @Column(name = "acceptata")
    private Boolean acceptata;

    @OneToMany(mappedBy = "consultatie")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AplicareConsultatie> aplicareConsultaties = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value="consults", allowSetters = true)
    private StudentInfo student;

    @ManyToOne
    @JsonIgnoreProperties("consultaties")
    private ProfesorInfo profesor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Consultatie data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Boolean isRezolvata() {
        return rezolvata;
    }

    public Consultatie rezolvata(Boolean rezolvata) {
        this.rezolvata = rezolvata;
        return this;
    }

    public void setRezolvata(Boolean rezolvata) {
        this.rezolvata = rezolvata;
    }

    public Boolean isAcceptata() {
        return acceptata;
    }

    public Consultatie acceptata(Boolean acceptata) {
        this.acceptata = acceptata;
        return this;
    }

    public void setAcceptata(Boolean acceptata) {
        this.acceptata = acceptata;
    }

    public Set<AplicareConsultatie> getAplicareConsultaties() {
        return aplicareConsultaties;
    }

    public Consultatie aplicareConsultaties(Set<AplicareConsultatie> aplicareConsultaties) {
        this.aplicareConsultaties = aplicareConsultaties;
        return this;
    }

    public Consultatie addAplicareConsultatie(AplicareConsultatie aplicareConsultatie) {
        this.aplicareConsultaties.add(aplicareConsultatie);
        aplicareConsultatie.setConsultatie(this);
        return this;
    }

    public Consultatie removeAplicareConsultatie(AplicareConsultatie aplicareConsultatie) {
        this.aplicareConsultaties.remove(aplicareConsultatie);
        aplicareConsultatie.setConsultatie(null);
        return this;
    }

    public void setAplicareConsultaties(Set<AplicareConsultatie> aplicareConsultaties) {
        this.aplicareConsultaties = aplicareConsultaties;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public Consultatie student(StudentInfo studentInfo) {
        this.student = studentInfo;
        return this;
    }

    public void setStudent(StudentInfo studentInfo) {
        this.student = studentInfo;
    }

    public ProfesorInfo getProfesor() {
        return profesor;
    }

    public Consultatie profesor(ProfesorInfo profesorInfo) {
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
        if (!(o instanceof Consultatie)) {
            return false;
        }
        return id != null && id.equals(((Consultatie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Consultatie{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", rezolvata='" + isRezolvata() + "'" +
            ", acceptata='" + isAcceptata() + "'" +
            "}";
    }
}
