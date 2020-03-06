package base.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AplicareConsultatie.
 */
@Entity
@Table(name = "aplicare_consultatie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AplicareConsultatie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rezolvata")
    private Boolean rezolvata;

    @Column(name = "acceptata")
    private Boolean acceptata;

    @ManyToOne
    @JsonIgnoreProperties(value="aplicareConsults", allowSetters = true)
    private StudentInfo student;

    @ManyToOne
    @JsonIgnoreProperties("aplicareConsultaties")
    private Consultatie consultatie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRezolvata() {
        return rezolvata;
    }

    public AplicareConsultatie rezolvata(Boolean rezolvata) {
        this.rezolvata = rezolvata;
        return this;
    }

    public void setRezolvata(Boolean rezolvata) {
        this.rezolvata = rezolvata;
    }

    public Boolean isAcceptata() {
        return acceptata;
    }

    public AplicareConsultatie acceptata(Boolean acceptata) {
        this.acceptata = acceptata;
        return this;
    }

    public void setAcceptata(Boolean acceptata) {
        this.acceptata = acceptata;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public AplicareConsultatie student(StudentInfo studentInfo) {
        this.student = studentInfo;
        return this;
    }

    public void setStudent(StudentInfo studentInfo) {
        this.student = studentInfo;
    }

    public Consultatie getConsultatie() {
        return consultatie;
    }

    public AplicareConsultatie consultatie(Consultatie consultatie) {
        this.consultatie = consultatie;
        return this;
    }

    public void setConsultatie(Consultatie consultatie) {
        this.consultatie = consultatie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AplicareConsultatie)) {
            return false;
        }
        return id != null && id.equals(((AplicareConsultatie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AplicareConsultatie{" +
            "id=" + getId() +
            ", rezolvata='" + isRezolvata() + "'" +
            ", acceptata='" + isAcceptata() + "'" +
            "}";
    }
}
