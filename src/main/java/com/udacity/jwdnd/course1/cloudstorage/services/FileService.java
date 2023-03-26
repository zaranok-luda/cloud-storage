package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.FileAlreadyExitsException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FilesMapper filesMapper;

    public void createFile(File file) throws FileAlreadyExitsException {
        List<File> dbFile = this.filesMapper.getFileByFilename(file.getUserid(), file.getFilename());
        if (!dbFile.isEmpty()) {
            throw new FileAlreadyExitsException("Such file already exists");
        }

        this.filesMapper.insert(file);
    }

    public void deleteFile(int fileId) {
        this.filesMapper.deleteFile(fileId);
    }

    public List<File> getUserFiles(int userId) {
        return this.filesMapper.getFiles(userId);
    }

    public File getFileById(int fileId) {
        return this.filesMapper.getFileById(fileId);
    }
}
