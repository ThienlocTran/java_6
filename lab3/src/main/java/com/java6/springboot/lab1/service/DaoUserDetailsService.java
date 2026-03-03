package com.java6.springboot.lab1.service;

import com.java6.springboot.lab1.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service("daoAuth")
public class DaoUserDetailsService implements UserDetailsService {
    final
    UserDAO userDAO;

    public DaoUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDAO.findById(username).get();

            // Lấy mật khẩu và danh sách quyền từ bảng UserRoles
            String password = user.getPassword();
            String[] roles = user.getUserRoles().stream()
                    .map(ur -> ur.getRole().getId())
                    .toArray(String[]::new);

            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(password)
                    .roles(roles) // Chú ý: Nếu ID role đã có ROLE_ thì dùng authorities() thay vì roles()
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}