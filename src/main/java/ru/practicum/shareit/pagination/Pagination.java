//package ru.practicum.shareit.pagination;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//public class Pagination extends PageRequest {
//
//    private final int from;
//
//    public Pagination(int from, int size) {
//        super(from / size, size, Sort.unsorted());
//        this.from = from;
//    }
//
//    @Override
//    public long getOffset() {
//        return from;
//    }
//}
