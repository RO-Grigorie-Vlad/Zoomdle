package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class ProfesorInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfesorInfo.class);
        ProfesorInfo profesorInfo1 = new ProfesorInfo();
        profesorInfo1.setId(1L);
        ProfesorInfo profesorInfo2 = new ProfesorInfo();
        profesorInfo2.setId(profesorInfo1.getId());
        assertThat(profesorInfo1).isEqualTo(profesorInfo2);
        profesorInfo2.setId(2L);
        assertThat(profesorInfo1).isNotEqualTo(profesorInfo2);
        profesorInfo1.setId(null);
        assertThat(profesorInfo1).isNotEqualTo(profesorInfo2);
    }
}
