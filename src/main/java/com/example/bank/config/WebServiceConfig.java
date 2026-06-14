package com.example.bank.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  // WSDL accessible via: http://localhost:8080/ws/bank.wsdl
  @Bean(name = "bank")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema bankSchema) {
    DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
    wsdl.setPortTypeName("BankPort");
    wsdl.setLocationUri("/ws");
    wsdl.setTargetNamespace("http://example.com/bank");
    wsdl.setSchema(bankSchema);
    return wsdl;
  }

  @Bean
  public XsdSchema bankSchema() {
    return new SimpleXsdSchema(new ClassPathResource("bank.xsd"));
  }
}
