package com.pwr.zsbd.experiments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.*;

@Configuration
@ComponentScan(basePackages = "com.pwr.zsbd.experiments")
public class Config {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        ds.setUsername("ZSB_TEST");
        ds.setPassword("passwd");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public SAXParser saxParser() throws ParserConfigurationException, SAXException {
        return SAXParserFactory.newInstance().newSAXParser();
    }

    @Bean
    public DocumentBuilderFactory documentBuilderFactory() {
        return DocumentBuilderFactory.newInstance();
    }

    @Bean
    public DocumentBuilder documentBuilder() throws ParserConfigurationException {
        return documentBuilderFactory().newDocumentBuilder();
    }

}
