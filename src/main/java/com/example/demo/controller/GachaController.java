package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.User;
import com.example.demo.service.GachaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GachaController {
	
	private final GachaService gachaService;
	
	@PostMapping("/gacha/{gachaId}/draw")
	public GachaResult draw(
	        @PathVariable Long gachaId,
	        @RequestParam Long userId) {
		
		return gachaService.drawGacha(userId, gachaId);
	}
	
	@GetMapping("/gacha/{id}")
	public Gacha getGacha(@PathVariable Long id) {
	    return gachaService.findById(id);
	}
	
	@GetMapping("/gachas")
	public List<Gacha> getGachas() {
	    return gachaService.getGachaList();
	}
	
	@GetMapping("/user/{id}")
	public User getUser(@PathVariable Long id) {
	    return gachaService.getUser(id);
	}

}
