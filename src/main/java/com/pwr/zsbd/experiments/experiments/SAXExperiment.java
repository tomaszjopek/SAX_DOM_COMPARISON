package com.pwr.zsbd.experiments.experiments;

import com.pwr.zsbd.experiments.dao.InventoryDAO;
import com.pwr.zsbd.experiments.handlers.InventoryHandler;
import com.pwr.zsbd.experiments.model.Inventory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SAXExperiment implements Experiment {

    private final InventoryDAO inventoryDAO;
    private final SAXParser saxParser;
    private final InventoryHandler inventoryHandler;

    @Getter
    private final List<Long> times = new LinkedList<>();

    @Autowired
    public SAXExperiment(InventoryDAO inventoryDAO, SAXParser saxParser, InventoryHandler inventoryHandler) {
        this.inventoryDAO = inventoryDAO;
        this.saxParser = saxParser;
        this.inventoryHandler = inventoryHandler;
    }

    public void startMeasurement() throws IOException, SAXException {
        inventoryDAO.clearBufferCache();
        inventoryDAO.clearSharedPool();

        log.info("Starting time measurement in SAX Experiment");
        long startTime = System.currentTimeMillis();
        List<String> resultSet = inventoryDAO.loadXML();
        String xml = resultSet.get(0);

        // parsing
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        saxParser.parse(inputStream, inventoryHandler);
        long endTime = System.currentTimeMillis();
        times.add(endTime - startTime);

        log.info("TOTAL INVENTORIES PROCESSED FROM XML: " + inventoryHandler.getInventories().size());
        log.info("TIME ELAPSED: " + (endTime - startTime) + " [ms]");
    }

}
