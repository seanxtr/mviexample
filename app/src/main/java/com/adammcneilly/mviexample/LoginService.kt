package com.adammcneilly.mviexample

class LoginService:LoginRepository {
    override suspend fun login(email: String, password: String): Boolean {
        return true
    }
}