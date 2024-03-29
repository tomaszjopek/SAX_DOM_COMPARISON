select XMLELEMENT("INVENTORIES",
                  XMLAGG(
                      XMLELEMENT("INVENTORY",
                                 XMLATTRIBUTES(i.QUANTITY as "quantity"),
                                 XMLELEMENT("PRODUCT",
                                            XMLATTRIBUTES(p.product_id as "id", p.CATEGORY_ID as "category_id"),
                                            XMLFOREST(
                                                p.product_name as "product_name",
                                                p.DESCRIPTION as "description",
                                                p.LIST_PRICE as "price",
                                                p.STANDARD_COST as "cost"
                                              )
                                   ),
                                 XMLELEMENT("WAREHOUSE",
                                            XMLATTRIBUTES(w.WAREHOUSE_ID as "id"),
                                            XMLELEMENT("warehouse_name", w.WAREHOUSE_NAME),
                                            XMLELEMENT("LOCATION",
                                                       XMLATTRIBUTES(l.LAT as "lat", l.LNG as "lng"),
                                                       XMLFOREST(
                                                           l.CITY as "city",
                                                           l.COUNTRY_ID as "country"
                                                         )
                                              )
                                   )
                        )
                    )
         )
from INVENTORIES i
       JOIN PRODUCTS p on i.product_id = p.product_id
       join WAREHOUSES w on w.WAREHOUSE_ID = i.WAREHOUSE_ID
       join locations l on l.LOCATION_ID = w.LOCATION_ID
where ROWNUM < 2;

