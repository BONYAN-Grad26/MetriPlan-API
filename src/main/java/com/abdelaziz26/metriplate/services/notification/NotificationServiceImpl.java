package com.abdelaziz26.metriplate.services.notification;

import com.abdelaziz26.metriplate.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl {
    UserRepository userRepository;

    public void notifyUserToUpdateHealthMetrics() {

    }
}
