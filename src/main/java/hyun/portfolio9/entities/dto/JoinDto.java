package hyun.portfolio9.entities.dto;

import hyun.portfolio9.entities.references.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto {
    private String userNickName;
    private String userEmail;
    private String userPassword;
    private String userBirth;
}
