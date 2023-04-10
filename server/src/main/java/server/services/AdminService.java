package server.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
public class AdminService {
    private String serverPassword;

    /**
     * generates a random password for the server after the server starts
     */
    @PostConstruct
    private void initPassword(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        serverPassword = generatedString;
        System.out.println(serverPassword);
    }

    /**
     * checks if the password is correct
     * @param password the password to check
     * @return true if the password is correct
     */
    public boolean checkPassword(String password) {
        System.out.println(serverPassword.equals(password));
        if(serverPassword.equals(password))
            return true;
        return false;
    }
}