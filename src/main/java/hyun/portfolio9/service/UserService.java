package hyun.portfolio9.service;

import hyun.portfolio9.entities.dto.EditUserDto;
import hyun.portfolio9.entities.User;
import hyun.portfolio9.entities.dto.JoinDto;
import hyun.portfolio9.entities.dto.ResponseUserInfoDto;
import hyun.portfolio9.entities.references.Role;
import hyun.portfolio9.exceptions.TokenNotFoundException;
import hyun.portfolio9.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProviderService jwtProviderService;

    public String create(JoinDto dto, HttpServletResponse response) {
        if(!(userRepository.findByUserNickName(dto.getUserNickName()) == null)){
            return "이미 있는 닉네임입니다.";
        }
        if(!(userRepository.findByUserEmail(dto.getUserNickName()) == null)) {
            return "이미 있는 이메일입니다.";
        }

        User user = new User();
        user.setUserNickName(dto.getUserNickName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        user.setUserBirth(dto.getUserBirth());
        user.setUserRole(Role.GUEST);
        userRepository.save(user);

        String jwtToken = jwtProviderService.create(user.getUserEmail());
        response.addHeader("Authorization", "Bearer " + jwtToken);
        return "create " + user.getUserRole();
    }

    public ResponseUserInfoDto getUser(String token) throws TokenNotFoundException {
        if(token == null){
            throw new TokenNotFoundException("Token 없음");
        }
        String findUsername = jwtProviderService.validate(token);
        User findUser = userRepository.findByUserEmail(findUsername);
        ResponseUserInfoDto responseUser = new ResponseUserInfoDto();
        responseUser.setUserId(findUser.getUserId());
        responseUser.setUserNickName(findUser.getUserNickName());
        responseUser.setUserBirth(findUser.getUserBirth());
        responseUser.setUserEmail(findUser.getUserEmail());
        responseUser.setUserPhone(findUser.getUserPhone());
        responseUser.setUserAddress(findUser.getUserAddress());
        // 이메일, 전번, 주소는 엔티티에 아직 안만들었음 만들면 추가 하도록!
        return responseUser;
    }

    public String editUser(EditUserDto dto) {
        Optional<User> foundUser = userRepository.findById(dto.getUserId());
        if(foundUser != null) {
            User user = User.builder()
                    .userId(dto.getUserId())
                    .userNickName(dto.getEditNickName())
                    .userPassword(foundUser.get().getUserPassword())
                    .userBirth(foundUser.get().getUserBirth())
                    .userEmail(dto.getEditEmail())
                    .userPhone(dto.getEditPhone())
                    .userAddress(dto.getEditAddress())
                    .userRole(foundUser.get().getUserRole())
                    .postsList(foundUser.get().getPostsList())
                    .build();

            userRepository.save(user);
            return "수정 완료";
        }
        return "수정 실패";
    }
}
