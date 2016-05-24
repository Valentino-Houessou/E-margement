import org.junit.*;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import javax.xml.bind.JAXBException;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
        running(testServer(9000, fakeApplication(inMemoryDatabase())), new HtmlUnitDriver(), new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) throws JAXBException {
                browser.goTo("http://localhost:9000/");
                try {
                    assertTrue(browser.pageSource().contains("E-Margement"));
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

}
