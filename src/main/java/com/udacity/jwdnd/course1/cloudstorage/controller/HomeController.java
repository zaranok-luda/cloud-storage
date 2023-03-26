package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private UsersMapper usersMapper;

    @GetMapping
    public String home(@ModelAttribute("note") NoteForm note, @ModelAttribute("credential") CredentialForm credentialForm, Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = usersMapper.getUser(username);
        if(user == null) return "signup";

        int userId = user.getUserId();
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("files", fileService.getUserFiles(userId));
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        return "home";
    }
}
