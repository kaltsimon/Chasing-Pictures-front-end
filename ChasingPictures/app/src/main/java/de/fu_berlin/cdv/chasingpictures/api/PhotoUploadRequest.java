package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * API request for sending a new photo to the back end.
 *
 * @author Simon Kalt
 */
public class PhotoUploadRequest extends ApiRequest<Picture> {
    private final Place place;
    private final File picture;

    public PhotoUploadRequest(Context context, @NonNull Place place, @NonNull File picture) {
        super(context, R.string.api_path_upload);
        this.place = place;
        this.picture = picture;
    }

    @Override
    protected ResponseEntity<Picture> send() {
        // Add a form data message converter, before any others
        FormHttpMessageConverter formMessageConverter = new FormHttpMessageConverter();
        restTemplate.getMessageConverters().add(0, formMessageConverter);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>(2);

        // wrap the ID in a string, otherwise Spring won't be able to convert the value
        formData.set("place_id", String.valueOf(place.getId()));

        // Manually set the content type header for the file
        // TODO: Is this always JPEG or should we read the actual type from the file system?
        MultiValueMap<String, String> fileHeaders = new LinkedMultiValueMap<>();
        fileHeaders.set("Content-Type", MediaType.IMAGE_JPEG_VALUE);

        formData.set("file", new HttpEntity<>(new FileSystemResource(picture), fileHeaders));


        return restTemplate.exchange(
                apiUri,
                HttpMethod.POST,
                new HttpEntity<>(formData, headers),
                Picture.class
        );
    }
}
