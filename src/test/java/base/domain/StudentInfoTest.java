package base.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import base.web.rest.TestUtil;

public class StudentInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentInfo.class);
        StudentInfo studentInfo1 = new StudentInfo();
        studentInfo1.setId(1L);
        StudentInfo studentInfo2 = new StudentInfo();
        studentInfo2.setId(studentInfo1.getId());
        assertThat(studentInfo1).isEqualTo(studentInfo2);
        studentInfo2.setId(2L);
        assertThat(studentInfo1).isNotEqualTo(studentInfo2);
        studentInfo1.setId(null);
        assertThat(studentInfo1).isNotEqualTo(studentInfo2);
    }
}
