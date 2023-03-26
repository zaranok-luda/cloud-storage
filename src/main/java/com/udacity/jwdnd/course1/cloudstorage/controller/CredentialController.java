package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialsService;

    @PostMapping("/credentials")
    public String addCredentials(
            @ModelAttribute("credential") CredentialForm credentialForm, @ModelAttribute("note") NoteForm note,
            Model model, Authentication authentication) {
        String username = authentication.getName();
        int userId = usersMapper.getUser(username).getUserId();
        Credential credential = Credential.builder().userId(userId).username(credentialForm.getUsername())
                .url(credentialForm.getUrl()).decodedPassword(credentialForm.getPassword()).build();

        if (credentialForm.getId() != null && !credentialForm.getId().isBlank()) {
            credential.setCredentialId(Integer.parseInt(credentialForm.getId()));
            credentialsService.updateCredential(credential);
        } else {
            credentialsService.createCredential(credential);
        }
        model.addAttribute("success", true);
        model.addAttribute("tab", "nav-credentials-tab");
        model.addAttribute("credentials", credentialsService.getUserCredentials(userId));
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("files", fileService.getUserFiles(userId));
        return "home";
    }

    @RequestMapping(value = "credentials/delete/{id}")
    private String deleteCredential(@PathVariable(name = "id") int id, RedirectAttributes redirectAttributes) {
        credentialsService.deleteCredential(id);
        redirectAttributes.addFlashAttribute("tab", "nav-credentials-tab");
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }
}
