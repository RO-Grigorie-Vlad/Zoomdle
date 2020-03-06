package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class ConsultatieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultatie.class);
        Consultatie consultatie1 = new Consultatie();
        consultatie1.setId(1L);
        Consultatie consultatie2 = new Consultatie();
        consultatie2.setId(consultatie1.getId());
        assertThat(consultatie1).isEqualTo(consultatie2);
        consultatie2.setId(2L);
        assertThat(consultatie1).isNotEqualTo(consultatie2);
        consultatie1.setId(null);
        assertThat(consultatie1).isNotEqualTo(consultatie2);
    }
}
