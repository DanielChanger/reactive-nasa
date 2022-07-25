package com.changer.reactivenasa;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pictures")
public class ReactiveNasaPictureController {

    private final ReactivePictureService reactivePictureService;

    public ReactiveNasaPictureController(ReactivePictureService reactivePictureService) {
        this.reactivePictureService = reactivePictureService;
    }

    @GetMapping(value = "/{sol}/largest", produces = MimeTypeUtils.IMAGE_PNG_VALUE)
    public Mono<byte[]> getLargestNasaPicture(@PathVariable int sol) {
        return reactivePictureService.getLargestPictureBySol(sol);
    }
}
