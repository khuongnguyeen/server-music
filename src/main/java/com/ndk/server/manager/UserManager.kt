package com.ndk.server.manager

import com.ndk.server.model.database.UserProfile
import com.ndk.server.model.request.RegisterRequest
import com.ndk.server.model.response.CommonResponse
import com.ndk.server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class UserManager {
    @Autowired
    private lateinit var userRepository: UserRepository
    fun register(
            request:RegisterRequest
    ): CommonResponse {
        val user = userRepository.findOnByUsername(request.username)
        if (user != null){
            return CommonResponse("Username existed", 1)
        }
        val userProfile = UserProfile()
        userProfile.avatar=request.avatar
        userProfile.username=request.username
        userProfile.firstName=request.firstName
        userProfile.lastName=request.lastName
        userProfile.sex=request.sex
        userProfile.password=request.password
        userRepository.save(userProfile)
        return CommonResponse(userProfile)
    }
}