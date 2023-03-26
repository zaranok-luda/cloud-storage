package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private CredentialService credentialsService;

    @PostMapping("/notes")
    public String postNote(@ModelAttribute("credential") CredentialForm credentialForm, Model model, @ModelAttribute("note") NoteForm note, Authentication authentication) {
        String username = authentication.getName();
        int userId = usersMapper.getUser(username).getUserId();
        Note noteObj = new Note();

        noteObj.setUserId(userId);
        noteObj.setNoteTitle(note.getTitle());
        noteObj.setNotedescription(note.getDescription());
        if (!note.getId().isBlank()) {
            noteObj.setNoteId(Integer.parseInt(note.getId()));
            noteService.updateNote(noteObj);
        } else {
            noteService.createNote(noteObj);
        }
        model.addAttribute("success", true);
        model.addAttribute("tab", "nav-notes-tab");
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("files", fileService.getUserFiles(userId));
        model.addAttribute("credentials", credentialsService.getUserCredentials(userId));
        return "home";
    }

    @RequestMapping(value = "/delete/{id}")
    private String deleteNote(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        noteService.deleteNote(Integer.parseInt(id));
        redirectAttributes.addFlashAttribute("tab", "nav-notes-tab");
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }
}

