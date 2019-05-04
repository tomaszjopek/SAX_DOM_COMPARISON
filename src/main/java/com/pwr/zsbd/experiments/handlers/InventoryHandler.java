package com.pwr.zsbd.experiments.handlers;

import com.pwr.zsbd.experiments.model.Inventory;
import com.pwr.zsbd.experiments.model.Location;
import com.pwr.zsbd.experiments.model.Product;
import com.pwr.zsbd.experiments.model.Warehouse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class InventoryHandler extends DefaultHandler {

    @Getter
    @Setter
    private List<Inventory> inventories;

    private Inventory inventory;
    private Product product;
    private Warehouse warehouse;
    private Location location;

    private boolean bInventories;
    private boolean bInventory;
    private boolean bProduct;
    private boolean bWarehouse;
    private boolean bLocation;

    private boolean bProductName;
    private boolean bProductDescription;
    private boolean bProductPrice;
    private boolean bProductCost;

    private boolean bWarehouseName;

    private boolean bLocationCity;
    private boolean bLocationCountry;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        log.info("Start element - " + qName);
        switch (qName.toLowerCase().trim()) {
            case "inventories":
                inventories = new LinkedList<>();
                bInventories = true;
                break;
            case "inventory":
                inventory = new Inventory();
                double quantity = Double.valueOf(attributes.getValue("quantity"));
                inventory.setQuantity(quantity);
                bInventory = true;
                break;
            case "product":
                product = new Product();
                int id = Integer.valueOf(attributes.getValue("id"));
                int categoryId = Integer.valueOf(attributes.getValue("category_id"));
                product.setId(id);
                product.setCategoryId(categoryId);
                bProduct = true;
            case "warehouse":
                warehouse = new Warehouse();
                int idw = Integer.valueOf(attributes.getValue("id"));
                warehouse.setId(idw);
                bWarehouse = true;
            case "location":
                location = new Location();
                Double lat = null;
                Double lng = null;
                if(attributes.getValue("lat") != null)
                    lat = Double.valueOf(attributes.getValue("lat").replace(",", "."));
                if(attributes.getValue("lat") != null)
                    lng = Double.valueOf(attributes.getValue("lng").replace(",", "."));

                location.setLat(lat);
                location.setLng(lng);
                bLocation = true;
            case "product_name":
                bProductName = true;
                break;
            case "description":
                bProductDescription = true;
                break;
            case "price":
                bProductPrice = true;
                break;
            case "cost":
                bProductCost = true;
                break;
            case "warehouse_name":
                bWarehouseName = true;
                break;
            case "city":
                bLocationCity = true;
                break;
            case "country":
                bLocationCountry = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        log.info("End element - " + qName);
        switch (qName.toLowerCase().trim()) {
            case "inventories":
                bInventories = false;
                break;
            case "inventory":
                inventories.add(inventory);
                bInventory = false;
                break;
            case "product":
                inventory.setProduct(product);
                bProduct = false;
            case "warehouse":
                inventory.setWarehouse(warehouse);
                bWarehouse = false;
            case "location":
                warehouse.setLocation(location);
                bLocation = false;
            case "product_name":
                bProductName = false;
                break;
            case "description":
                bProductDescription = false;
                break;
            case "price":
                bProductPrice = false;
                break;
            case "cost":
                bProductCost = false;
                break;
            case "warehouse_name":
                bWarehouseName = false;
                break;
            case "city":
                bLocationCity = false;
                break;
            case "country":
                bLocationCountry = false;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // inventories/inventory/product
        if(bInventory && bProduct && bProductName)
            product.setProductName(String.valueOf(ch, start, length));
        else if(bInventory && bProduct && bProductDescription)
            product.setDescription(String.valueOf(ch, start, length));
        else if(bInventory && bProduct && bProductPrice)
            product.setPrice(Double.valueOf(String.valueOf(ch, start, length).replace(",", ".")));
        else if(bInventory && bProduct && bProductCost)
            product.setCost(Double.valueOf(String.valueOf(ch, start, length).replace(",", ".")));
        // inventories/inventory/warehouse
        else if(bInventory && bWarehouse && bWarehouseName)
            warehouse.setWarehouseName(String.valueOf(ch, start, length));
        // inventories/inventory/warehouse/location
        else if(bInventory && bWarehouse && bLocation && bLocationCity)
            location.setCity(String.valueOf(ch, start, length));
        else if(bInventory && bWarehouse && bLocation && bLocationCountry)
            location.setCountry(String.valueOf(ch, start, length));
    }
}
