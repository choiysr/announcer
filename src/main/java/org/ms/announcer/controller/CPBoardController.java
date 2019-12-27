package org.ms.announcer.controller;

import static org.ms.announcer.utils.FileUtil.audioSave;
import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;

import org.ms.announcer.domain.BCBoardDTO;
import org.ms.announcer.domain.CPBoard;
import org.ms.announcer.domain.MemberVO;
import org.ms.announcer.service.CPBoardService;
import org.ms.announcer.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;

@RestController
@RequestMapping("/rcpboard/*")
public class CPBoardController {

    @Setter(onMethod_ = { @Autowired })
    private CPBoardService cpbService;

    @Setter(onMethod_ = { @Autowired })
    private MemberService mService;


    // =======================================================REGISTER 
    @PostMapping(value = "/register")
    public void register(@RequestBody CPBoard[] boards) {
        
        for(CPBoard board : boards) {
            System.out.println("실행확인");
            cpbService.register(board);
        }
    }

    @PostMapping(value = "/registerFiles")
    public ResponseEntity<List<String>> registerFiles(MultipartFile[] files) {
        List<String> list = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileNameWithoutType = file.getOriginalFilename().substring(0,
                        file.getOriginalFilename().lastIndexOf("."));
                list.add(audioSave(fileNameWithoutType, file.getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(list, OK);
    }

 // =======================================================LIST
    @GetMapping("/getCPBoardList/{mid}")
    public ResponseEntity<List<CPBoard>> getList(@PathVariable("mid") String mid) {
        MemberVO vo = mService.findMember(mid);
        List<CPBoard> result = cpbService.getList(vo);
        return new ResponseEntity<>(result, OK);
    }

}