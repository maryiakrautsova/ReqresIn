package models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserModel {
    private DataModel data;
    private SupportModel support;
}
