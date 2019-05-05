package com.pwr.zsbd.experiments;

import com.pwr.zsbd.experiments.experiments.DOMExperiment;
import com.pwr.zsbd.experiments.experiments.SAXExperiment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.stream.IntStream;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.pwr.zsbd.experiments")
@Component
@Slf4j
public class App {

    private final SAXExperiment saxExperiment;
    private final DOMExperiment domExperiment;

    @Autowired
    public App(SAXExperiment saxExperiment, DOMExperiment domExperiment) {
        this.saxExperiment = saxExperiment;
        this.domExperiment = domExperiment;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        App app = context.getBean(App.class);
        app.run(args);
    }

    private void run(String[] args) {
        log.info("Starting application");

        try {
//            domExperiment.startMeasurement();
//            domExperiment.startMeasurement();
            IntStream.rangeClosed(1, 10).forEach(i -> {
                try {
                    domExperiment.startMeasurement();
                } catch (IOException | SAXException e) {
                    e.printStackTrace();
                }
            });

            log.info("DOM TIMES[ms]: " + domExperiment.getTimes());

//            saxExperiment.startMeasurement();
//            saxExperiment.startMeasurement();
            IntStream.rangeClosed(1, 10).forEach(i -> {
                try {
                    saxExperiment.startMeasurement();
                } catch (IOException | SAXException e) {
                    e.printStackTrace();
                }
            });
            log.info("SAX TIMES[ms]: " + saxExperiment.getTimes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Ending application");
    }

}
