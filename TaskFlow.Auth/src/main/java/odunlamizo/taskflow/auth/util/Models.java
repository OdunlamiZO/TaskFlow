package odunlamizo.taskflow.auth.util;

import java.util.Base64;
import java.util.Optional;

import odunlamizo.taskflow.auth.dto.FileDto;
import odunlamizo.taskflow.auth.dto.UserDto;
import odunlamizo.taskflow.auth.model.Name;
import odunlamizo.taskflow.auth.model.File;
import odunlamizo.taskflow.auth.model.User;

public final class Models {

    private Models() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToModel(String string, Class<T> cls) {
        if (cls.isAssignableFrom(Name.class)) {
            String[] names = string.split(" ");
            Name name = new Name();
            name.setFirstName(names[0]);
            name.setLastName(names[1]);
            return (T) name;
        }
        throw new UnsupportedOperationException(String.format("Cannot convert to %s", cls));
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2> T2 modelToDto(Optional<T1> model, Class<T2> cls) {
        if (model.isEmpty()) {
            return null;
        }
        if (cls.isAssignableFrom(UserDto.class)) {
            User user = (User) model.get();
            UserDto userDto = new UserDto();
            userDto.setEmail(user.getEmail());
            userDto.setUsername(user.getUsername());
            userDto.setName(user.getName().toString());
            return (T2) userDto;
        }
        if (cls.isAssignableFrom(FileDto.class)) {
            File photo = (File) model.get();
            FileDto fileDto = new FileDto();
            fileDto.setData(Base64.getEncoder().encodeToString(photo.getData()));
            fileDto.setMimeType(photo.getMimeType());
            return (T2) fileDto;
        }
        throw new UnsupportedOperationException(String.format("Cannot convert from % to %s", model.getClass(), cls));
    }
}
