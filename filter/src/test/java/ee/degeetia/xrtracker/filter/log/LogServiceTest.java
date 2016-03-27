package ee.degeetia.xrtracker.filter.log;

import ee.degeetia.xrtracker.filter.config.properties.Property;
import ee.degeetia.xrtracker.filter.config.properties.RuntimeProperty;
import ee.degeetia.xrtracker.filter.http.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {

  @InjectMocks
  private LogService logService;
  @Mock
  private HttpClient httpClient;

  private LogEntry testEntry;

  @Before
  public void setUp() {
    testEntry = new LogEntry();
    testEntry.setPersoncode("33311110000");
    testEntry.setAction("action");

    RuntimeProperty.APPLICATION_URL.setValue("http://localhost:8123");
  }

  @After
  public void tearDown() {
    RuntimeProperty.APPLICATION_URL.setValue(null);
  }

  @Test
  public void testCreateEntry_absolutePath() throws Exception {
    Property.LOGGER_REST_URL.setValue("http://127.0.0.1/api/log");
    logService.createEntry(testEntry);
    verify(httpClient).post(new URL("http://127.0.0.1/api/log"), testEntry);
  }

  @Test
  public void testCreateEntry_relativePath() throws Exception {
    Property.LOGGER_REST_URL.setValue("/api/log");
    logService.createEntry(testEntry);
    verify(httpClient).post(new URL("http://localhost:8123/api/log"), testEntry);
  }

  @Test
  public void testCreateEntry_relativePath_missingSlash() throws Exception {
    Property.LOGGER_REST_URL.setValue("api/log");
    logService.createEntry(testEntry);
    verify(httpClient).post(new URL("http://localhost:8123/api/log"), testEntry);
  }

}