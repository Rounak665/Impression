package com.rounak.impression.service;


import com.rounak.impression.model.GameImpression;
import com.rounak.impression.repository.GameImpressionRepository;
import lombok.RequiredArgsConstructor; // NEW IMPORT for Lombok
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // NEW: Lombok will generate a constructor for all final fields
public class GameImpressionService { // RENAMED CLASS: Removed 'Impl' suffix


    private final GameImpressionRepository gameImpressionRepository;
    private final CloudinaryService cloudinaryService;



    public List<GameImpression> getAllGameImpressions() {
        return gameImpressionRepository.findAll();
    }

    public Optional<GameImpression> getGameImpressionById(Long id) {
        return gameImpressionRepository.findById(id);
    }

    public GameImpression saveGameImpression(GameImpression gameImpression, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imageFile, "game_impressions");
            gameImpression.setImageUrl(imageUrl);
        }
        return gameImpressionRepository.save(gameImpression);
    }

    public void deleteGameImpression(Long id) {
        Optional<GameImpression> impressionOptional = gameImpressionRepository.findById(id);
        if (impressionOptional.isPresent()) {
            GameImpression impression = impressionOptional.get();
            if (impression.getImageUrl() != null && !impression.getImageUrl().isEmpty()) {
                try {
                    String url = impression.getImageUrl();
                    int uploadIndex = url.indexOf("/upload/");
                    if (uploadIndex != -1) {
                        String publicIdWithVersion = url.substring(uploadIndex + "/upload/".length());
                        int versionIndex = publicIdWithVersion.indexOf('/');
                        String publicId = publicIdWithVersion;
                        if (versionIndex != -1 && publicIdWithVersion.substring(0, versionIndex).matches("v\\d+")) {
                            publicId = publicIdWithVersion.substring(versionIndex + 1);
                        }

                        int dotIndex = publicId.lastIndexOf('.');
                        if (dotIndex != -1) {
                            publicId = publicId.substring(0, dotIndex);
                        }

                        cloudinaryService.deleteFile(publicId);
                    }
                } catch (IOException e) {
                    System.err.println("Error deleting image from Cloudinary: " + e.getMessage());
                }
            }
            gameImpressionRepository.deleteById(id);
        }
    }

    public GameImpression updateGameImpression(Long id, GameImpression gameImpressionDetails, MultipartFile imageFile) throws IOException {
        GameImpression existingImpression = gameImpressionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game impression not found with id: " + id));

        existingImpression.setTitle(gameImpressionDetails.getTitle());
        existingImpression.setDeveloper(gameImpressionDetails.getDeveloper());
        existingImpression.setPublisher(gameImpressionDetails.getPublisher());
        existingImpression.setGenre(gameImpressionDetails.getGenre());
        existingImpression.setReleaseDate(gameImpressionDetails.getReleaseDate());
        existingImpression.setImpressionText(gameImpressionDetails.getImpressionText());
        existingImpression.setRating(gameImpressionDetails.getRating());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (existingImpression.getImageUrl() != null && !existingImpression.getImageUrl().isEmpty()) {
                try {
                    String url = existingImpression.getImageUrl();
                    int uploadIndex = url.indexOf("/upload/");
                    if (uploadIndex != -1) {
                        String publicIdWithVersion = url.substring(uploadIndex + "/upload/".length());
                        int versionIndex = publicIdWithVersion.indexOf('/');
                        String publicId = publicIdWithVersion;
                        if (versionIndex != -1 && publicIdWithVersion.substring(0, versionIndex).matches("v\\d+")) {
                            publicId = publicIdWithVersion.substring(versionIndex + 1);
                        }
                        int dotIndex = publicId.lastIndexOf('.');
                        if (dotIndex != -1) {
                            publicId = publicId.substring(0, dotIndex);
                        }
                        cloudinaryService.deleteFile(publicId);
                    }
                } catch (IOException e) {
                    System.err.println("Error deleting old image from Cloudinary during update: " + e.getMessage());
                }
            }
            String newImageUrl = cloudinaryService.uploadFile(imageFile, "game_impressions");
            existingImpression.setImageUrl(newImageUrl);
        } else if (gameImpressionDetails.getImageUrl() == null || gameImpressionDetails.getImageUrl().isEmpty()) {
            if (existingImpression.getImageUrl() != null && !existingImpression.getImageUrl().isEmpty()) {
                try {
                    String url = existingImpression.getImageUrl();
                    int uploadIndex = url.indexOf("/upload/");
                    if (uploadIndex != -1) {
                        String publicIdWithVersion = url.substring(uploadIndex + "/upload/".length());
                        int versionIndex = publicIdWithVersion.indexOf('/');
                        String publicId = publicIdWithVersion;
                        if (versionIndex != -1 && publicIdWithVersion.substring(0, versionIndex).matches("v\\d+")) {
                            publicId = publicIdWithVersion.substring(versionIndex + 1);
                        }
                        int dotIndex = publicId.lastIndexOf('.');
                        if (dotIndex != -1) {
                            publicId = publicId.substring(0, dotIndex);
                        }
                        cloudinaryService.deleteFile(publicId);
                    }
                } catch (IOException e) {
                    System.err.println("Error deleting image from Cloudinary when clearing URL: " + e.getMessage());
                }
            }
            existingImpression.setImageUrl(null);
        }

        return gameImpressionRepository.save(existingImpression);
    }
}