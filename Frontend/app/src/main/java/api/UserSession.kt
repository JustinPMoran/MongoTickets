package api

object UserSession {
    private var userDetails: UserDetails? = null

    fun setUserDetails(details: UserDetails) {
        userDetails = details
    }

    fun getUserDetails(): UserDetails? {
        return userDetails
    }

    fun clearUserDetails() {
        userDetails = null
    }

    fun setUserEmail(email: String) {
        userDetails = userDetails?.copy(email = email) ?: UserDetails(email = email, username = "")
    }

    fun getUserEmail(): String? {
        return userDetails?.email
    }

    data class UserDetails(val email: String, val username: String)
}