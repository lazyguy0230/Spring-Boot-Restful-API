package com.example.msuser.controller;

import com.example.msuser.dto.UserRequest;
import com.example.msuser.dto.UserResponse;
import com.example.msuser.entity.User;
import com.example.msuser.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class WebUserController {

    @Autowired
    private UserService userService;

    // 顯示所有用戶
    @GetMapping
    public String listPage(Model model) {
        List<UserResponse> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    // 顯示新增用戶表單頁面
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "user-form";
    }

    // 提交新增操作
    @PostMapping
    public String createUser(@Valid @ModelAttribute("userRequest") UserRequest userRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-form";
        }
        userService.createUser(userRequest);
        return "redirect:/users";
    }

    // 顯示編輯用戶表單
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserResponse userResponse = userService.getUserById(id).orElse(null);
        if (userResponse == null) {
            return "redirect:/users";
        }
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(userResponse.getUsername());
        userRequest.setRole(userResponse.getRole());
        model.addAttribute("userRequest", userRequest);
        model.addAttribute("userId", id);
        return "user-form";
    }

    // 提交編輯操作
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userRequest") UserRequest userRequest,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-form";
        }
        userService.updateUser(id, userRequest);
        return "redirect:/users";
    }

    // 刪除用戶
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
