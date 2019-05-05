package com.pwr.zsbd.experiments.experiments;

import org.xml.sax.SAXException;

import java.io.IOException;

public interface Experiment {

    void startMeasurement() throws IOException, SAXException;

}
