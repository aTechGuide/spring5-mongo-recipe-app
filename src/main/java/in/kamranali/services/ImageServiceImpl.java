package in.kamranali.services;

import java.io.IOException;
import java.util.Optional;

import in.kamranali.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.kamranali.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeID, MultipartFile file) {
        log.debug("Recieved a file");

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeID)
                .map(recipe -> {
                    Byte[] bytes = new Byte[0];
                    try {
                        bytes = new Byte[file.getBytes().length];

                        int i = 0;
                        for (Byte b : file.getBytes()) {
                            bytes[i++] = b;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recipe.setImage(bytes);
                    return recipe;

                });

        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }
}