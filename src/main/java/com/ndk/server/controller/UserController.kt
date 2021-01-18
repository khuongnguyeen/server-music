package com.ndk.server.controller

import com.ndk.server.manager.UserManager
import com.ndk.server.model.request.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
open class UserController {
    @Autowired
    private lateinit var manager: UserManager
    @PostMapping("/api/register")
    fun register(
            @RequestBody request:RegisterRequest
    ) : Any{

        return manager.register(request)
    }


    @GetMapping("/api/friend")
    fun makeFriend(
        @RequestParam("sender_id", required = false) senderId: Int,
        @RequestParam("receiver_id", required = false) receiverId: Int
    ) : Any{

        return ""
    }
}