package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class AplicareLicentaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AplicareLicenta.class);
        AplicareLicenta aplicareLicenta1 = new AplicareLicenta();
        aplicareLicenta1.setId(1L);
        AplicareLicenta aplicareLicenta2 = new AplicareLicenta();
        aplicareLicenta2.setId(aplicareLicenta1.getId());
        assertThat(aplicareLicenta1).isEqualTo(aplicareLicenta2);
        aplicareLicenta2.setId(2L);
        assertThat(aplicareLicenta1).isNotEqualTo(aplicareLicenta2);
        aplicareLicenta1.setId(null);
        assertThat(aplicareLicenta1).isNotEqualTo(aplicareLicenta2);
    }
}
