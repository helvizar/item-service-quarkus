package id.kawahedukasi.model.dto;

import javax.ws.rs.FormParam;

public class UploadItemRequest {

    @FormParam("file")
    public byte[] file;
}
