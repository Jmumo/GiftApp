package GiftsBackend.Dtos;

import GiftsBackend.Model.Event;
import GiftsBackend.Model.Product;
import lombok.Data;

import java.util.List;

@Data
public class EventSearchResponse {
        private List<Event> productList;
        private Integer nextPage;
        private Integer currentPage;
        private Integer previouspage;

}
