package models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class DataModel {
    private int id;
    private String name;
    private String year;
    private String color;
    private String pantone_value;
}
