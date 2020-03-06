package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class LicentaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Licenta.class);
        Licenta licenta1 = new Licenta();
        licenta1.setId(1L);
        Licenta licenta2 = new Licenta();
        licenta2.setId(licenta1.getId());
        assertThat(licenta1).isEqualTo(licenta2);
        licenta2.setId(2L);
        assertThat(licenta1).isNotEqualTo(licenta2);
        licenta1.setId(null);
        assertThat(licenta1).isNotEqualTo(licenta2);
    }
}
