package com.ndk.server.model.database

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
open class UserProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int=0
    var username:String=""
    var password:String=""
    var firstName:String=""
    var lastName:String=""
    var avatar:String?= null
    var sex:String="FEMALE"

}