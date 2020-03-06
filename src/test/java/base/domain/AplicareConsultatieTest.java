package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class AplicareConsultatieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AplicareConsultatie.class);
        AplicareConsultatie aplicareConsultatie1 = new AplicareConsultatie();
        aplicareConsultatie1.setId(1L);
        AplicareConsultatie aplicareConsultatie2 = new AplicareConsultatie();
        aplicareConsultatie2.setId(aplicareConsultatie1.getId());
        assertThat(aplicareConsultatie1).isEqualTo(aplicareConsultatie2);
        aplicareConsultatie2.setId(2L);
        assertThat(aplicareConsultatie1).isNotEqualTo(aplicareConsultatie2);
        aplicareConsultatie1.setId(null);
        assertThat(aplicareConsultatie1).isNotEqualTo(aplicareConsultatie2);
    }
}
