package com.job.delivery.config;

import com.job.delivery.entity.SignUpRequest;
import com.job.delivery.entity.User;
import com.job.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> byUsername = userRepository.findByUsername(username);
//        byUsername.orElseThrow(() -> new UsernameNotFoundException("Not Found:  " + username));
//        return byUsername.map(MyUserDetails::new).get();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    @Transactional
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), bCryptPasswordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

}
