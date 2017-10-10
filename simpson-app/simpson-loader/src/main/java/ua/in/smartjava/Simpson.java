package ua.in.smartjava;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "of")
public class Simpson {

    private String id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String salary;
    private String password;
    private String about;

}