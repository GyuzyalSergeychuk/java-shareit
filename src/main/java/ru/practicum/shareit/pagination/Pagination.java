package ru.practicum.shareit.pagination;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class Pagination<T> {

    public List<T> makePagination(Integer from, Integer size, List<T> list) throws ValidationException {
        if (from < 0 || size < 0 || size == 0) {
            throw new ValidationException("Индекс первого элемента и размер листа не может быть меньше нуля");
        }

        if(size > list.size()){
            size = list.size();
        }
        if (from > list.size()){
            return List.of();
        }
        List<T> listT = new ArrayList<>();
        if ((from + size) - list.size() < size) {
            size = size - ((from + size) - list.size());
        }
        for (int i = from; i < (from + size); i++) {
            listT.add(list.get(i));
        }
        return listT;
    }
}
