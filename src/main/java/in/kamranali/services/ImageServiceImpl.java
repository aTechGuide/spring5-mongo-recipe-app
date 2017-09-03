package in.kamranali.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.kamranali.domain.Recipe;
import in.kamranali.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

	private final RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void saveImageFile(String recipeID, MultipartFile file) {
		log.debug("Recieved a file");

		try {
			Optional<Recipe> recipeOptional = recipeRepository.findById(recipeID);

			if (recipeOptional.isPresent()) {

				Recipe recipe = recipeOptional.get();
				Byte[] bytes = new Byte[file.getBytes().length];

				int i = 0;
				for (Byte b : file.getBytes()) {
					bytes[i++] = b;
				}

				recipe.setImage(bytes);
				recipeRepository.save(recipe);

			} else {
				log.debug("Invalid Recipe ID" + recipeID);
			}
		} catch (Exception e) {
			log.error("Error Occured", e);
		}
	}

}
