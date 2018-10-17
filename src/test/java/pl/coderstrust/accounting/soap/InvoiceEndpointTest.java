//package pl.coderstrust.accounting.soap;
//
//import static org.springframework.ws.test.server.RequestCreators.withPayload;
//import static org.springframework.ws.test.server.ResponseMatchers.noFault;
//import static org.springframework.ws.test.server.ResponseMatchers.payload;
//import static org.springframework.ws.test.server.ResponseMatchers.validPayload;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import javax.xml.transform.Source;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.ws.test.server.MockWebServiceClient;
//import org.springframework.xml.transform.StringSource;
//import pl.coderstrust.accounting.logic.InvoiceService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class InvoiceEndpointTest {
//
//  private static final String REQUESTS_PATH = "src/test/resources/SoapXmlRequests/";
//
//  @Autowired
//  private ApplicationContext applicationContext;
//  @Autowired
//  InvoiceService invoiceService;
//
//  private MockWebServiceClient mockClient;
//  private Resource xsdSchema = new ClassPathResource("invoices.xsd");
//
//  @Before
//  public void init() {
//    mockClient = MockWebServiceClient.createClient(applicationContext);
//    invoiceService.clearDatabase();
//  }
//
//  @Test
//  public void shouldReturnInvoiceById() throws IOException {
//    Source requestPayload = composeRequest("addInvoiceRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        .andExpect(noFault());
//
//    Source requestPayload2 = composeRequest("getInvoiceByIdRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload2))
//        //then
//        .andExpect(noFault())
//        .andExpect(validPayload(xsdSchema));
//  }
//
//  @Test
//  public void addInvoice() throws IOException {
//    Source requestPayload = composeRequest("addInvoiceRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        //then
//        .andExpect(noFault())
//        .andExpect(validPayload(xsdSchema));
//  }
//
//  @Test
//  //@Transactional
//  public void returnInvoicesWithinDateRange() throws IOException {
//    Source requestPayload = composeRequest("addInvoiceWithUniqueDate.xml");
//    for (int i = 0; i < 3; i++) {
//      mockClient
//          .sendRequest(withPayload(requestPayload))
//          //then
//          .andExpect(noFault());
//    }
//
//    requestPayload = composeRequest("getInvoicesByDateRequest.xml");
//    Source responsePayload = composeRequest("getInvoicesByDateResponse.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        //then
//        .andExpect(noFault())
//        .andExpect(validPayload(xsdSchema))
//        .andExpect(payload(responsePayload));
//  }
//
//  @Test
//  public void shouldUpdateInvoice() throws IOException {
//    Source requestPayload = composeRequest("addInvoiceRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        //then
//        .andExpect(noFault());
//
//    requestPayload = composeRequest("updateInvoiceRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        //then
//        .andExpect(noFault());
//
//    requestPayload = composeRequest("getInvoiceByIdRequest.xml");
//    Source responsePayload = composeRequest("updateInvoiceResponse.xml");
//
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        .andExpect(noFault())
//        .andExpect(validPayload(xsdSchema))
//        .andExpect(payload(responsePayload));
//  }
//
//  @Test
//  public void shouldRemoveInvoice() throws IOException {
//
//    Source requestPayload = composeRequest("addInvoiceRequest.xml");
//    for (int i = 0; i < 5; i++) {
//      mockClient
//          .sendRequest(withPayload(requestPayload))
//          .andExpect(noFault());
//    }
//
//    requestPayload = composeRequest("removeInvoiceRequest.xml");
//    mockClient
//        .sendRequest(withPayload(requestPayload))
//        //then
//        .andExpect(noFault());
//  }
//
//  private Source composeRequest(String xmlRequestPath) throws IOException {
//    String filePath = REQUESTS_PATH + xmlRequestPath;
//    String xmlRequest = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
//    Source requestPayload = new StringSource(xmlRequest);
//    return requestPayload;
//  }
//}