package io.github.kbiakov.newsreader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.github.kbiakov.newsreader.di.components.AppTestComponent;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApp.class,
        constants = BuildConfig.class,
        sdk = 21)
@Ignore
public class BaseTest {

    public AppTestComponent component;
    public TestUtils testUtils;

    @Before
    public void setUp() throws Exception {
        component = (AppTestComponent) App.getAppComponent();
        testUtils = new TestUtils();
    }
}
