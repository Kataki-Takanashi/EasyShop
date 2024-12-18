package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;
import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // CRUD
    // This is so we can get the logged in user
    @GetMapping
    public Profile getProfile(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        // Get username
        String username = principal.getName();
        
        // get user by username to get ID
        User user = userDao.getByUserName(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // get profile by id
        Profile profile = profileDao.getByUserId(user.getId());
        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        
        return profile;
    }

    @Transactional
    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal) {
        profile.setUserId(userDao.getByUserName(principal.getName()).getId());
        return profileDao.update(profile);
    }
} 