package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.FileAlreadyExitsException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileController implements HandlerExceptionResolver {
    @Autowired
    private FileService fileService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialsService;

    @PostMapping("/files")
    public String uploadFile(Model model, @RequestParam("fileUpload") MultipartFile file, Authentication authentication,
                             @ModelAttribute("note") NoteForm note, @ModelAttribute("credential") CredentialForm credentialForm) {
        String username = authentication.getName();
        int userId = usersMapper.getUser(username).getUserId();
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid File");
        } else {
            File f = new File();
            f.setContenttype(file.getContentType());
            f.setFilename(file.getOriginalFilename());
            f.setUserid(userId);
            f.setFilesize(file.getSize() + "");
            try {
                f.setFiledata(file.getBytes());
                fileService.createFile(f);
                model.addAttribute("success", "File saved!");
            } catch (FileAlreadyExitsException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "File already exists!");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Unknown error!");
            }
        }
        model.addAttribute("tab", "nav-files-tab");
        model.addAttribute("files", fileService.getUserFiles(userId));
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("credentials", credentialsService.getUserCredentials(userId));

        return "home";
    }

    @RequestMapping(value = {"/files/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> viewFile(@Valid @PathVariable(name = "id") int id, HttpServletResponse response, HttpServletRequest request) {
        File file = fileService.getFileById(id);
        byte[] fileContents = file.getFiledata();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(file.getContenttype()));
        String fileName = file.getFilename();
        httpHeaders.setContentDispositionFormData(fileName, fileName);
        httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(fileContents, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "files/delete/{id}")
    private String deleteFile(@Valid @PathVariable(name = "id") int id, RedirectAttributes redirectAttributes) {
        fileService.deleteFile(id);
        redirectAttributes.addFlashAttribute("tab", "nav-files-tab");
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
    {
        Map<String, Object> model = new HashMap<>();
        if (exception instanceof MaxUploadSizeExceededException) {
            model.put("errorMessage", "File size limit exceeded (1MB)!");
        }
        return new ModelAndView("home", model);
    }
}

