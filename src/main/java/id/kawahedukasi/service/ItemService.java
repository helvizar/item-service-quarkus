package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped

public class ItemService {

    //@create new item data
    @Transactional
    public Response create(JsonObject request) {
        String name = request.getString("name");
        Double price = request.getDouble("price");
        Long count = request.getLong("count");
        String type = request.getString("type");

        if(name == null || price == null || count == null || type == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "DATA_NOT_COMPLETE"))
                    .build();
        }

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setCount(count);
        item.setType(type);
        item.setDescription(request.getString("description"));

        item.persist();

        return Response.status(Response.Status.CREATED).entity(Map.of("id", item.getId())).build();
    }

    //@get all item data
    public Response getAll() {
        List<Item> items = Item.listAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for(Item item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("price", item.getPrice());
            map.put("count", item.getCount());
            map.put("type", item.getType());
            map.put("description", item.getDescription());

            result.add(map);
        }
        return Response.ok().entity(result).build();
    }

    @Transactional
    public Response update(@PathParam("id") Integer id, JsonObject request) {
        String name = request.getString("name");
        Double price = request.getDouble("price");
        Long count = request.getLong("count");
        String type = request.getString("type");

        if(name == null || price == null || count == null || type == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "BAD_REQUEST"))
                    .build();
        }

        Item item = Item.findById(id);
        if(item == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ITEM_NOT_FOUND"))
                    .build();
        }

        item.setName(name);
        item.setPrice(price);
        item.setCount(count);
        item.setType(type);
        item.setDescription(request.getString("description"));

        item.persist();

        return Response.ok().entity(Map.of("id", item.getId())).build();
    }

    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        Item item = Item.findById(id);
        if(item == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message",  "ITEM_ALREADY_DELETED"))
                    .build();
        }

        item.delete();

        return Response.status(Response.Status.NO_CONTENT).entity(Map.of("id", item.getId())).build();
    }
}
