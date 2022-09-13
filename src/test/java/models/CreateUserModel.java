package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserModel {
    private String name;
    private String job;
}
