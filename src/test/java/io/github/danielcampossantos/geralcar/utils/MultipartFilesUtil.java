package io.github.danielcampossantos.geralcar.utils;

import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MultipartFilesUtil {
    public List<MockMultipartFile> generateMultipartFiles() {
        return new ArrayList<>(List.of(
                new MockMultipartFile(
                        "file1",
                        "foto1.jpg",
                        "image/jpeg",
                        "fake image content 1".getBytes()
                ),
                new MockMultipartFile(
                        "file2",
                        "foto2.jpg",
                        "image/jpeg",
                        "fake image content 2".getBytes()
                )
        ));
    }
}
