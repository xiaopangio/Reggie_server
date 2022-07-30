package org.xiaogang.server.dto;

import lombok.Data;
import org.xiaogang.server.entity.Dish;
import org.xiaogang.server.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
