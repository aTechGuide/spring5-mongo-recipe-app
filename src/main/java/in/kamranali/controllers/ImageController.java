package in.kamranali.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.kamranali.commands.RecipeCommand;
import in.kamranali.services.ImageService;
import in.kamranali.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ImageController {

	private final ImageService imageService;
	private final RecipeService recipeService;

	public ImageController(ImageService imageService, RecipeService recipeService) {
		this.imageService = imageService;
		this.recipeService = recipeService;
	}
	
	@GetMapping("recipe/{recipeId}/image")
	public String showUploadForm(@PathVariable String recipeId, Model model){
		
		log.debug("Getting image for recipe id:" + recipeId);
		
		model.addAttribute("recipe", recipeService.findCommandById(recipeId));
		return "recipe/imageuploadform";
	}
	
	@PostMapping("recipe/{recipeId}/image")
	public String handleImagePost(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file){
		
		log.debug("Saving image for recipe id:" + recipeId);
		
		imageService.saveImageFile(recipeId, file);
		return "redirect:/recipe/" + recipeId + "/show";
	}
	
	@GetMapping("recipe/{recipeId}/recipeimage")
	public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException{
		
		log.debug("Getting image from DB for recipe id:" + recipeId);
		
		RecipeCommand command = recipeService.findCommandById(recipeId).block();
		
		if(command.getImage() != null){
			byte[] byteArray = new byte[command.getImage().length];
			
			int i = 0;
			for (byte b : command.getImage()) {
				byteArray[i++] = b;
			}
			response.setContentType("image/jpeg");
			IOUtils.copy(new ByteArrayInputStream(byteArray), response.getOutputStream());
		}
	}
}
