package id.kawahedukasi.controller;


import id.kawahedukasi.model.dto.UploadItemRequest;
import id.kawahedukasi.service.ExcelService;
import id.kawahedukasi.service.ItemService;
import id.kawahedukasi.service.ReportService;
import id.kawahedukasi.service.ScheduleService;
import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.engine.JRException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {

    @Inject
    ItemService itemService;

    @Inject
    ReportService reportService;

    @Inject
    ExcelService excelService;

    @GET
    @Path("/report")
    @Produces("application/pdf")
    public Response create() throws JRException {
        return reportService.exportJasper();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartForm UploadItemRequest request) throws IOException {
        return excelService.upload(request);
    }

    @GET
    @Path("/download")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response download() throws IOException {
        return excelService.download();
    }

    @POST
    public Response create(JsonObject request) {
        return itemService.create(request);
    }

    @GET
    public Response getAll() {
        return itemService.getAll();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Integer id, JsonObject request) {
        return itemService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        return itemService.delete(id);
    }
}
