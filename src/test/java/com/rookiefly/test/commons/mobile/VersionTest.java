package com.rookiefly.test.commons.mobile;

import com.rookiefly.commons.mobile.Version;
import org.junit.Test;

public class VersionTest {

    @Test
    public void test01() {
        System.out.println(Version.compare("9.9", "9.9.9", false));
    }

    @Test
    public void test02() {
        System.out.println(Version.compare("10.0.0", "9.9.9"));
    }

    @Test
    public void test03() {
        System.out.println(Version.of("2.1.0").gt("2.0.0"));
    }
}
