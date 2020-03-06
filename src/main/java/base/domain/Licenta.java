package base.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Licenta.
 */
@Entity
@Table(name = "licenta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Licenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "denumire", nullable = false)
    private String denumire;

    @Column(name = "descriere")
    private String descriere;

    @Column(name = "atribuita")
    private Boolean atribuita;

    @OneToOne
    @JoinColumn(unique = true)
    private StudentInfo studentInfo;

    @OneToMany(mappedBy = "licenta")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AplicareLicenta> aplicareLicentas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("licentes")
    private ProfesorInfo profesor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public Licenta denumire(String denumire) {
        this.denumire = denumire;
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public Licenta descriere(String descriere) {
        this.descriere = descriere;
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Boolean isAtribuita() {
        return atribuita;
    }

    public Licenta atribuita(Boolean atribuita) {
        this.atribuita = atribuita;
        return this;
    }

    public void setAtribuita(Boolean atribuita) {
        this.atribuita = atribuita;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public Licenta studentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
        return this;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public Set<AplicareLicenta> getAplicareLicentas() {
        return aplicareLicentas;
    }

    public Licenta aplicareLicentas(Set<AplicareLicenta> aplicareLicentas) {
        this.aplicareLicentas = aplicareLicentas;
        return this;
    }

    public Licenta addAplicareLicenta(AplicareLicenta aplicareLicenta) {
        this.aplicareLicentas.add(aplicareLicenta);
        aplicareLicenta.setLicenta(this);
        return this;
    }

    public Licenta removeAplicareLicenta(AplicareLicenta aplicareLicenta) {
        this.aplicareLicentas.remove(aplicareLicenta);
        aplicareLicenta.setLicenta(null);
        return this;
    }

    public void setAplicareLicentas(Set<AplicareLicenta> aplicareLicentas) {
        this.aplicareLicentas = aplicareLicentas;
    }

    public ProfesorInfo getProfesor() {
        return profesor;
    }

    public Licenta profesor(ProfesorInfo profesorInfo) {
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
        if (!(o instanceof Licenta)) {
            return false;
        }
        return id != null && id.equals(((Licenta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Licenta{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", atribuita='" + isAtribuita() + "'" +
            "}";
    }
}
