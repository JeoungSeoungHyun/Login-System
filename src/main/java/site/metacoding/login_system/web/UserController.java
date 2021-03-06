package site.metacoding.login_system.web;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import site.metacoding.login_system.config.auth.LoginUser;
import site.metacoding.login_system.domain.User;
import site.metacoding.login_system.handler.ex.CustomException;
import site.metacoding.login_system.service.UserService;
import site.metacoding.login_system.util.UtilValid;
import site.metacoding.login_system.web.dto.JusoRespDto;
import site.metacoding.login_system.web.dto.user.InfoUpdateReqDto;
import site.metacoding.login_system.web.dto.user.JoinReqDto;
import site.metacoding.login_system.web.dto.user.PasswordUpdateReqDto;
import site.metacoding.login_system.web.dto.user.UserDetailRespDto;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/join-form")
    public String joinForm(String roadFullAddr, Model model) {
        return "user/joinForm";
    }

    @PostMapping("/join")
    public String join(@Valid JoinReqDto joinReqDto, BindingResult bindingResult) {

        UtilValid.???????????????????????????(bindingResult);
        userService.????????????(joinReqDto.toEntity());

        return "redirect:/login-form";
    }

    // ????????? ??????
    @GetMapping("/popup")
    public String addrFind() {
        return "jusoPopup";
    }

    // ??????API ?????? ?????? ??????(Post)
    @PostMapping("/popup")
    public String addrResp(JusoRespDto jusoRespDto, Model model) {

        model.addAttribute("data", jusoRespDto);

        return "jusoPopup";
    }

    // ????????? ???????????? ?????? ??????
    @GetMapping("/api/user/username/same-check")
    public ResponseEntity<?> usermaeSameCheck(String username) {
        // true(?????? ??????.)
        boolean isNotSame = userService.????????????????????????(username);
        return new ResponseEntity<>(isNotSame, HttpStatus.OK);
    }

    // ???????????? ?????? ??????
    @GetMapping("/s/user/{userId}")
    public String userDetail(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser, Model model) {
        User principal = (User) loginUser.getUser();

        UserDetailRespDto updateRespDto = userService.??????????????????(userId, principal);
        model.addAttribute("data", updateRespDto);

        return "user/userDetail";
    }

    @GetMapping("/s/user/{userId}/info-update-form")
    public String infoUpdateForm(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser,
            Model model) {
        User principal = (User) loginUser.getUser();

        UserDetailRespDto updateRespDto = userService.??????????????????(userId, principal);
        model.addAttribute("data", updateRespDto);

        return "user/infoUpdateForm";
    }

    @PutMapping("/s/api/user/{userId}/info")
    public ResponseEntity<?> infoUpdate(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody InfoUpdateReqDto infoUpdateReqDto, BindingResult bindingResult) {
        User principal = (User) loginUser.getUser();
        boolean result = false;

        UtilValid.???????????????????????????(bindingResult);
        result = userService.??????????????????(userId, principal, infoUpdateReqDto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/s/user/{userId}/password-update-form")
    public String passwordUpdateForm(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser,
            Model model) {
        User principal = (User) loginUser.getUser();
        // ????????????
        if (userId != principal.getId()) {
            throw new CustomException("????????? ????????????");
        }
        return "user/passwordUpdateForm";
    }

    @PutMapping("/s/api/user/{userId}/password")
    public ResponseEntity<?> passwordUpdate(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser,
            @Valid @RequestBody PasswordUpdateReqDto passwordUpdateReqDto, BindingResult bindingResult) {
        User principal = (User) loginUser.getUser();
        boolean result = false;

        UtilValid.???????????????????????????(bindingResult);
        result = userService.??????????????????(userId, principal, passwordUpdateReqDto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/s/api/user/{userId}/img")
    public ResponseEntity<?> imgUpdate(@PathVariable Integer userId, @AuthenticationPrincipal LoginUser loginUser,
            MultipartFile profileImgFile) {
        User principal = (User) loginUser.getUser();
        boolean result = false;

        result = userService.????????????????????????(session, principal, profileImgFile);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/s/api/user/{userId}")
    public ResponseEntity<?> deleteUser() {
        boolean result = false;

        result = userService.????????????(session);
        System.out.println("result : " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
