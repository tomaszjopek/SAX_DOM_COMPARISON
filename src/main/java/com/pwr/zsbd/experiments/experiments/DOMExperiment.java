package com.pwr.zsbd.experiments.experiments;

import com.pwr.zsbd.experiments.dao.InventoryDAO;
import com.pwr.zsbd.experiments.model.Inventory;
import com.pwr.zsbd.experiments.model.Location;
import com.pwr.zsbd.experiments.model.Product;
import com.pwr.zsbd.experiments.model.Warehouse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class DOMExperiment implements Experiment {

    private final InventoryDAO inventoryDAO;
    private final DocumentBuilder dBuilder;

    @Getter
    private final List<Long> times = new LinkedList<>();

    @Getter
    private List<Inventory> inventories;

    @Autowired
    public DOMExperiment(InventoryDAO inventoryDAO, DocumentBuilder documentBuilder) {
        this.inventoryDAO = inventoryDAO;
        this.dBuilder = documentBuilder;
    }

    @Override
    public void startMeasurement() throws IOException, SAXException {
        log.info("Starting time measurement in DOM Experiment");
        inventoryDAO.clearBufferCache();
        inventoryDAO.clearSharedPool();

        inventories = new LinkedList<>();

        long startTime = System.currentTimeMillis();
        List<String> resultSet = inventoryDAO.loadXML();
        String xml = resultSet.get(0);

        // parsing
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("INVENTORY");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Inventory inventory = getInventory(nodeList.item(i));
            inventories.add(inventory);
        }


        long endTime = System.currentTimeMillis();
        times.add(endTime - startTime);

        log.info("SIZE = " + nodeList.getLength());
        log.info("TIME ELAPSED: " + (endTime - startTime) + " [ms]");
    }

    private Inventory getInventory(Node item) {
        Inventory inventory = new Inventory();
        Double quantity = Double.valueOf(item.getAttributes().getNamedItem("quantity").getNodeValue().replace(",", "."));
        inventory.setQuantity(quantity);

        NodeList nodeList = item.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("PRODUCT")) {
                inventory.setProduct(convertProduct(nodeList.item(i)));
            } else if (nodeList.item(i).getNodeName().equals("WAREHOUSE")) {
                inventory.setWarehouse(convertWarehouse(nodeList.item(i)));
            }
        }

        return inventory;
    }

    private Warehouse convertWarehouse(Node item) {
        Warehouse warehouse = new Warehouse();

        int id = Integer.parseInt(item.getAttributes().getNamedItem("id").getNodeValue());
        warehouse.setId(id);

        if (item.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) item;
            warehouse.setWarehouseName(getTagValue("warehouse_name", element));
        }

        NodeList nodeList = item.getChildNodes();
        Node node = nodeList.item(1);

        Location location = convertLocation(node);
        warehouse.setLocation(location);

        return warehouse;
    }

    private Location convertLocation(Node node) {
        Location location = new Location();

        Double lat = null;
        Double lng = null;

        if(node.getAttributes().getNamedItem("lat") != null)
            lat = Double.valueOf(node.getAttributes().getNamedItem("lat").getNodeValue().replace(",", "."));
        if(node.getAttributes().getNamedItem("lng") != null)
            lng = Double.valueOf(node.getAttributes().getNamedItem("lng").getNodeValue().replace(",", "."));

        location.setLat(lat);
        location.setLng(lng);

        location.setCity(getTagValue("city", (Element) node));
        location.setCountry(getTagValue("country", (Element) node));

        return location;
    }

    private Product convertProduct(Node item) {
        Product product = new Product();

        int id = Integer.parseInt(item.getAttributes().getNamedItem("id").getNodeValue());
        int categoryId = Integer.parseInt(item.getAttributes().getNamedItem("category_id").getNodeValue());

        product.setId(id);
        product.setCategoryId(categoryId);

        if (item.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) item;
            product.setProductName(getTagValue("product_name", element));
            product.setDescription(getTagValue("description", element));
            product.setPrice(Double.valueOf(getTagValue("price", element).replace(",", ".")));
            product.setCost(Double.valueOf(getTagValue("cost", element).replace(",", ".")));
        }

        return product;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);

        return node.getNodeValue();
    }
}
