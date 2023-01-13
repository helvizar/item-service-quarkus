package id.kawahedukasi.service;

import id.kawahedukasi.controller.ItemController;
import id.kawahedukasi.model.Item;
import io.quarkus.scheduler.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ScheduleService {
    Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    //menghapus item yang jumlahnya 0
    @Scheduled(every = "1h")
    @Transactional
    public void getDeleteItem() {

        Item item = new Item();
        Long itemCount = item.getCount();

        if(itemCount == 0) {
            item.delete();
        }

        logger.info("Item kosong telah dihapus");
    }
}
