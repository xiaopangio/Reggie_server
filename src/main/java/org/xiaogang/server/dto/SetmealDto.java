package org.xiaogang.server.dto;

import lombok.Data;
import org.xiaogang.server.entity.Setmeal;
import org.xiaogang.server.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
