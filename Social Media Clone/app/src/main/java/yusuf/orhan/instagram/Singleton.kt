package yusuf.orhan.instagram

import yusuf.orhan.instagram.Adapter.PostAdapter
import yusuf.orhan.instagram.Model.Posts

object Singleton {
    lateinit var uuid : String
    lateinit var selected_Post : Posts
    var selected_email : String? = null
    lateinit var controller : String
}